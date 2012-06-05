package de.zib.gndms.gndms.security;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 30.05.12  16:28
 * @brief
 */
public class LocalRequestSkippingChannelProcessingFilter extends ChannelProcessingFilter {


    protected Logger logger = LoggerFactory.getLogger( this.getClass() );

    @Override
    public void doFilter( final ServletRequest req, final ServletResponse res,
                          final FilterChain chain )
            throws IOException, ServletException
    {


        final String remoteAddr = req.getRemoteAddr();
        logger.info( "request from: " + remoteAddr );
        if( remoteAddr.equals( req.getLocalAddr() ) )
            chain.doFilter( req, res );
        else
            super.doFilter( req, res, chain );    // overriden method implementation
    }
}
