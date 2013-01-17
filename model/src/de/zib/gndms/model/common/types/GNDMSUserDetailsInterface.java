package de.zib.gndms.model.common.types;

import org.springframework.security.core.userdetails.UserDetails;

public interface GNDMSUserDetailsInterface extends UserDetails {

     String getLocalUser();
     
     void setLocalUser(String localUser);
}
