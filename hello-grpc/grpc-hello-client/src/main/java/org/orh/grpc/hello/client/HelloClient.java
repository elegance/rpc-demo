package org.orh.grpc.hello.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.orh.grpc.hello.api.HelloRequest;
import org.orh.grpc.hello.api.HelloResponse;
import org.orh.grpc.hello.api.HelloServiceGrpc;

public class HelloClient {
    private static final String host = "localhost";
    private static final int port = 8000;

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext(true)
                .build();

        HelloServiceGrpc.HelloServiceBlockingStub  helloService = HelloServiceGrpc.newBlockingStub(channel);
        HelloRequest request = HelloRequest
                .newBuilder()
                .setName("world")
                .build();
        HelloResponse response = helloService.say(request);
        System.out.println(response.getMessage());

        channel.shutdown();
    }
}
