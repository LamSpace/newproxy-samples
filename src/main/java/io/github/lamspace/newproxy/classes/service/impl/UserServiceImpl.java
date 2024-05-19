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

package io.github.lamspace.newproxy.classes.service.impl;

import io.github.lamspace.newproxy.classes.pojo.User;
import io.github.lamspace.newproxy.classes.service.UserService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of {@link UserService}, to simulate the service layer.
 *
 * @author Lam Tong
 * @version 1.0.0
 * @since 1.0.0
 */
public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public int add(User user) {
//        if (logger.isLoggable(Level.INFO)) {
//            logger.log(Level.INFO, "UserServiceImpl add user [" + user + "]");
//        }
        return 1;
    }

    @Override
    public int delete(int id) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "UserServiceImpl delete user with id = [" + id + "]");
        }
        return 1;
    }

    @Override
    public int update(User user) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "UserServiceImpl update user [" + user + "]");
        }
        return 1;
    }

    @Override
    public User get(int id) {
        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO, "UserServiceImpl get user with id = [" + id + "]");
        }
        return new User(id, "Lam Tong", "111111", "lamtong@gmail.com", "China");
    }

}
