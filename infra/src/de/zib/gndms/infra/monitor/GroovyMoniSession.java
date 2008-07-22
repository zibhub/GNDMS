package de.zib.gndms.infra.monitor;

import com.oreilly.servlet.Base64Decoder;
import com.oreilly.servlet.multipart.FilePart;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.apache.tools.ant.filters.StringInputStream;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Set;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 19.07.2008 Time: 13:08:00
 */
public class GroovyMoniSession {
	private static final int INITSTREAM_CAPACITY = 1024;

	public enum RunMode {
		REPL(true, true), BATCH(false, true), SCRIPT(false, false), EVAL_SCRIPT(true, false),
		CLOSE(false, false);

		private final boolean evaled;
		private final boolean looping;


		RunMode(boolean eval, boolean loop) {
			evaled = eval;
			looping = loop;
		}

		public boolean isEvaled() {
			return evaled;
		}

		public boolean isLooping() {
			return looping;
		}
	}

	private static final int WAIT_LOOP_DELAY = 750;

	@NotNull
	private final GroovyMoniConsole console;

	@NotNull
	private final Principal principal;

	@NotNull
	private final String token;

	@Nullable
	private GroovyShell shell;

	@NotNull
	final PrintWriter outWriter;

	@NotNull
	private RunMode runMode;

	private boolean doneOnce;

	@SuppressWarnings({"IOResourceOpenedButNotSafelyClosed"})
	public GroovyMoniSession(
		  @NotNull GroovyMoniConsole theConsole, @NotNull Principal thePrincipal,
		  @NotNull String theToken,
		  @NotNull RunMode theMode, @NotNull PrintWriter theOutWriter
	) {
		token = theToken;
		console = theConsole;
		principal = thePrincipal;
		outWriter = theOutWriter;
		runMode = theMode;
	}

	public synchronized PrintWriter init() throws IOException {
		shell = createShell();
		notifyAll();
		return outWriter;
	}

	public synchronized void ensureShell(long timeout) {
		while (! hasShell()) {
			try { wait(timeout); }
			catch (InterruptedException e) {
				if (timeout > 0)
					throw new ServletRuntimeException(
						  HttpServletResponse.SC_GATEWAY_TIMEOUT,
						  "No shell was created within timeout");
			}
		}
	}

	public synchronized boolean hasShell() {
		return shell != null;
	}

	@NotNull
	private GroovyShell createShell() throws IOException {
		CompilerConfiguration config = new CompilerConfiguration();
		config.setOutput(outWriter);

		Binding binding = createBinding();

		GroovyShell theShell = new GroovyShell(binding, config);
		theShell.initializeBinding();

		runInitStream(theShell);

		return theShell;
	}

	private void runInitStream(@NotNull GroovyShell theShell) throws IOException {
		InputStream theInitStream = null;
		try {
			theInitStream = getInitStream();
			runStream(theShell, theInitStream, false);
		}
		finally {
			if (theInitStream != null)
				theInitStream.close();
		}
	}

	@NotNull
	private Binding createBinding() {
		BindingFactory bindingFactory = console.getBindingFactory();
		Binding binding = bindingFactory == null ?
			  new Binding() : bindingFactory.createBinding(console, principal);
		binding.setVariable("out", outWriter);
		binding.setVariable("err", outWriter);
		binding.setProperty("out", outWriter);
		binding.setProperty("err", outWriter);
		return binding;
	}

	private Object runStream(
		  @NotNull GroovyShell theShell, @NotNull InputStream inStream, boolean evaled) {
		final Script script = theShell.parse(inStream);

		script.setProperty("out", outWriter);
		script.setProperty("err", outWriter);

		outWriter.flush();
		final Object result = script.run();
		if (evaled)
			script.println(result);
		outWriter.flush();
		return result;
	}

	@NotNull
	@SuppressWarnings({"HardcodedLineSeparator"})
	private static InputStream getInitStream() {
		StringBuilder builder = new StringBuilder(INITSTREAM_CAPACITY);
		builder.append("ExpandoMetaClass.enableGlobally()\n");
		builder.append("Object.metaClass.out=out\n");
		builder.append("Object.metaClass.err=out\n");
		return new StringInputStream(builder.toString());
	}

	synchronized void waitLoop() {

		do {
			try {
				wait(WAIT_LOOP_DELAY); }
			catch (InterruptedException e) { /* ignored */ }
			outWriter.flush();
		}

		while (isRemainingOpen());
	}

	private synchronized boolean isRemainingOpen() {
		return !RunMode.CLOSE.equals(runMode) && (runMode.isLooping() || !isPostDone());
	}

	synchronized void evalParts(@NotNull HttpServletRequest servletRequest, boolean b64)
		  throws IOException {
		try {
			if (isRemainingOpen()) {
				MultipartParser mParser =  new MultipartParser(servletRequest, console.getMaxScriptSize());
				for (Part part = mParser.readNextPart(); part != null;
					 part = mParser.readNextPart())
					handlePart(b64, part);
			}
			else {
				throw new RuntimeException("Mode " + runMode
					  + " doesnt support multiple requests");
			}
		}
		finally {
			postDone();
			notifyAll();
		}
	}

	private synchronized void handlePart(boolean b64, @NotNull Part part) throws IOException {
		if (part.isFile()) {
			InputStream in = ((FilePart)part).getInputStream();
			handleStream(b64, in);
		}
		else if (part.isParam())
		{
			String val = ((ParamPart)part).getStringValue();
			StringInputStream valStream = null;
			try {
				valStream = new StringInputStream(val);
				handleStream(b64, valStream);
			}
			finally {
				if (valStream != null)
					valStream.close();
			}
		}
	}

	private synchronized void handleStream(boolean b64, @NotNull InputStream val) throws IOException {
		final GroovyShell theShell = getShell();
		synchronized (theShell) {
			if (theShell == null)
				throw new RuntimeException("Cant evaluate without shell");
			InputStream decodedStream = null;
			try {
				decodedStream = getDecodedValStream(b64, val);
				runStream(theShell, decodedStream, runMode.isEvaled());
			}
			finally {
				if (decodedStream != null) {
					decodedStream.close();
					if (val != decodedStream)
						val.close();
				}
			}
		}
	}

	@NotNull
	private static InputStream getDecodedValStream(boolean b64, @NotNull InputStream val) throws IOException {
		InputStream val1;
		if (b64) {
			val1 = new Base64Decoder(val);
		}
		else
			val1 = val;
		return val1;
	}

	public synchronized void verifyPrincipal(@NotNull Principal thePrincipal) {
		if (! principal.equals(thePrincipal))
			throw new SecurityException("Principal mismatch");
	}

	public synchronized void removeMoniSession(
		 @NotNull Set<GroovyMoniSession> sessions, @NotNull HttpSession session) {
		synchronized (session) {
			sessions.remove(this);
			runMode = RunMode.CLOSE;
			session.removeAttribute(getToken());
			notifyAll();
		}
	}

	boolean isPostDone() {
		return doneOnce;
	}

	void postDone() {
		doneOnce = true;
	}

	@NotNull
	public String getToken() {
		return token;
	}

	@NotNull
	public Principal getPrincipal() {
		return principal;
	}

	@Nullable
	public synchronized GroovyShell getShell() {
		return shell;
	}


	@NotNull
	public synchronized RunMode getRunMode() {
		return runMode;
	}

	public synchronized void setRunMode(@NotNull RunMode theMode) {
		runMode = theMode;
	}	
}

