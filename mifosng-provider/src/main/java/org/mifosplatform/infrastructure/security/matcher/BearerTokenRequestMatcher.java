package org.mifosplatform.infrastructure.security.matcher;

import org.springframework.security.web.util.matcher.RequestMatcher;
import javax.servlet.http.HttpServletRequest;

public class BearerTokenRequestMatcher implements RequestMatcher {
	
	@Override
	public boolean matches(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		System.out.println("BearerTokenRequestMatcher.matches():"+authHeader);
		return null!=authHeader && (authHeader.startsWith("bearer ") || authHeader.startsWith("Bearer "));
	}

}
