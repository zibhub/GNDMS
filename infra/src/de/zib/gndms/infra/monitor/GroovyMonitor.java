package de.zib.gndms.infra.monitor;

import com.oreilly.servlet.Base64Decoder;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.*;
import java.io.*;
import java.security.Principal;

/**
 * Instances represent a multi-way connection point between an output stream (get-request) and
 * input-streams (post-request) of a GroovyMonitorServlet which can be used to execute server-side
 * groovy script code in a stateful manner.
 * <br />
 * 
 * Thread safe.
 *
 * @see GroovyMoniServlet
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 19.07.2008 Time: 13:08:00
 */
@SuppressWarnings({"HardcodedLineSeparator"})
final class GroovyMonitor implements HttpSessionBindingListener, HttpSessionActivationListener {

	/**
	 * Describes how code is to be executed by a GroovyMoniSession instance
	 *
	 */
	enum RunMode {
		REPL(true, true), BATCH(false, true), SCRIPT(false, false), EVAL_SCRIPT(true, false),
		// reserved to denote end of execution
		CLOSE(false, false);

		/**
		 * Determined if the result of calls to script.run() be printed on out
		 */
		private final boolean evaled;

		/**
		 * Determines if a two-way connection is closed after the first post-request or if
		 * it is kept-open for multiple requests
		 */
		private final boolean looping;


		RunMode(boolean eval, boolean loop) {
			evaled = eval;
			looping = loop;
		}

		boolean isEvaled() {
			return evaled;
		}

		boolean isLooping() {
			return looping;
		}
	}

	@SuppressWarnings({"FieldCanBeLocal"})
	private static final String INIT_SCRIPT;

	private static final int INIT_SCRIPT_SIZE_BOUND = 1024;

	static {
		StringBuilder builder = new StringBuilder(INIT_SCRIPT_SIZE_BOUND);
		builder.append("ExpandoMetaClass.enableGlobally()\n");
		builder.append("Object.metaClass.out=out\n");
		builder.append("Object.metaClass.err=out\n");
		INIT_SCRIPT = builder.toString();
	}

	/**
	 *  Timeout used in out-stream flushing wait-loop
	 */
	private static final int WAIT_LOOP_DELAY = 2300;

	/**
	 * The console object that runs the servlet engine that hosts the servlet that uses this
	 */
	@Nullable
	private GroovyMoniServer moniServer;

	/**
	 * Principal of the get-request that created this instance
	 */
	@Nullable
	private Principal principal;

	/**
	 * Session-unique token that identifies this instance
	 */
	@NotNull
	private final String token;

	/**
	 * GroovyShell object that executes code received via incoming post-requests
	 */
	@Nullable
	private final GroovyShell shell;

	@NotNull
	private final PrintWriter outWriter;

	@NotNull
	private RunMode runMode;

	/**
	 * True iff at least one post-request was executed by this monitor.  It is not relevant
	 * whether that execution was succesfull or not.
	 *
	 */
	private boolean doneOnce;

	@SuppressWarnings({"FieldHasSetterButNoGetter"})
	@Nullable
	private HttpSession session;

	/**
	 *
	 * @param theMoniServer the responsible monitor server for this instances' servlet
	 * @param thePrincipal that created this monitor
	 * @param theToken that is used to identify this monitor in principal's session
	 * @param theMode @see RunMode
	 * @param args a plain argument string passend on to the GroovyBindingFactory
	 * @param theOutWriter output stream of the get-requests's connection 
	 * @throws IOException if something is afoul with the stream
	 */
	@SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
	GroovyMonitor(
		  @NotNull GroovyMoniServer theMoniServer, @NotNull Principal thePrincipal,
		  @NotNull String theToken,
		  @NotNull RunMode theMode, @NotNull String args, @NotNull PrintWriter theOutWriter
	) throws IOException {
		token = theToken;
		moniServer = theMoniServer;
		principal = thePrincipal;
		outWriter = theOutWriter;
		runMode = theMode;
		shell = createShell(args);
	}


