package com.cr.demo;

import com.cr.WatcherHolder;
import com.cr.ZK;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class CreateNode {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        WatcherHolder.holder.set((event) -> {
            System.out.println(event);
            if(Watcher.Event.KeeperState.SyncConnected == event.getState()){
                latch.countDown();
            }
        });
        ZooKeeper zk = ZK.getInstance();
        latch.await();

        System.out.println(zk.create("/car", "accord".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT));
    }

}
