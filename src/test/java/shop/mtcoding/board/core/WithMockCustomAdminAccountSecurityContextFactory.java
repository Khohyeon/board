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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import shop.mtcoding.board.module.user.model.UserRepository;

import java.util.List;

public class WithMockCustomAdminAccountSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomAdmin> {

	private UserRepository userRepository;

	@Override
	public SecurityContext createSecurityContext(WithMockCustomAdmin mockCustomAdmin) {
		var adminId = mockCustomAdmin.id(); // 예시: @WithMockCustomUser 어노테이션에서 가져온 사용자 ID
		var admin = userRepository.findByRole(adminId).orElse(null);
		var accountDetails =  new User(
				admin.getUsername(),
				admin.getPassword(),
				List.of(new SimpleGrantedAuthority(admin.getRole()))
		);

		var securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(
				new UsernamePasswordAuthenticationToken(
					accountDetails,
					accountDetails.getPassword(),
					accountDetails.getAuthorities()
				)
		);
		return securityContext;
	}

}
