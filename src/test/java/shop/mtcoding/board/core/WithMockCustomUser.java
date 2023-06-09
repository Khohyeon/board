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

import org.springframework.security.test.context.support.WithSecurityContext;
import shop.mtcoding.board.common.RoleType;
import shop.mtcoding.board.module.user.status.UserStatus;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomAccountSecurityContextFactory.class)
public @interface WithMockCustomUser {

	int id() default 1;
	String username() default "ssar";
	RoleType role() default RoleType.USER;
	String email() default "ssar@nate.com";
	UserStatus status() default UserStatus.ACTIVE;
}
