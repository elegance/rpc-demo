package org.orh.rpc.hello.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.orh.rpc")
public class HelloServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloServerApplication.class, args);
    }
}
