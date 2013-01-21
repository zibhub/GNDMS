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

import org.springframework.security.core.GrantedAuthority;

import de.zib.gndms.model.common.types.GNDMSUserDetailsInterface;

import java.util.Collection;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 29.02.12  17:36
 * @brief
 */
public class GNDMSUserDetails implements GNDMSUserDetailsInterface {

    private static final long serialVersionUID = 774209650763623369L;

    private Collection<? extends GrantedAuthority> authorities;
    private String dn;
    private boolean isUser;
    private String localUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }


    @Override
    public String getPassword() {

        return "N/A";
    }


    @Override
    public String getUsername() {

        return dn;
    }

    @Override
    public String getLocalUser() {
        return localUser;
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }


    @Override
    public boolean isAccountNonLocked() {

        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }


    @Override
    public boolean isEnabled() {

        return true;
    }

    public void setDn( final String dn ) {

        this.dn = dn;
    }

    @Override
    public void setLocalUser( final String localUser ) {

        this.localUser = localUser;
    }

    public void setAuthorities( final Collection<? extends GrantedAuthority> authorities ) {

        this.authorities = authorities;
    }


    public void setIsUser( final boolean isUser ) {

        this.isUser = isUser;
    }


    public boolean isUser() {

        return isUser;
    }
}
