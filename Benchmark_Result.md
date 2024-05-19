# Benchmark Result

This is the result of the benchmark, which tests the performance of **NewProxy**, with contrast to **Proxy** and
**CGLIB**.

---

# Environment

| Name   | Value                     |
|--------|---------------------------|
| OS     | Windows 10                |
| CPU    | Intel i5-8300H @ 2.30 GHz |
| Memory | 32GB                      |
| JDK    | 8                         |
| JMH    | 1.37                      |

---

# Benchmark Tests

## Case 1: Time to generate proxy class

See [**io.github.lamspace.newproxy.benchmark.BenchmarkOnGenerationTimeForSingleInterface**][target], and results are
listed as follows:

[target]: ./src/main/java/io/github/lamspace/newproxy/benchmark/BenchmarkOnGenerationTimeForSingleInterface.java

| Benchmark                                                      | Mode | Cnt | Score | Error   | Units |
|----------------------------------------------------------------|------|-----|-------|---------|-------|
| BenchmarkOnGenerationTimeForSingleInterface.generateByCglib    | avgt | 10  | 0.152 | ± 0.001 | us/op |
| BenchmarkOnGenerationTimeForSingleInterface.generateByNewProxy | avgt | 10  | 0.333 | ± 0.008 | us/op |
| BenchmarkOnGenerationTimeForSingleInterface.generateByProxy    | avgt | 10  | 0.052 | ± 0.001 | us/op |

> We can infer from the results that
> <br/><br/>
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
> Proxy > CGLIB > NewProxy
> <br/><br/>
> considering time to generate proxy class.

---

## Case 2: Time to invoke method for interfaces

See [**io.github.lamspace.newproxy.benchmark.BenchMarkOnInvocationForSingleInterface**][target], and results are listed
as follows:

[target]: ./src/main/java/io/github/lamspace/newproxy/benchmark/BenchMarkOnInvocationForSingleInterface.java

| Benchmark                                                             | Mode | Cnt | Score | Error   | Units |
|-----------------------------------------------------------------------|------|-----|-------|---------|-------|
| BenchMarkOnInvocationForSingleInterface.addOnCgLib                    | avgt | 10  | 0.007 | ± 0.001 | us/op |                    
| BenchMarkOnInvocationForSingleInterface.addOnNewProxyWithMethodHandle | avgt | 10  | 0.006 | ± 0.001 | us/op |
| BenchMarkOnInvocationForSingleInterface.addOnNewProxyWithReflection   | avgt | 10  | 0.007 | ± 0.001 | us/op |
| BenchMarkOnInvocationForSingleInterface.addOnProxy                    | avgt | 10  | 0.007 | ± 0.001 | us/op |

> We can inter from the results that
> <br/><br/>
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
> NewProxy(MethodHandle) > Proxy = CGLIB = NewProxy(Reflection)
> <br/><br/>
> considering time to invoke method for interfaces.

---

## Case 3: Time to invoke method for class

See [**io.github.lamspace.newproxy.benchmark.BenchmarkOnInvocationForClass**][target], and results are listed as
follows:

[target]: ./src/main/java/io/github/lamspace/newproxy/benchmark/BenchmarkOnInvocationForClass.java

| Benchmark                                   | Mode | Cnt | Score | Error    | Units |
|---------------------------------------------|------|-----|-------|----------|-------|
| BenchmarkOnInvocationForClass.addOnCglib    | avgt | 10  | 0.007 | ±  0.001 | us/op |
| BenchmarkOnInvocationForClass.addOnNewProxy | avgt | 10  | 0.004 | ±  0.001 | us/op |

> We can infer that
> <br/><br/>
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
> NewProxy > CGLIB
> <br/><br/>
> considering time to invoke method for class.
> <br/><br/>
> Wait, where is Proxy?
> <br/>
> Proxy does not support to generate dynamic proxy class for Class. O(∩_∩)O!!!

---

## Case 4: Size of generated proxy instance

See [**io.github.lamspace.newproxy.benchmark.BenchmarkOfSizeForProxyInstance**][target], and results are listed as
follows:

[target]: ./src/main/java/io/github/lamspace/newproxy/benchmark/BenchmarkOfSizeForProxyInstance.java

| Runtime Proxy Instance Size Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| com.sun.proxy.$Proxy0 object internals:<br/>OFF  SZ                                  TYPE DESCRIPTION               VALUE<br/>  0   8                                       (object header: mark)     0x0000000000000001 (non-biasable; age: 0<br/>  8   4                                       (object header: class)    0xf8012b05<br/> 12   4   java.lang.reflect.InvocationHandler Proxy.h                   (object)<br/>Instance size: 16 bytes<br/>Space losses: 0 bytes internal + 0 bytes external = 0 bytes total                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| io.github.lamspace.newproxy.$NewProxy0 object internals:<br/>OFF  SZ                                                TYPE DESCRIPTION               VALUE<br/>  0   8                                                     (object header: mark)     0x0000000000000001 (non-biasable; age: 0)<br/>  8   4                                                     (object header: class)    0xf8014ab1<br/> 12   4   io.github.lamspace.newproxy.InvocationInterceptor $NewProxy0.interceptor    (object)<br/> 16   4                       java.lang.invoke.MethodHandle $NewProxy0.mhAdd          null<br/> 20   4                       java.lang.invoke.MethodHandle $NewProxy0.mhGet          null<br/> 24   4                       java.lang.invoke.MethodHandle $NewProxy0.mhUpdate       null<br/> 28   4                       java.lang.invoke.MethodHandle $NewProxy0.mhDelete       null<br/>Instance size: 32 bytes<br/>Space losses: 0 bytes internal + 0 bytes external = 0 bytes total    |
| io.github.lamspace.newproxy.classes.service.UserService$$EnhancerByCGLIB$$a62cf7e0 object internals:<br/>OFF  SZ                                   TYPE DESCRIPTION                                               VALUE<br/>  0   8                                        (object header: mark)                                     0x0000000000000001 (non-biasable; age: 0)<br/>  8   4                                        (object header: class)                                    0xf802698e<br/> 12   1                                boolean UserService$$EnhancerByCGLIB$$a62cf7e0.CGLIB$BOUND        true<br/> 13   3                                        (alignment/padding gap)<br/> 16   4   net.sf.cglib.proxy.MethodInterceptor UserService$$EnhancerByCGLIB$$a62cf7e0.CGLIB$CALLBACK_0   (object)<br/> 20   4                                        (object alignment gap)<br/>Instance size: 24 bytes<br/>Space losses: 3 bytes internal + 4 bytes external = 7 bytes total |

| Generated Proxy Class File       | value                                             |
|----------------------------------|---------------------------------------------------|
| Proxy class generate by Proxy    | 2769 bytes                                        |
| Proxy class generate by NewProxy | 5732 bytes                                        |
| Proxy class generate by CGLIB    | 12297 bytes (proxy class + FastClass index class) |

> From the results above, we can infer that
> <br/><br/>
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
> Proxy > NewProxy > CGLIB
> <br/><br/>
> considering size of generated proxy class file, and
> <br/><br/>
> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
> Proxy > CGLIB > NewProxy
> <br/><br/>
> considering size of runtime instance.

---
