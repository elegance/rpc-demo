package org.orh.rpc.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HelloServer {

    /**
     * 带有返回值的 Listener
     */
    @RabbitListener(queues = "rpc-queue")
    public String receive(String message) {
        return "hello" + message;
    }
}
