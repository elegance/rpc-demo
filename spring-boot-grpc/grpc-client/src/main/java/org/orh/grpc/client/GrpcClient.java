package org.orh.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GrpcClient {

    @Value("${grpc.host}")
    private String host;

    @Value("${grpc.port}")
    private int port;

    public ManagedChannel buildChannel() {
        return ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext(true)
                .build();
    }
}
