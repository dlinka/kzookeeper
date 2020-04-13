package com.cr;

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
    private CountDownLatch eventNone = new CountDownLatch(1);
    private String serversNode;

    public static void main(String[] args) throws Exception {
        Consumer consumer = new Consumer();
        consumer.buildZKClient();
        consumer.eventNone.await();
        consumer.getProducers();
        TimeUnit.SECONDS.sleep(300);
    }

    private void buildZKClient() throws IOException {
        try (InputStream is = Producer.class.getResourceAsStream("/zk.properties")) {
            Properties properties = new Properties();
            properties.load(is);
            zk = new ZooKeeper(properties.getProperty("address"),
                    Integer.parseInt(properties.getProperty("sessionTimeout")),
                    (event) -> {
                        System.out.println(event);
                        if (event.getType() == Watcher.Event.EventType.None) {
                            System.out.println("zk client is build");
                            eventNone.countDown();
                            return;
                        }
                        if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                            getProducers();
                            return;
                        }
                        if (event.getPath().indexOf("/servers/server") != -1 && event.getType() == Watcher.Event.EventType.NodeDataChanged) {
                            getProducers();
                            return;
                        }
                    });
            serversNode = properties.getProperty("serversNode");
        }
    }

    private void getProducers() {
        try {
            List<String> children = zk.getChildren(serversNode, true);
            Map<String, String> servers = new HashMap<>();
            for (String child : children) {
                byte[] data = zk.getData(serversNode + "/" + child, true, null);
                servers.put(child, new String(data));
            }
            System.out.println("servers [" + servers + "]");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

}
