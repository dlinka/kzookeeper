package com.cr.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class WatchNodeExist {

    public static void main(String[] args) throws KeeperException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        WatcherHolder.holder.set((event) -> {
            System.out.println(event);
            if(Watcher.Event.KeeperState.SyncConnected == event.getState()){
                latch.countDown();
            }
        });
        ZooKeeper zk = ZK.getInstance();
        latch.await();
        System.out.println(zk.exists("/car", false));
    }

}