	/**
	 * @return a newly created shell that writes to outStream, uses bindings created by
	 * createBinding() and has already processed the contents of getInitStream()
	 *
	 * @throws IOException
	 * @param args
	 */
	@NotNull
	private GroovyShell createShell(@NotNull String args) throws IOException {
		CompilerConfiguration config = new CompilerConfiguration();
		config.setOutput(outWriter);

		final GroovyBindingFactory bindingFactory = getMoniServer().getBindingFactory();
		Binding binding = createBinding(args, bindingFactory);

		GroovyShell theShell = new GroovyShell(binding, config);
		theShell.initializeBinding();

		runInitStream(theShell);
		bindingFactory.initShell(theShell, binding);

		return theShell;
	}

	/**
	 * Calls BindingFactory and adds points "out" and "err" properties to outWriter
	 *
	 * @return new binding
	 * @param args
	 * @param bindingFactory
	 */
	@NotNull
	private synchronized Binding createBinding(
		  @NotNull String args, @NotNull GroovyBindingFactory bindingFactory) {
		Binding binding = bindingFactory.createBinding(getMoniServer(), getPrincipal(), args);
		binding.setVariable("out", outWriter);
		binding.setVariable("err", outWriter);
		binding.setProperty("out", outWriter);
		binding.setProperty("err", outWriter);
		return binding;
	}

	private void runInitStream(@NotNull GroovyShell theShell) throws IOException {
		InputStream theInitStream = null;
		try {
			theInitStream = getInitStream();
			runStream(theShell, "", theInitStream, false);
		}
		finally {
			if (theInitStream != null)
				theInitStream.close();
		}
	}

