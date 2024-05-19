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
import io.github.lamspace.newproxy.classes.service.UserService;
import io.github.lamspace.newproxy.classes.service.impl.UserServiceImpl;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.openjdk.jol.info.ClassLayout;

import java.io.File;
import java.lang.reflect.Proxy;

/**
 * Benchmark on the size of proxy instance.
 *
 * @author Lam Tong
 * @version 1.0.0
 * @since 1.0.0
 */
public class BenchmarkOfSizeForProxyInstance {

    private static final UserService userServiceImpl = new UserServiceImpl();

    public static void main(String[] args) {
//        // This block will dump two proxy classes, generated by Proxy, which may cause confusion when comparing
//        // sizes of proxy class as below.
//        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
//        TestSample testSample = new TestSample();
//        ClassLayout classLayout = ClassLayout.parseInstance(testSample);
//        System.out.println(classLayout.toPrintable());

        UserService userService;
        ClassLayout classLayout;

        // dump to directory "com"
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        userService = (UserService) Proxy.newProxyInstance(
                UserService.class.getClassLoader(),
                new Class[]{UserService.class},
                (proxy, method, args1) -> method.invoke(userServiceImpl, args1)
        );
        classLayout = ClassLayout.parseInstance(userService);
        System.out.println(classLayout.toPrintable());
        System.clearProperty("sun.misc.ProxyGenerator.saveGeneratedFiles");

        // dump to directory "newproxy"
        System.setProperty(Constants.STRING_DUMP_FLAG, "true");
        userService = (UserService) NewProxy.newProxyInstance(
                UserService.class.getClassLoader(),
                (proxy, method, args1) -> method.invoke(proxy, userServiceImpl, args1),
                null,
                null,
                UserService.class
        );
        classLayout = ClassLayout.parseInstance(userService);
        System.out.println(classLayout.toPrintable());

        // dump to directory "cglib"
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "cglib");
        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{UserService.class});
        enhancer.setCallback((MethodInterceptor) (proxy, method, args1, proxyMethod) -> method.invoke(userServiceImpl, args1));
        userService = (UserService) enhancer.create();
        classLayout = ClassLayout.parseInstance(userService);
        System.out.println(classLayout.toPrintable());

        readGeneratedFileSizes();
    }

    // Test on Windows.
    private static void readGeneratedFileSizes() {
        File file = new File("com/sun/proxy/$Proxy0.class");
        if (!file.exists()) {
            throw new RuntimeException("file not exists, " + file.getName());
        }
        if (file.isFile()) {
            long length = file.length();
            System.out.println("Proxy class generate by Proxy\t\tsize = " + length + " bytes");
        }
        file = new File("newproxy/$NewProxy0.class");
        if (!file.exists()) {
            throw new RuntimeException("file not exists, " + file.getName());
        }
        if (file.isFile()) {
            long length = file.length();
            System.out.println("Proxy class generate by NewProxy\tsize = " + length + " bytes");
        }
        file = new File("cglib/io/github/lamspace/newproxy/classes/service/UserService$$EnhancerByCGLIB$$a62cf7e0.class");
        File file1 = new File("cglib/net/sf/cglib/core/MethodWrapper$MethodWrapperKey$$KeyFactoryByCGLIB$$d45e49f7.class");
        File file2 = new File("cglib/net/sf/cglib/proxy/Enhancer$EnhancerKey$$KeyFactoryByCGLIB$$7fb24d72.class");
        if (!file.exists()) {
            throw new RuntimeException("file not exists, " + file.getName());
        }
        if (!file1.exists()) {
            throw new RuntimeException("file not exists, " + file1.getName());
        }
        if (!file2.exists()) {
            throw new RuntimeException("file not exists, " + file2.getName());
        }
        long length = 0;
        if (file.isFile()) {
            length += file.length();
        }
        if (file1.isFile()) {
            length += file1.length();
        }
        if (file2.isFile()) {
            length += file2.length();
            System.out.println("Proxy class generate by CGLIB\t\tsize = " + length + " bytes (proxy class + FastClass index class)");
        }
    }

}
