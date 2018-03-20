package org.orh.grpc.hello.client;

import io.grpc.ManagedChannel;
import org.orh.grpc.client.GrpcClient;
import org.orh.grpc.hello.api.HelloRequest;
import org.orh.grpc.hello.api.HelloResponse;
import org.orh.grpc.hello.api.HelloServiceGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.orh.grpc")
public class HelloClientApplication implements CommandLineRunner {

    @Autowired
    private GrpcClient grpcClient;

    @Override
    public void run(String... strings) throws Exception {
        ManagedChannel channel = grpcClient.buildChannel();

        try {
            HelloServiceGrpc.HelloServiceBlockingStub helloService = HelloServiceGrpc.newBlockingStub(channel);
            HelloRequest request = HelloRequest
                    .newBuilder()
                    .setName("world")
                    .build();
            HelloResponse response = helloService.say(request);
            System.out.println(response.getMessage());
        } finally {
            channel.shutdown();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloClientApplication.class, args).close();
    }
}
