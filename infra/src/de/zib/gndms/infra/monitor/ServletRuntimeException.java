package de.zib.gndms.infra.monitor;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 20.07.2008 Time: 01:08:59
 */
@SuppressWarnings({"ThrowableInstanceNeverThrown"})
public class ServletRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -7914136302943649643L;
	private final int errorCode;

	public ServletRuntimeException(int theErrorCode, String message) {
		super(message);
		errorCode = theErrorCode;
	}

	public ServletRuntimeException(int theErrorCode,String message, Throwable cause) {
		super(message, cause);
		errorCode = theErrorCode;
	}

	public ServletRuntimeException(int theErrorCode, Throwable cause)  {
		super(cause);
		errorCode = theErrorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void throwToClient(HttpServletResponse response) throws IOException {
		String msg = getMessage();
		if (msg == null)
			response.sendError(errorCode);
		else
			response.sendError(errorCode, msg);
		response.getOutputStream().flush();
		response.flushBuffer();
		throw this;
	}

	// Shorcut functions for a selection of common error codes

	public static ServletRuntimeException preCondFailed(String s) {
		return new ServletRuntimeException(HttpServletResponse.SC_PRECONDITION_FAILED, s);
	}

	public static ServletRuntimeException forbidden(String s) {
		return new ServletRuntimeException(HttpServletResponse.SC_FORBIDDEN, s);
	}

	public static ServletRuntimeException notAcceptable(String s) {
		return new ServletRuntimeException(HttpServletResponse.SC_NOT_ACCEPTABLE, s);
	}

	public static ServletRuntimeException badRequest(String s) {
		return new ServletRuntimeException(HttpServletResponse.SC_BAD_REQUEST, s);
	}

	public static ServletRuntimeException unauthorized(String s) {
		return new ServletRuntimeException(HttpServletResponse.SC_UNAUTHORIZED, s);
	}

	public static ServletRuntimeException expectationFailed(String s) {
		return new ServletRuntimeException(HttpServletResponse.SC_EXPECTATION_FAILED, s);
	}

	public static ServletRuntimeException internalError(String s) {
		return new ServletRuntimeException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, s);
	}

	public static ServletRuntimeException unavailable(String s) {
		return new ServletRuntimeException(HttpServletResponse.SC_SERVICE_UNAVAILABLE, s);
	}
}
