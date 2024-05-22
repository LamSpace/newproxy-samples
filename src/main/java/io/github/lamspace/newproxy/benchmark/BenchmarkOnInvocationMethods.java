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

import io.github.lamspace.newproxy.Constants;
import io.github.lamspace.newproxy.NewProxy;
import io.github.lamspace.newproxy.classes.TestSample;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark for a single class to invoke all methods. Result shows that {@code NewProxy} is better than {@code CGLIB}
 * with the same method invocation logic.
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
public class BenchmarkOnInvocationMethods {

    private static final ThreadLocalRandom rand = ThreadLocalRandom.current();

    private TestSample testSampleByNewProxy;

    private TestSample testSampleByCGLIB;

    @Setup
    public void setup() {
        testSampleByNewProxy = (TestSample) NewProxy.newProxyInstance(
                TestSample.class.getClassLoader(),
                (proxy, method, args) -> method.invoke(proxy, null, args),
                null,
                null,
                TestSample.class
        );
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(TestSample.class);
        enhancer.setCallback((MethodInterceptor) (proxy, method, args, methodProxy) -> methodProxy.invokeSuper(proxy, args));
        testSampleByCGLIB = (TestSample) enhancer.create();
    }

    @SuppressWarnings("DuplicatedCode")
    @Benchmark
    public void invocationByNewProxy() {
        int nextInt = rand.nextInt(20) + 1;
        switch (nextInt) {
            case 1:
                testSampleByNewProxy.add1(1, 2);
                break;
            case 2:
                testSampleByNewProxy.add2(1, 2);
                break;
            case 3:
                testSampleByNewProxy.add3(1, 2);
                break;
            case 4:
                testSampleByNewProxy.add4(1, 2);
                break;
            case 5:
                testSampleByNewProxy.add5(1, 2);
                break;
            case 6:
                testSampleByNewProxy.add6(1, 2);
                break;
            case 7:
                testSampleByNewProxy.add7(1, 2);
                break;
            case 8:
                testSampleByNewProxy.add8(1, 2);
                break;
            case 9:
                testSampleByNewProxy.add9(1, 2);
                break;
            case 10:
                testSampleByNewProxy.add10(1, 2);
                break;
            case 11:
                testSampleByNewProxy.add11(1, 2);
                break;
            case 12:
                testSampleByNewProxy.add12(1, 2);
                break;
            case 13:
                testSampleByNewProxy.add13(1, 2);
                break;
            case 14:
                testSampleByNewProxy.add14(1, 2);
                break;
            case 15:
                testSampleByNewProxy.add15(1, 2);
                break;
            case 16:
                testSampleByNewProxy.add16(1, 2);
                break;
            case 17:
                testSampleByNewProxy.add17(1, 2);
                break;
            case 18:
                testSampleByNewProxy.add18(1, 2);
                break;
            case 19:
                testSampleByNewProxy.add19(1, 2);
                break;
            case 20:
                testSampleByNewProxy.add20(1, 2);
                break;
            default:
                throw new IllegalArgumentException("illegal argument nextInt = " + nextInt);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Benchmark
    public void invocationByCGLIB() {
        int nextInt = rand.nextInt(20) + 1;
        switch (nextInt) {
            case 1:
                testSampleByCGLIB.add1(1, 2);
                break;
            case 2:
                testSampleByCGLIB.add2(1, 2);
                break;
            case 3:
                testSampleByCGLIB.add3(1, 2);
                break;
            case 4:
                testSampleByCGLIB.add4(1, 2);
                break;
            case 5:
                testSampleByCGLIB.add5(1, 2);
                break;
            case 6:
                testSampleByCGLIB.add6(1, 2);
                break;
            case 7:
                testSampleByCGLIB.add7(1, 2);
                break;
            case 8:
                testSampleByCGLIB.add8(1, 2);
                break;
            case 9:
                testSampleByCGLIB.add9(1, 2);
                break;
            case 10:
                testSampleByCGLIB.add10(1, 2);
                break;
            case 11:
                testSampleByCGLIB.add11(1, 2);
                break;
            case 12:
                testSampleByCGLIB.add12(1, 2);
                break;
            case 13:
                testSampleByCGLIB.add13(1, 2);
                break;
            case 14:
                testSampleByCGLIB.add14(1, 2);
                break;
            case 15:
                testSampleByCGLIB.add15(1, 2);
                break;
            case 16:
                testSampleByCGLIB.add16(1, 2);
                break;
            case 17:
                testSampleByCGLIB.add17(1, 2);
                break;
            case 18:
                testSampleByCGLIB.add18(1, 2);
                break;
            case 19:
                testSampleByCGLIB.add19(1, 2);
                break;
            case 20:
                testSampleByCGLIB.add20(1, 2);
                break;
            default:
                throw new IllegalArgumentException("illegal argument nextInt = " + nextInt);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(BenchmarkOnInvocationMethods.class.getSimpleName())
                .build();
        new Runner(options).run();
    }

}
