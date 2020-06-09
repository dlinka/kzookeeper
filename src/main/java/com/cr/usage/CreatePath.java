package com.cr.usage;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class CreatePath {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ZooKeeperWatcherHolder.holder.set((event) -> {
            System.out.println(event);
            if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                latch.countDown();
            }
        });
        ZooKeeper zk = ZooKeeperInstance.get();
        latch.await();

        String path = "/cr";
        byte[] data = "陈润".getBytes();
        ArrayList<ACL> acls = ZooDefs.Ids.OPEN_ACL_UNSAFE;
        CreateMode mode = CreateMode.PERSISTENT;
        zk.create(path, data, acls, mode);
    }

}
