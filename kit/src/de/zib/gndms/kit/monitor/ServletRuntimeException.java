package de.zib.gndms.kit.monitor;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Used in servlet code; encapsulates message and integer status code for
 * HttpServletReqeuest.sendError(int, String) + a boolean flag indicating wether
 * the exception should be rethrown by sendToClient().
 * </br>
 * 
 * Typical usage is try
 * <pre>
 *      { ... } catch (ServletRuntimeException e) {e.sendToClient(response) }
 * </pre>
 *
 * @author: try ste fan pla nti kow zib
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
	 */	public void sendToClient(final @NotNull HttpServletResponse response)
		  throws IOException {
        StringWriter swriter = new StringWriter();
        PrintWriter pwriter = new PrintWriter(swriter);
        try {
            pwriter.println(getMessage());
            printStackTrace(pwriter);
        }
        finally { pwriter.close(); }
		response.sendError(errorCode, swriter.toString());
		response.getOutputStream().flush();
		response.flushBuffer();
		if (thrownOnServerSide)
			throw this;
	}

}
