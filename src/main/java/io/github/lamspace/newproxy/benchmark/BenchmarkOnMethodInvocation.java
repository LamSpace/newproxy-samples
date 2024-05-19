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

import io.github.lamspace.newproxy.classes.pojo.User;
import io.github.lamspace.newproxy.classes.service.UserService;
import io.github.lamspace.newproxy.classes.service.impl.UserServiceImpl;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Demonstration of benchmarking on method invocation, between {@code Reflection} and {@code Dynamic Language Support}.
 * Generally, {@code Dynamic Language Support} is faster than {@code Reflection}. This is why {@code NewProxy} use
 * {@link MethodHandle} to invoke method for interfaces' method invocation.
 *
 * @author Lam Tong
 * @version 1.0.0
 * @since 1.0.0
 */
@Fork(value = 1)
@Measurement(iterations = 10)
@Warmup(iterations = 10)
@BenchmarkMode(value = Mode.AverageTime)
@OutputTimeUnit(value = TimeUnit.MICROSECONDS)
@State(value = Scope.Thread)
public class BenchmarkOnMethodInvocation {

    private final User user = new User(1, "lamspace", "123456", "lamspace@gmail.com", "China");

    private UserService userService;

    private Method method;

    private MethodHandle methodHandle;

    @Setup
    public void setUp() throws Throwable {
        this.userService = new UserServiceImpl();
        this.method = Class.forName(UserService.class.getName()).getMethod("add", User.class);
        this.methodHandle = MethodHandles.lookup().findVirtual(UserService.class, "add", MethodType.methodType(int.class, User.class)).bindTo(this.userService);
    }

    @Benchmark
    public void addByReflection() throws Throwable {
        int res = (int) this.method.invoke(this.userService, this.user);
    }

    @Benchmark
    public void addByMethodHandle() throws Throwable {
        int res = (int) this.methodHandle.invokeExact(this.user);
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(BenchmarkOnMethodInvocation.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

}
