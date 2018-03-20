package org.orh.rpc.hello.server;

import org.orh.rpc.hello.api.HelloService;
import org.orh.rpc.server.RpcService;

@RpcService(HelloService.class)
public class HelloServerImpl implements HelloService {
    @Override
    public String say(String name) {
        return null;
    }
}
