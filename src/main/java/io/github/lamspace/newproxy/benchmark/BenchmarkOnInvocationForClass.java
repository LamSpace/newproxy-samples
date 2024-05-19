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
import io.github.lamspace.newproxy.classes.controller.UserController;
import io.github.lamspace.newproxy.classes.pojo.User;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Fork(value = 1)
@Measurement(iterations = 10)
@Warmup(iterations = 10)
@BenchmarkMode(value = Mode.AverageTime)
@OutputTimeUnit(value = TimeUnit.MICROSECONDS)
@State(value = Scope.Thread)
public class BenchmarkOnInvocationForClass {

    private static final User user = new User(1, "lamspace", "123456", "lamspace@gmail.com", "China");

    private UserController userControllerByNewProxy;

    private UserController userControllerByCglib;

    @Setup
    public void setUp() {
        this.userControllerByNewProxy = (UserController) NewProxy.newProxyInstance(
                UserController.class.getClassLoader(),
                (proxy, method, args) -> method.invoke(proxy, null, args),
                null,
                null,
                UserController.class
        );
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserController.class);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> proxy.invokeSuper(obj, args));
        this.userControllerByCglib = (UserController) enhancer.create();
    }

    @Benchmark
    public void addOnNewProxy() {
        this.userControllerByNewProxy.addUser(user);
    }

    @Benchmark
    public void addOnCglib() {
        this.userControllerByCglib.addUser(user);
    }

    public static void main(String[] args) throws RunnerException {
        String filename = "benchmark" + File.separator + BenchmarkOnInvocationForClass.class.getSimpleName() + ".txt";
        File file = new File(filename).getParentFile();
        if (!file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        Options options = new OptionsBuilder()
                .include(BenchmarkOnInvocationForClass.class.getSimpleName())
                .output(filename)
                .build();
        new Runner(options).run();
    }

}
