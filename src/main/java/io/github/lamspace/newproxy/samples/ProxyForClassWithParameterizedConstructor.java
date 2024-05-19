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
import io.github.lamspace.newproxy.classes.controller.AnotherUserController;
import io.github.lamspace.newproxy.classes.pojo.Auditable;
import io.github.lamspace.newproxy.classes.pojo.User;
import io.github.lamspace.newproxy.classes.service.UserService;
import io.github.lamspace.newproxy.classes.service.impl.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demonstrations of generating dynamic proxy class for class with parameterized constructor. In this sample,
 * {@link AnotherUserController} is the target class, which has a public constructor
 * {@link AnotherUserController#AnotherUserController(UserService)}, requiring an instance of type {@link UserService}.
 * The third parameter of {@link NewProxy#newProxyInstance(ClassLoader, InvocationInterceptor, Class[], Object[], Class[])
 * newProxyInstance} represents the type of the constructor parameters, and the forth parameter of that means
 * value passed to the constructor.
 *
 * @author Lam Tong
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings(value = {"all"})
public class ProxyForClassWithParameterizedConstructor {

    private static final Logger logger = Logger.getLogger(ProxyForClassWithParameterizedConstructor.class.getName());

    public static void main(String[] args) {
        User user = new User(1, "Lam", "123456", "lam@gmail.com", "Hanoi");
        InvocationInterceptor interceptor = (proxy, method, args1) -> {
            String methodName = method.getMethod().getName();
            if (methodName.contains("add") && args1 != null) {
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
            if (methodName.contains("update") && args1 != null) {
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
            return method.invoke(proxy, null, args1);
        };
        AnotherUserController userController = (AnotherUserController) NewProxy.newProxyInstance(AnotherUserController.class.getClassLoader(),
                interceptor,
                new Class[]{UserService.class},
                new Object[]{new UserServiceImpl()},
                AnotherUserController.class);
        String res = userController.addUser(user);
        logger.info(res);
        res = userController.deleteUser(1);
        logger.info(res);
        res = userController.updateUser(user);
        logger.info(res);
        User u = userController.getUser(1);
        logger.info(u.toString());
    }

}
