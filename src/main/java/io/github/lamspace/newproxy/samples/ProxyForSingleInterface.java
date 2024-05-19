/*
 * Copyright 2024 the original author, Lam Tong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lamspace.newproxy.samples;

import io.github.lamspace.newproxy.InvocationInterceptor;
import io.github.lamspace.newproxy.NewProxy;
import io.github.lamspace.newproxy.classes.pojo.Auditable;
import io.github.lamspace.newproxy.classes.pojo.User;
import io.github.lamspace.newproxy.classes.service.UserService;
import io.github.lamspace.newproxy.classes.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demonstration of generating dynamic proxy class for a single interface with corresponding implementation.
 * In this sample, the proxy class is generated for the interface {@code UserService} and its implementation
 * {@code UserServiceImpl}. And the invocation interceptor is used to intercept the invocation of the method,
 * adding audit information to the {@code User} object.
 *
 * @author Lam Tong
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings(value = {"all"})
public class ProxyForSingleInterface {

    private static final Logger logger = Logger.getLogger(ProxyForSingleInterface.class.getName());

    public static void main(String[] args) {
        User user = new User(1, "lamspace", "123456", "lamspace@gmail.com", "China");
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        InvocationInterceptor interceptor = (proxy, method, args1) -> {
            String methodName = method.getMethod().getName();
            if (methodName.equals("add") && args1 != null) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("Add method invoked with parameter " + Arrays.toString(args1));
                }
                for (int i = 0; i < args1.length; i++) {
                    if (args1[i] instanceof Auditable) {
                        Auditable auditable = (Auditable) args1[i];
                        auditable.setCreatedBy("add-method");
                        auditable.setCreateAt(LocalDateTime.now());
                        auditable.setUpdateBy("add-method");
                        auditable.setUpdateAt(LocalDateTime.now());
                        args1[i] = auditable;
                    }
                }
            }
            if (methodName.equals("update") && args1 != null) {
                if (logger.isLoggable(Level.INFO)) {
                    logger.info("Update method invoked with parameter " + Arrays.toString(args1));
                }
                for (int i = 0; i < args1.length; i++) {
                    if (args1[i] instanceof Auditable) {
                        Auditable auditable = (Auditable) args1[i];
                        auditable.setUpdateBy("update-method");
                        auditable.setUpdateAt(LocalDateTime.now());
                        args1[i] = auditable;
                    }
                }
            }
            return method.invoke(proxy, userServiceImpl, args1);
        };
        UserService userService = (UserService) NewProxy.newProxyInstance(UserService.class.getClassLoader(),
                interceptor,
                null,
                null,
                UserService.class);

        userService.add(user);
        userService.delete(1);
        userService.update(user);
        userService.get(1);
    }

}
