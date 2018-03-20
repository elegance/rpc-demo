package org.orh.grpc.hello.server;

import io.grpc.stub.StreamObserver;
import org.orh.grpc.hello.api.HelloRequest;
import org.orh.grpc.hello.api.HelloResponse;
import org.orh.grpc.hello.api.HelloServiceGrpc;
import org.orh.grpc.server.annotation.GrpcService;

@GrpcService
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    @Override
    public void say(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        HelloResponse response = null;
        try {
            response = HelloResponse.newBuilder()
                    .setMessage("Hello " + request.getName())
                    .build();
        } catch (Exception e) {
            responseObserver.onError(e);
        } finally {
            responseObserver.onNext(response);
        }
        responseObserver.onCompleted();
    }
}
