package com.cr;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Producer {

    private ZooKeeper zk;
    private String serversNode;
    private CountDownLatch eventNone = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        Producer producer = new Producer();
        producer.buildZKClient(); //生成客户端
        producer.eventNone.await();
        producer.createNode(args); //创建节点
        TimeUnit.SECONDS.sleep(300);
    }

    private void buildZKClient() throws IOException {
        try (InputStream is = Producer.class.getResourceAsStream("/zk.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            zk = new ZooKeeper(properties.getProperty("address"),
                    Integer.parseInt(properties.getProperty("sessionTimeout")),
                    (event) -> {
                        if (event.getType() == Watcher.Event.EventType.None) {
                            System.out.println("zk client is build");
                            eventNone.countDown();
                        }
                    });
            serversNode = properties.getProperty("serversNode");
        }
    }


    private void createNode(String[] args) throws Exception {
        if (args.length == 0) throw new Exception("参数异常");
        String uuid = UUID.randomUUID().toString();
        String path = zk.create(serversNode + "/" + args[0], uuid.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(path + " node is create");
    }

}
