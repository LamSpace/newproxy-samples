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

import io.github.lamspace.newproxy.InvocationInterceptor;
import io.github.lamspace.newproxy.NewProxy;
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
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark on generation time for single interface. And results can be listed as follows.
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
public class BenchmarkOnGenerationTimeForSingleInterface {

    private static final Class<?> CLASS = UserService.class;

    private static final UserService userService = new UserServiceImpl();

    private static final InvocationHandler handler = (proxy, method, args) -> method.invoke(userService, args);

    private static final InvocationInterceptor interceptor = (proxy, method, args) -> method.invoke(proxy, userService, args);

    private static final MethodInterceptor methodInterceptor = (proxy, method, args, methodProxy) -> method.invoke(userService, args);

    @Benchmark
    public void generateByProxy() {
        UserService service = (UserService) Proxy.newProxyInstance(CLASS.getClassLoader(),
                new Class[]{CLASS},
                handler);
    }

    @Benchmark
    public void generateByNewProxy() {
        UserService service = (UserService) NewProxy.newProxyInstance(CLASS.getClassLoader(),
                interceptor,
                null,
                null,
                CLASS);
    }

    @Benchmark
    public void generateByCglib() {
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{CLASS});
        enhancer.setCallback(methodInterceptor);
        UserService service = (UserService) enhancer.create();
    }

    public static void main(String[] args) throws RunnerException, IOException {
        String filename = "benchmark" + File.separator + BenchmarkOnGenerationTimeForSingleInterface.class.getSimpleName() + ".txt";
        File file = new File(filename).getParentFile();
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        Options options = new OptionsBuilder()
                .include(BenchmarkOnGenerationTimeForSingleInterface.class.getSimpleName())
                .output(filename)
                .build();
        new Runner(options).run();
    }

}
