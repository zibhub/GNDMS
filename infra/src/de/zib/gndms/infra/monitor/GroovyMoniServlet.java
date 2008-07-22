package de.zib.gndms.infra.monitor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * Servlet that serve's groovy shells (used by GroovyMoniConsole).
*
* @author Stefan Plantikow <plantikow@zib.de>
* @version $Id$
*
*          User: stepn Date: 18.07.2008 Time: 02:20:30
*/
public final class GroovyMoniServlet extends HttpServlet {

	private static final long serialVersionUID = 4561717404398330474L;
	private static final int EXPECTED_NUM_PARALLEL_SESSIONS = 16;

	private transient GroovyMoniConsole console;

	private final transient Set<GroovyMoniSession> sessions =
		new HashSet<GroovyMoniSession>(EXPECTED_NUM_PARALLEL_SESSIONS);

	private String roleName;

	@Override
	public synchronized void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);    // Overridden method
		final ServletContext servletContext = servletConfig.getServletContext();
		console = (GroovyMoniConsole) servletContext.getAttribute("console");
		roleName = (String) servletContext.getAttribute("roleName");
	}

	@SuppressWarnings({"FeatureEnvy"})
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		  throws IOException
	{
		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request);

		try {
			verifyUserRole(request);
			refreshOnRequest(requestWrapper);


			String token = parseToken(requestWrapper);
			if (token.length() == 0) {
				createNewSession(request);
				response.setStatus(HttpServletResponse.SC_OK);
				return;
			}

			HttpSession session= getSessionForDoGet(request);
			
			PrintWriter outWriter = null;
			GroovyMoniSession mSession = null;
			try {
				mSession = createMoniSession(requestWrapper, session, token, response);
				if (mSession.getRunMode().equals(GroovyMoniSession.RunMode.CLOSE)) {
					response.setStatus(HttpServletResponse.SC_OK);
					return;
				}
				outWriter = mSession.init();
				response.setStatus(HttpServletResponse.SC_OK);
				mSession.waitLoop();
			}
			finally {
				if (outWriter != null)
					outWriter.close();
				if (mSession != null) {
					synchronized (sessions) {
						mSession.removeMoniSession(sessions, session);
					}
				}
			}
		}
		catch (ServletRuntimeException e) {
			e.throwToClient(response);
		}
	}

	private static void createNewSession(HttpServletRequest request) {request.getSession(true);}

	private static HttpSession getSessionForDoGet(HttpServletRequest request) {
		final HttpSession session;
		session = request.getSession(false);
		if (session == null)
			throw ServletRuntimeException.preCondFailed("No session found");
		return session;
	}

	private synchronized void refreshOnRequest(HttpServletRequestWrapper requestWrapper) {
		if ("refresh".equalsIgnoreCase(requestWrapper.getParameter("m"))) {
			try {
				console.refresh();
				throw new ServletRuntimeException(HttpServletResponse.SC_RESET_CONTENT, "Refreshed");
			}
			catch (Exception e) {
				throw new ServletRuntimeException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
			}
		}
	}

	@SuppressWarnings({"NakedNotify", "FeatureEnvy"})
	@Override
	protected void doPost(
		  HttpServletRequest servletRequest, HttpServletResponse servletResponse)
		  throws ServletException, IOException
	{
		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(servletRequest);

		try {
			verifyUserRole(servletRequest);

			String token = parseToken(requestWrapper);
			if (token.length() == 0)
				throw ServletRuntimeException.notAcceptable("Zero-length token");

			HttpSession session = getSessionForDoPost(servletRequest);

			final Principal principal = servletRequest.getUserPrincipal();
			final GroovyMoniSession mSession =
				  lookupMoniSession(principal, session, token);
			if (mSession == null)
				throw ServletRuntimeException.preCondFailed("Unknown token");

			mSession.evalParts(servletRequest, shouldDecodeBase64(requestWrapper));
			servletResponse.setStatus(HttpServletResponse.SC_OK);
		}
		catch (ServletRuntimeException e) {
			e.throwToClient(servletResponse);
		}
	}

	private HttpSession getSessionForDoPost(HttpServletRequest servletRequest) {
		HttpSession session = servletRequest.getSession(false);
		if (session == null)
			throw ServletRuntimeException.preCondFailed("No session found");
		return session;
	}

	@Override
	public void destroy() {
		super.destroy();    // Overridden method
		for (GroovyMoniSession session : sessions) {
			synchronized (session) {
				session.setRunMode(GroovyMoniSession.RunMode.CLOSE);
				session.notifyAll();
			}
		}
	}

	private synchronized void verifyUserRole(HttpServletRequest servletRequest) {
		if (! servletRequest.isUserInRole(roleName))
			throw ServletRuntimeException.unauthorized("User not in required role");
	}

	@SuppressWarnings({"InstanceMethodNamingConvention"})
	private static boolean shouldDecodeBase64(HttpServletRequestWrapper requestWrapper) {
		return "1".equals(requestWrapper.getParameter("b64"));
	}

	@NotNull
	@SuppressWarnings({"unchecked", "NonSerializableObjectBoundToHttpSession"})
	GroovyMoniSession createMoniSession(
		  @NotNull HttpServletRequestWrapper request, @NotNull HttpSession session,
		  @NotNull String token,
		  @NotNull HttpServletResponse response) throws IOException {
		GroovyMoniSession mSession;
		Principal principal = request.getUserPrincipal();

		synchronized (session) {
			mSession = (GroovyMoniSession) session.getAttribute(token);
			if (mSession == null) {
				GroovyMoniSession.RunMode mode = parseMode(request, console.getDefaultMode());
				if (mode == GroovyMoniSession.RunMode.CLOSE)
					throw ServletRuntimeException.badRequest("Cant close unavailable token");
				mSession = new GroovyMoniSession(console, principal, token,
					  mode, response.getWriter());
				session.setAttribute(token, mSession);
			}
			else {
				GroovyMoniSession.RunMode mode = parseMode(request, console.getDefaultMode());
				if (GroovyMoniSession.RunMode.CLOSE.equals(mode)) {
					// mSession.removeMoniSession(session) will be calle by doGet finalizer
					mSession.setRunMode(mode);
					return mSession;
				}
				else
					throw ServletRuntimeException.badRequest("Token already open");
			}
		}
		synchronized (sessions) { sessions.add(mSession); }

		return mSession;
	}

	@Nullable
	@SuppressWarnings({"unchecked", "NonSerializableObjectBoundToHttpSession"})
	static GroovyMoniSession lookupMoniSession(
		  @NotNull Principal principal, @NotNull HttpSession session, @NotNull String token)
	{
		synchronized (session) {
			final GroovyMoniSession mSession =
				  (GroovyMoniSession) session.getAttribute(token);
			if (mSession == null)
				return null;
			mSession.verifyPrincipal(principal);
			return mSession;
		}
	}

	private static String parseToken(HttpServletRequestWrapper requestWrapper) {
		String token = requestWrapper.getParameter("token");
		return token == null ? "" : token.trim();
	}

	public static GroovyMoniSession.RunMode parseMode(
		  HttpServletRequestWrapper requestWrapper, GroovyMoniSession.RunMode defaultMode) {
			final String param = requestWrapper.getParameter("m");
			return param == null ?
				  defaultMode : GroovyMoniSession.RunMode.valueOf(param.trim().toUpperCase());
		}

}