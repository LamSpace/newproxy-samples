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
import io.github.lamspace.newproxy.classes.extra.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demonstration of generating dynamic proxy class for class and multiple interfaces, with calling
 * a parameterized constructor of superclass with one parameter.
 *
 * @author Lam Tong
 * @version 1.0.0
 * @since 1.0.0
 */
@SuppressWarnings(value = {"all"})
public class ProxyForClassWithMultipleInterfacesAndOneParameter {

    private static final Logger logger = Logger.getLogger(ProxyForClassWithMultipleInterfacesAndOneParameter.class.getName());

    public static void main(String[] args) {
        FooService fooService = new FooServiceImpl();
        BarService barService = new BarServiceImpl();
        InvocationInterceptor interceptor = (proxy, method, args1) -> {
            Class<?> declaringClass = method.getMethod().getDeclaringClass();
            if (declaringClass.equals(FooService.class)) {
                return method.invoke(proxy, fooService, args);
            }
            if (declaringClass.equals(BarService.class)) {
                return method.invoke(proxy, barService, args);
            }
            return method.invoke(proxy, null, args);
        };
        Object o = NewProxy.newProxyInstance(Something.class.getClassLoader(),
                interceptor,
                new Class<?>[]{String.class},
                new Object[]{"Lam"},
                Something.class, FooService.class, BarService.class);

        if (o instanceof FooService) {
            FooService fooService1 = (FooService) o;
            fooService1.foo();
        }

        if (o instanceof BarService) {
            BarService barService1 = (BarService) o;
            barService1.bar();
        }

        if (o instanceof Something) {
            Something something = (Something) o;
            String res = something.repeat();
            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, res);
            }
        }
    }

}
