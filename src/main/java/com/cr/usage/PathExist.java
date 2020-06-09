package com.cr.usage;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

public class PathExist {

    public static void main(String[] args) throws KeeperException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ZooKeeperWatcherHolder.holder.set((event) -> {
            System.out.println(event);
            if(Watcher.Event.KeeperState.SyncConnected == event.getState()){
                latch.countDown();
            }
        });
        ZooKeeper zk = ZooKeeperInstance.get();
        latch.await();

        System.out.println(zk.exists("/cr", false));
    }

}
