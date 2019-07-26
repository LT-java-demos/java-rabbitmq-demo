import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

public class Send {
    //队列名称
    private final static String QUEUE_NAME = "helloMQ";

    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
        /**
         * 创建连接连接到MabbitMQ
         */
        ConnectionFactory factory = new ConnectionFactory();
        //设置MabbitMQ所在主机ip或者主机名
        factory.setHost("localhost");
        //创建一个连接
        Connection connection = factory.newConnection();
        //创建一个频道
        Channel channel = connection.createChannel();
        // 声明一个队列 -- 在 RabbitMQ 中, 队列的声明是幂等性的
        // 一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同
        // 也就是说, 如果不存在, 就创建, 如果存在, 不会对已经存在的队列产生任何影响
        // 如果并不知道是生产者还是消费者程序中的哪个先运行，在程序中重复将队列重复声明一下是种值得推荐的做法
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //发送的消息
        String message = "hello world!";
        //往队列中发出一条消息
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        //关闭频道和连接
        channel.close();
        connection.close();
    }
}
