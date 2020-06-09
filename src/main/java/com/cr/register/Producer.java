package com.cr.register;

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
    private String path;
    private CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        Producer producer = new Producer();

        //构建生产者客户端
        producer.build();
        producer.latch.await();

        producer.create();
        TimeUnit.SECONDS.sleep(300);
    }

    public void build() throws IOException {
        try (InputStream is = Producer.class.getResourceAsStream("/zk.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            path = properties.getProperty("path");
            zk = new ZooKeeper(properties.getProperty("address"),
                    Integer.parseInt(properties.getProperty("sessionTimeout")),
                    (event) -> {
                        if (event.getType() == Watcher.Event.EventType.None) {
                            System.out.println("zk client is build");
                            latch.countDown();
                        }
                    });

        }
    }

    public void create() throws Exception {
        String uuid = UUID.randomUUID().toString();
        zk.create(path + "/cr", uuid.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    }

}
