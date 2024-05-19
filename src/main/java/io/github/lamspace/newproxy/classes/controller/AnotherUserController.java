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

package io.github.lamspace.newproxy.classes.controller;

import io.github.lamspace.newproxy.classes.pojo.User;
import io.github.lamspace.newproxy.classes.service.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A controller class to demonstrate how to generate dynamic proxy class, with parameterized constructor.
 *
 * @author Lam Tong
 * @version 1.0.0
 * @since 1.0.0
 */
public class AnotherUserController {

    private static final Logger logger = Logger.getLogger(AnotherUserController.class.getName());

    private final UserService userService;

    public AnotherUserController(UserService userService) {
        this.userService = userService;
    }

    public String addUser(User user) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Add user: " + user.toString());
        }
        return this.userService.add(user) > 0 ? "success" : "fail";
    }

    public String deleteUser(int id) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Delete user: " + id);
        }
        return this.userService.delete(id) > 0 ? "success" : "fail";
    }

    public String updateUser(User user) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Update user: " + user.toString());
        }
        return this.userService.update(user) > 0 ? "success" : "fail";
    }

    public User getUser(int id) {
        if (logger.isLoggable(Level.INFO)) {
            logger.info("Get user: " + id);
        }
        return this.userService.get(id);
    }

}
