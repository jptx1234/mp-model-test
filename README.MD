# MyBatis Plus 的 Model 类测试

基于JMH框架，测试[MyBatis Plus](https://github.com/baomidou/mybatis-plus)的`com.baomidou.mybatisplus.extension.activerecord.Model`类的成员变量中，有log对象和无log对象的性能差异。

环境要求java 11，如果要在Java其他版本下运行，请修改pom.xml中的java.version。



测试方法：

```sh
mvn package
java -jar target/mp-model-test.jar
```



关联issue：https://github.com/baomidou/mybatis-plus/issues/3262

