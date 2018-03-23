# 使用RabbitMQ实现RPC

#### RabbitMQ环境搭建(docker)

```bash
docker run -d -p 15672:15672 -p 5672:5672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin --name rabbitmq rabbitmq:3-management
```

web管理界面：http://localhost:15672

#### 原理简介

使用**请求应答模式(Request-Reply)**，两个应用间的消息通信，不再是生产者到消费者的单向流动，而是一个**消息闭环**。消息流动包括以下四个步骤：

1. 请求者发送消息至请求队列中  -- (原来的Producer生产消息)
2. 应答者从队列中取出消息 -- (原来的 Consumer 消费消息)
3. 应答者根据消息应答新的消息至队列
4. 请求者从队列中获取应答的新消息

可见请求者不再是单纯的生产者，应答者也不再是单纯的消费者。请求过程是一次 Producer，Consumer, 应答过程也是一次 Producer, Consumer。

#### RabbitMQ的实现

在RabbitMQ中，一个消息分为三个部分，第一部分叫 “消息头部(Message Headers)”, 第二部分叫 “消息属性(Message Properties)”，第三部分叫 ”消息载荷(Message Payload)“。消息载荷就是要发送的消息内容。消息头和消息属性的结构比较类似，都是 ”键值对“ 结构。我们可以**自定义 “键值对”作为消息头部**， 但**消息属性是RabbitMQ固定的**，有12种：content_type, content_encoding, priority, expiration, message_id, timestamp, type, user_id, app_id, cluster_id 这10种我们从单词基本可以看出它的意义。另外两种：

* **correlation_id**: 用于设置消息的关联ID，可用于请求应答模式。
* **reply_to**: 用于设置消息的应答队列名称，可用于请求应答模式。

请求消息发出后，该消息带有两个属性：**correlation_id** 是消息的关联ID,可通过UUID来生成，保证唯一性；**reply_to** 是应答队列的名称。

应答消息发出后，该消息只带有一个属性：**correlation_id**，它来自请求消息中的 correlation_id。

在RabbitMQ中：

**请求阶段**：“请求者” 发送消息（Request）到 RabbitMQ 特定的 **Exchange**中，随后通过 特定的**RoutingKey** 将消息转发到 具体的Queue中，响应者从 Queue中得到请求消息。

**响应阶段**：响应者处理完消息相关逻辑后，发送消息(Reply) 到**同样的Exchange中**，随后通过**同样的 Binding** 将消息转发到**同样的 Queue中**。

可见请求、响应消息经历了同样的 Exchange 与 Queue，**无需将应对消息放入到另一个Queue中**。

**为什么响应者发送到自己取消息的Queue中不会造成死循环呢？**

// TODO

#### Spring Boot 伪代码实现

```java
// 服务端
@Bean // 返回一个Binding 关系(queue 与 exchange) 到 Spring 容器
return BindingBuilder.bind(rpc-queue).to(rpc-exchange).with(rpc-route-key);

@RabbitListener(queues = rpc-queue) // 监听指定队列的消息
public String receive(String message) { // 不同于往常的消费者，这里有返回值，代表响应
	return "hello" + message;
}

// 客户端
public String send(String message) { // 消息从这里发出，响应由这里返回，形成闭环
	return (String) rabbitTemplate.convertSendAndReceive(rpc-exchange, rpc-route-key, message);
}
```