	/**
	 * The initStream is evaluated by the shell prior to the processing of post-requests.
	 *
	 * Currently adds "out" and "err" properties to Object.metaClass for easy output from
	 * within classes.
	 *
	 * @return a new init stream
	 */
	@NotNull
	@SuppressWarnings({"HardcodedLineSeparator"})
	private static InputStream getInitStream() {
        try {
            return new ByteArrayInputStream(INIT_SCRIPT.getBytes("utf8"));
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

     /**
	 * Explicitely sets "out" and "err" properties to outWriter before running the script
	 * read from inStream.
	 *
	 * @param theShell
	 * @param args  to the stream/script, bound to "args" property
	 * @param inStream
	 * @param printEvaled if true, the result object is printed to outWriter via script.println()
	 * @return the result of evaluating inStream
	 */
	private Object runStream(
		  @NotNull GroovyShell theShell, String args, @NotNull InputStream inStream,
		  boolean printEvaled) throws IOException
     {
         final InputStreamReader streamReader = new InputStreamReader(inStream, "utf8");
         final StringBuilder builder = new StringBuilder(INIT_SCRIPT.length());
         final BufferedReader bufReader= new BufferedReader(streamReader);
         try {
             String line = bufReader.readLine();
             while (line != null) {
                 builder.append(line);
                 builder.append('\n');
                 line = bufReader.readLine();
             }
         }
         finally {
                bufReader.close();
         }

		 final Script script = theShell.parse(builder.toString());

		 script.setProperty("out", outWriter);
		 script.setProperty("err", outWriter);
		 script.setProperty("args", args);

		 outWriter.flush();
		 final Object result = script.run();
		 if (printEvaled)
		 	script.println(result);
		 outWriter.flush();
		 return result;
	}

	/**
	 * Main wait loop on the output stream. Wakes up every WAIT_LOOP_DELY ms
	 * to flush the stream or on notify().
	 *
	 */
	synchronized void waitLoop() {

		do {
			try {
				wait(WAIT_LOOP_DELAY); }
			catch (InterruptedException e) { /* ignored */ }
			outWriter.flush();
		}

		while (isRemainingOpen());
	}

	/**
	 * outWriter closing condition: Not in CLOSE mode and either looping or no post-request
	 * processed so far
	 *
	 * @return true, if processing should continue and outWriter may not yet be closed down
	 */
	private synchronized boolean isRemainingOpen() {
		return !RunMode.CLOSE.equals(runMode) && (runMode.isLooping() || !isPostDone());
	}

	/**
	 * Evaluate the multiparts of a http multipart request as groovy shell script code.
	 *
	 * @param servletRequest
	 * @param args to the script/command (plain string)
	 *@param b64 if true, the content is asumed to be encoded in base64 @throws IOException
	 */
	synchronized void evalParts(@NotNull HttpServletRequest servletRequest,
	                            @NotNull String args, boolean b64)
		  throws IOException {
		try {
			if (isRemainingOpen()) {
				MultipartParser parser =
					  new MultipartParser(servletRequest, getMoniServer().getMaxScriptSizeInBytes());
				for (Part part = parser.readNextPart(); part != null; part = parser.readNextPart())
					handlePart(b64, args, part);
			}
			else {
				throw new RuntimeException("Processing of multiple requests on a monitor in "
					  + " mode " + runMode + " is either invalid "
				      + " or monitor was closed down in the middle of processing");
			}
		}
		finally {
			postDone();
			notifyAll();
		}
	}

	/**
	 * Processing of single parts.
	 * <br />
	 *
	 * Converts non-file parts into string streams and handles base64 decoding.
	 *
	 * @param b64 if true, the content is asumed to be encoded in base64
	 * @param args args to the part/script
	 *@param part @throws IOException
	 */
	private synchronized void handlePart(boolean b64, @NotNull String args, @NotNull Part part)
		  throws IOException {
		if (part.isFile()) {
			InputStream in = ((FilePart)part).getInputStream();
			handleStream(b64, args, in);
		}
		else if (part.isParam())
		{
			final String val = ((ParamPart)part).getStringValue();
            final InputStream valStream = new ByteArrayInputStream(val.getBytes("utf8"));
			try {
				handleStream(b64, args, valStream);
			}
			finally {
    			valStream.close();
			}
		}
	}

	@SuppressWarnings({ "SynchronizationOnLocalVariableOrMethodParameter" })
    private synchronized void handleStream(
		  boolean b64, final @NotNull String args, final @NotNull InputStream val)
            throws IOException
    {
		final @NotNull GroovyShell theShell = getShell();
		synchronized (theShell) {
			final InputStream decodedStream = getDecodedValStream(b64, val);
			try {
				runStream(theShell, args, decodedStream, runMode.isEvaled());
			}
			finally {
                decodedStream.close();
                if (val != decodedStream)
                    val.close();
			}
		}
	}

	private static @NotNull InputStream getDecodedValStream(boolean b64,
                                                            final @NotNull InputStream val)
		  throws IOException {
		final @NotNull InputStream val1;
		if (b64) {
			val1 = new Base64Decoder(val);
		}
		else
			val1 = val;
		return val1;
	}

	synchronized void verifyPrincipal(@NotNull Principal thePrincipal) {
		if (! getPrincipal().equals(thePrincipal))
			throw new SecurityException("Principal mismatch");
	}

	public synchronized void valueBound(HttpSessionBindingEvent event) {
		setSession(event.getSession());
	}

	private synchronized void setSession(@NotNull HttpSession httpSession) {
		if (session != null)
			throw new RuntimeException("Attempt to overwrite monitor session");
		session = httpSession;
	}

	public synchronized void valueUnbound(HttpSessionBindingEvent event) {
		if (event.getSession() == session) {
			session = null;
			final GroovyMoniServer theSerer = getMoniServer();
			theSerer.getBindingFactory().destroyBinding(theSerer, getShell().getContext());
			runMode = RunMode.CLOSE;
			notifyAll();
		}
	}

	public void sessionDidActivate(HttpSessionEvent event) {
		// That may be
	}

	public synchronized void sessionWillPassivate(HttpSessionEvent event) {
		final HttpSession evSession = event.getSession();
		if (evSession !=null) {
			evSession.removeAttribute(getToken());
		}
	}
	
	synchronized boolean isPostDone() {
		return doneOnce;
	}

	synchronized void postDone() {
		doneOnce = true;
	}

	@NotNull
	String getToken() {
		return token;
	}

	@NotNull
	private synchronized Principal getPrincipal() {
		if (principal == null)
			throw new IllegalStateException("null Principal encountered where not allowed");
		return principal;
	}

	@NotNull
	synchronized GroovyShell getShell() {
		if (shell == null)
			throw new IllegalStateException("null shell encountered where not allowed");
		return shell;
	}


	@NotNull
	synchronized RunMode getRunMode() {
		return runMode;
	}

	synchronized void setRunMode(@NotNull RunMode theMode) {
		runMode = theMode;
	}

	@NotNull
	private synchronized GroovyMoniServer getMoniServer() {
		if (moniServer == null)
			throw new IllegalStateException("null moniServer encountered where not allowed");
		return moniServer;
	}


}

