# NewProxy Samples

在本项目中, 对生成代理类的时间、调用方法的时间和生成的代理类文件大小以及运行时代理实例大小进行了对比.
所有结果均记录在[Benchmark_Results.md](./Benchmark_Result.md).

根据以上结果, 可大致推断如下:

* 就生成动态代理类时间而言, `Proxy` 生成代理类时间最短, 其次是 `CGLIB`, 最后是 `NewProxy`.(看来还的改进改进)

![](https://gitee.com/LamTong/markdown-images/raw/master/proxyClassGenerationTime_CN.jpg)

* 就接口方法调用时间而言, `Proxy`, `NewProxy` 和 `CGLIB` 的时间基本上都差不多, 只是 `NewProxy` 通过使用 MethodHandle
  的方式来调用效果稍微好一点.

![](https://gitee.com/LamTong/markdown-images/raw/master/methodInvocationTimeForInterfaces_CN.jpg)

* 就类方法调用时间而言, `NewProxy` 较 `CGLIB` 更好一些.(Proxy无法实现该功能)

![](https://gitee.com/LamTong/markdown-images/raw/master/methodInvocationTimeForClass_CN.jpg)

* 就生成的动态代理类文件大小而言, `Proxy` 生成的代理类文件最小, `NewProxy` 其次, 最后是 `CGLIB`.

![](https://gitee.com/LamTong/markdown-images/raw/master/sizeOfGeneratedProxyClassFile_CN.jpg)

* 就运行时代理实例的对象大小而言, `Proxy` 生成的代理实例对象最小, 其次是 `CGLIB`, 最后是 `NewProxy`.

![](https://gitee.com/LamTong/markdown-images/raw/master/sizeOfRuntimeProxyInstance_CN.jpg)

就动态代理实现复杂度而言, `Proxy` 生成的动态代理类复杂度最低, `NewProxy` 其次, `CGLIB` 最复杂.(作者在编写`NewProxy`
的时候参考了不少`Proxy`的代码实现, 且 [BenchmarkOfSizeForProxyInstance.java][target] 演示了如何导出生成的代理类)

[target]: ./src/main/java/io/github/lamspace/newproxy/benchmark/BenchmarkOfSizeForProxyInstance.java

---
