/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
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

package de.zib.gndms.common.rest;

import java.io.Serializable;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 24.11.11  11:00
 * @brief Another semantic pair ....
 * <p/>
 * As the name suggests represents the token used for myproxy access.
 */
public class MyProxyToken implements Serializable {

    private static final long serialVersionUID = -3476234246022203639L;
    private String login;
    private String password;


    public MyProxyToken( String login ) {

        this.login = login;
    }


    public MyProxyToken( ) {

    }


    public MyProxyToken( String login, String password ) {

        this.login = login;
        this.password = password;
    }


    public String getLogin( ) {

        return login;
    }


    public void setLogin( String login ) {

        this.login = login;
    }


    public String getPassword( ) {

        return password;
    }


    public void setPassword( String password ) {

        this.password = password;
    }
    

    public boolean hasPassword( ) {
        
        return password != null;
    }
    
    
    public boolean isValid( ) {
        
        return login != null && !login.trim().isEmpty();
    }


    // the following method are used to keep list compatibility
    public int size( ) {
        
        int size = 0;
        if( login != null ) ++size;
        if( hasPassword() ) ++size;
        return size;
    }
    
    
    public String get( int idx ) {
        
        if( idx == 0 ) {
            if( login != null ) return login;
            if( hasPassword() ) return password;
        } else if( idx == 1 && hasPassword() )
            return password;
        
        throw new IndexOutOfBoundsException( String.valueOf(idx) );
    }
    

    @Override
    public boolean equals( Object o ) {

        if ( this == o ) return true;
        if ( !(o instanceof MyProxyToken) ) return false;

        MyProxyToken that = ( MyProxyToken ) o;

        return     !( login != null ? !login.equals( that.login ) : that.login != null )
                && !( password != null ? !password.equals( that.password ) : that.password != null );

    }


    @Override
    public int hashCode( ) {

        int result = login.hashCode( );
        result = 31 * result + (password != null ? password.hashCode( ) : 0);
        return result;
    }
}

