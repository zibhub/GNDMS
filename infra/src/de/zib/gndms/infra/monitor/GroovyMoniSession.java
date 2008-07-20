package de.zib.gndms.infra.monitor;

import com.oreilly.servlet.Base64Decoder;
import com.oreilly.servlet.multipart.MultipartParser;
import com.oreilly.servlet.multipart.ParamPart;
import com.oreilly.servlet.multipart.Part;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

	public synchronized PrintWriter init() {
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

	private GroovyShell createShell() {
		BindingFactory bindingFactory = console.getBindingFactory();
		Binding binding = bindingFactory == null ?
			  new Binding() : bindingFactory.createBinding(console, principal);
		binding.setProperty("out", outWriter);
		GroovyShell theShell = new GroovyShell(binding);
		theShell.initializeBinding();
		return theShell;
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

	synchronized boolean isRemainingOpen() {
		return !RunMode.CLOSE.equals(runMode) && (runMode.isLooping() || !isPostDone());
	}

	@SuppressWarnings({"NakedNotify"})
	synchronized void evalParts(HttpServletRequest servletRequest, boolean b64) throws IOException {
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

	private void handlePart(boolean b64, Part part) throws UnsupportedEncodingException {
		if (part.isFile()) {
			// TODO: Change this
			// currently unsupported..
		}
		else if (part.isParam())
		{
			String val = ((ParamPart)part).getStringValue();
			handleString(b64, val);
		}
	}

	private synchronized void handleString(boolean b64, String val) {
		final GroovyShell theShell = getShell();
		if (theShell == null)
			throw new RuntimeException("Cant evaluate without shell");
		Object result = theShell.evaluate(b64 ? Base64Decoder.decode(val) : val);
		if (runMode.isEvaled())
			println(result);
	}

	private void println(@Nullable Object obj) {
		outWriter.println(obj == null ? "null" : obj);
	}

	public void verifyPrincipal(@NotNull Principal thePrincipal) {
		if (! principal.equals(thePrincipal))
			throw new SecurityException("Principal mismatch");
	}

	public synchronized void removeMoniSession(
		  Set<GroovyMoniSession> sessions, @NotNull HttpSession session) {
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

