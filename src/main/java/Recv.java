import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {
    // 队列名称
    private final static String QUEUE_NAME = "helloMQ";

    public static void main(String[] argv) throws Exception {

        // 打开连接和创建频道，与发送端一样
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明要关注的队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        //创建消费者
        // DefaultConsumer类 实现了 Consumer 接口, 通过传入一个频道, 告诉服务器我们需要哪个频道的消息
        // 如果频道中有消息, 就会执行回调函数 handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        // 自动回复队列应答 -- RabbitMQ 中的消息确认机制,
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
