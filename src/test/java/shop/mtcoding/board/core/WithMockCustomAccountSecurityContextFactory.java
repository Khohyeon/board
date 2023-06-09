/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package shop.mtcoding.board.core;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import shop.mtcoding.board.auth.MyUserDetails;
import shop.mtcoding.board.module.user.model.User;
import shop.mtcoding.board.module.user.status.UserStatus;

public class WithMockCustomAccountSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser mockCustomUser) {
		User user = User.builder()
				.id(mockCustomUser.id())
				.username(mockCustomUser.username())
				.password("1234")
				.email(mockCustomUser.email())
				.role(mockCustomUser.role())
				.status(mockCustomUser.status())
				.build();
		var MyUserDetails = new MyUserDetails(user);

		var securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(
				new UsernamePasswordAuthenticationToken(
						MyUserDetails,
						MyUserDetails.getPassword(),
						MyUserDetails.getAuthorities()
				)
		);

		return securityContext;
	}
}