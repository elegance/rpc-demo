package org.orh.demo.rpc.hello.server;

import org.orh.demo.rpc.hello.api.HelloService;
import org.orh.rpc.server.RpcService;

@RpcService(HelloService.class)
public class HelloServerImpl implements HelloService {
    @Override
    public String say(String name) {
        return null;
    }
}
