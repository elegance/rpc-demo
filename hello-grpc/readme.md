## 初体验gRPC

#### 第一步：定义RPC接口

项目名：grpc-hello-api, maven依赖：

```xml
<dependency>
     <groupId>io.grpc</groupId>
     <artifactId>grpc-all</artifactId>
     <version>1.10.0</version>
</dependency>
```

grpc-all 依赖包含了gRPC的全部依赖，简单情况下我们可以直接使用这个依赖，特定情况下可以针对性的选择依赖，如grpc-netty、grpc-protobuf、grpc-stub.

此外，我们需要添加 Maven扩展与插件，因为后面要直接使用 Maven 来执行 `Protocol Buffers` 命令，从而生成 Stub 代码库:

```xml
 <build>
        <extensions>
            <!--os-maven-plugin 扩展用于生成各种与平台无关的属性-->
            <extension>
                <groupId>kr.motd.maven</groupId>
                <artifactId>os-maven-plugin</artifactId>
                <version>1.5.0.Final</version>
            </extension>
        </extensions>
        <plugins>
           <!-- protobuf-maven-plugin 用于执行 Protocol Buffers 命令生成 Stub 代码块
            一下配置表示：执行 mvn compile 命令时，将生成 Stub代码-->
            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.5.0</version>
                <configuration>
                    <pluginId>grpc-java</pluginId>
                    <protocArtifact>
                        com.google.protobuf:protoc:3.1.0:exe:${os.detected.classifier}
                    </protocArtifact>
                    <pluginArtifact>
                        io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}
                    </pluginArtifact>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

在 src/main 创建`proto`,创建文件 `hello.proto`：

```protobuf
// 定义语法版本
syntax = "proto3";

// 定义 Stub 代码选项
option java_package = "org.orh.grpc.hello.api";
option java_outer_classname = "HelloProto";
option java_multiple_files = true;

// 定义包名
package org.orh.grpc.hello.api;

// 定义服务
service HelloService {
    // 定义方法
    rpc Say (HelloRequest) returns (HelloResponse) {};
}


// 定义消息（请求）
message HelloRequest {
    string name = 1;
}

// 定义消息（响应）
message HelloResponse {
    string message = 1;
}
```

执行 `mvn compile` 观察生成的 Stub 代码。