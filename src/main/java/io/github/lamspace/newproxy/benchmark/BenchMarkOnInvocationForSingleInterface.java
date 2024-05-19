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

import io.github.lamspace.newproxy.NewProxy;
import io.github.lamspace.newproxy.classes.pojo.User;
import io.github.lamspace.newproxy.classes.service.UserService;
import io.github.lamspace.newproxy.classes.service.impl.UserServiceImpl;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark on invocation time for method inherits from single interface. And results can be listed as below.
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
public class BenchMarkOnInvocationForSingleInterface {

    private static final User user = new User(1, "lam", "123456", "lam@gmail.com", "Hangzhou");

    private UserService userServiceByProxy;

    private UserService userServiceByNewProxyWithMethodHandle;

    private UserService userServiceByNewProxyWithReflection;

    private UserService userServiceByCglib;

    @Setup
    public void setUp() {
        UserService userService = new UserServiceImpl();
        this.userServiceByProxy = (UserService) Proxy.newProxyInstance(
                UserService.class.getClassLoader(),
                new Class[]{UserService.class},
                (proxy, method, args) -> method.invoke(userService, args)
        );
        this.userServiceByNewProxyWithMethodHandle = (UserService) NewProxy.newProxyInstance(
                UserService.class.getClassLoader(),
                (proxy, method, args) -> method.invoke(proxy, userService, args),
                null,
                null,
                UserService.class
        );
        this.userServiceByNewProxyWithReflection = (UserService) NewProxy.newProxyInstance(
                UserService.class.getClassLoader(),
                (proxy, method, args) -> method.getMethod().invoke(userService, args),
                null,
                null,
                UserService.class
        );
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{UserService.class});
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> method.invoke(userService, args));
        this.userServiceByCglib = (UserService) enhancer.create();
    }

    @Benchmark
    public void addOnProxy() {
        this.userServiceByProxy.add(user);
    }

    @Benchmark
    public void addOnNewProxyWithMethodHandle() {
        this.userServiceByNewProxyWithMethodHandle.add(user);
    }

    @Benchmark
    public void addOnNewProxyWithReflection() {
        this.userServiceByNewProxyWithReflection.add(user);
    }

    @Benchmark
    public void addOnCgLib() {
        this.userServiceByCglib.add(user);
    }

    public static void main(String[] args) throws RunnerException {
        String filename = "benchmark" + File.separator + BenchMarkOnInvocationForSingleInterface.class.getSimpleName() + ".txt";
        File file = new File(filename).getParentFile();
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        Options options = new OptionsBuilder()
                .include(BenchMarkOnInvocationForSingleInterface.class.getSimpleName())
                .output(filename)
                .build();
        new Runner(options).run();
    }

}
