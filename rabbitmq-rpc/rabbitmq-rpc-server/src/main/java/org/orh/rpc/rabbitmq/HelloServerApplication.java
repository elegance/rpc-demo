package org.orh.rpc.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HelloServerApplication {

    /**
     * 定义名称为 rpc-queue 的 Queue
     */
    @Bean
    public Queue queue() {
        return new Queue("rpc-queue");
    }

    /**
     * 定义名称为 rpc-exchange 的 DirectExchange
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("rpc-exchange");
    }

    /**
     *  创建了 Queue 与 DirectExchange 之间一个名为 rpc 的 Binding 关系(或称 Routing key)
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("rpc");
    }

    /**
     * 定义了一个 Jackson2JsonMessageConvert ,用于实现消息对象与 JOSN 字符串的序列化反序列化操作
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloServerApplication.class, args);
    }
}
