package com.cr.usage;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WatchPath {

    private ZooKeeper zk;

    public static void main(String[] args) throws KeeperException, InterruptedException {
        WatchPath wp = new WatchPath();
        wp.watchNode();
        TimeUnit.SECONDS.sleep(100);
    }

    public void watchNode() throws InterruptedException, KeeperException {
        CountDownLatch latch = new CountDownLatch(1);
        ZooKeeperWatcherHolder.holder.set((event) -> {
            System.out.println(event);
            if (Watcher.Event.EventType.None == event.getType()) {
                latch.countDown();
            } else if (Watcher.Event.EventType.NodeChildrenChanged == event.getType()) {
                try {
                    nodeChildrenChanged();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        zk = ZooKeeperInstance.get();
        latch.await();

        //true也只会监听一次
        List<String> children = zk.getChildren("/", true);
        children.forEach(child -> System.out.println(child));
    }

    public void nodeChildrenChanged() throws KeeperException, InterruptedException {
        //false不监听
        List<String> children = zk.getChildren("/", false);
        children.forEach(child -> System.out.println(child));
    }

}
