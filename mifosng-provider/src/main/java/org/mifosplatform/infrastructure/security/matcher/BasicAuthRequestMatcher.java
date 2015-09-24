package org.mifosplatform.infrastructure.security.matcher;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class BasicAuthRequestMatcher implements RequestMatcher{

	@Override
	public boolean matches(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		System.out.println("BasicAuthRequestMatcher.matches():"+authHeader);
		return null!=authHeader && !(authHeader.startsWith("bearer ") || authHeader.startsWith("Bearer "));
	}

}
