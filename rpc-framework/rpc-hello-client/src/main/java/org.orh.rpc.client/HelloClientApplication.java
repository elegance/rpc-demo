package main.org.orh.rpc.client;

import org.orh.rpc.client.RpcClient;
import org.orh.rpc.hello.api.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = "org.orh.rpc")
public class HelloClientApplication {

    @Autowired
    private RpcClient rpcClient;

    @PostConstruct
    public void run() {
        HelloService helloService = rpcClient.create(HelloService.class);
        System.out.println(helloService.say("world"));
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloClientApplication.class, args);
    }
}
