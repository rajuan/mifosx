package org.mifosplatform.infrastructure.security.service;

import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;

public class ProviderDaoAuthenticationProvider extends
		DaoAuthenticationProvider {
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) throws BadCredentialsException {
		super.additionalAuthenticationChecks(userDetails, authentication);
		
		if(((AppUser)userDetails).isSelfServiceUser()){
			throw new BadCredentialsException("User not authorised for the requested resource");
		}
	}

}
