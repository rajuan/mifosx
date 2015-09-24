package org.mifosplatform.infrastructure.security.service;

import org.mifosplatform.infrastructure.core.service.ThreadLocalContextUtil;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;

public class MifosDaoAuthenticationProvider extends
		DaoAuthenticationProvider {
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication) throws BadCredentialsException {
		super.additionalAuthenticationChecks(userDetails, authentication);

		if(!ThreadLocalContextUtil.isAuthRequest() 
				&& (ThreadLocalContextUtil.isSelfServiceRequest() ^ ((AppUser)userDetails).isSelfServiceUser())){
			System.out
					.println("MifosDaoAuthenticationProvider.additionalAuthenticationChecks():not valid user");
			throw new BadCredentialsException("User not authorised to use the requested resource.");
		}
	}

}
