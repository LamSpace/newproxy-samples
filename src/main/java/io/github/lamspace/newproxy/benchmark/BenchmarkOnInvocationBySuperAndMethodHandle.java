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

package io.github.lamspace.newproxy.benchmark;

import io.github.lamspace.newproxy.classes.controller.UserController;
import io.github.lamspace.newproxy.classes.pojo.User;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark on method invocation. Considering implementation to invoke method of specified class
 * with MethodHandle and super directly.<br/>
 * Obviously, invoking superclass method by super is faster.
 */
@Fork(value = 1)
@Measurement(iterations = 10)
@Warmup(iterations = 10)
@BenchmarkMode(value = Mode.AverageTime)
@OutputTimeUnit(value = TimeUnit.MICROSECONDS)
@State(value = Scope.Thread)
public class BenchmarkOnInvocationBySuperAndMethodHandle extends UserController {

    private static MethodHandle methodHandle;

    private static final Object user = new User(1, "lamspace", "123456", "lamspace@gmail.com", "China");

    @Setup
    public void setUp() throws NoSuchMethodException, IllegalAccessException {
        methodHandle = MethodHandles.lookup().findVirtual(UserController.class, "addUser", MethodType.methodType(String.class, User.class)).bindTo(new UserController());
    }

    @Benchmark
    public void invokeByMethodHandle() throws Throwable {
        String res = (String) methodHandle.invokeExact(User.class.cast(user));
    }

    @Benchmark
    public void invokeBySuper() {
        String res = super.addUser(((User) user));
    }

    public static void main(String[] args) throws Throwable {
        Options options = new OptionsBuilder()
                .include(BenchmarkOnInvocationBySuperAndMethodHandle.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

}
