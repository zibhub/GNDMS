package de.zib.gndms.infra.monitor;

import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used in servlet code; encapsulates message and integer status code for
 * HttpServletReqeuest.sendError(int, String) + a boolean flag indicating wether
 * the exception should be rethrown by sendToClient().
 * </br>
 * 
 * Typical usage is try
 * <pre>
 *      { ... }Êcatch (ServletRuntimeException e) {Êe.sendToClient(response) }
 * </pre>
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 20.07.2008 Time: 01:08:59
 */
@SuppressWarnings({"ThrowableInstanceNeverThrown"})
public class ServletRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -7914136302943649643L;
	private final int errorCode;
	private final boolean thrownOnServerSide;

	public ServletRuntimeException(int theErrorCode, String message, boolean serverSideEx) {
		super(message);
		errorCode = theErrorCode;
		thrownOnServerSide = serverSideEx;
	}

	public ServletRuntimeException(int theErrorCode, String message, Throwable cause,
	                               boolean serverSideEx) {
		super(message, cause);
		errorCode = theErrorCode;
		thrownOnServerSide = serverSideEx;
	}

	public ServletRuntimeException(int theErrorCode, Throwable cause, boolean serverSideEx)  {
		super(cause);
		errorCode = theErrorCode;
		thrownOnServerSide = serverSideEx;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public boolean isThrownOnServerSide() {
		return thrownOnServerSide;
	}

	/**
	 * Calls response.sendError with parameters filled in from this instance, flushes
	 * the response output stream, and then, optionally rethrows this instance based on the value of
	 * isThrownOnServerSide().
	 *
	 * @param response
	 * @throws IOException
	 */	public void sendToClient(@NotNull final HttpServletResponse response)
		  throws IOException {
		String msg = getMessage();
		if (msg == null)
			response.sendError(errorCode);
		else
			response.sendError(errorCode, msg);
		response.getOutputStream().flush();
		response.flushBuffer();
		if (thrownOnServerSide)
			throw this;
	}

}
