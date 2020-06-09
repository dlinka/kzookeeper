package com.cr.register;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Consumer {

    private ZooKeeper zk;
    private CountDownLatch latch = new CountDownLatch(1);
    private String path;

    public static void main(String[] args) throws Exception {
        Consumer consumer = new Consumer();

        //构建消费者客户端
        consumer.build();
        consumer.latch.await();

        consumer.getProducers();
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
                            return;
                        }
                        if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                            getProducers();
                            return;
                        }
                        if (event.getType() == Watcher.Event.EventType.NodeDataChanged) {
                            getProducers();
                            return;
                        }
                    });
        }
    }

    public void getProducers() {
        Map<String, String> producers = new HashMap<>();
        try {
            List<String> children = zk.getChildren(path, true);
            for (String producer : children) {
                byte[] data = zk.getData(path + "/" + producer, true, null);
                producers.put(producer, new String(data));
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("producers -  [" + producers + "]");
    }

}
