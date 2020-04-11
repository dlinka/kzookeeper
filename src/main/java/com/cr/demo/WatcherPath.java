package com.cr.demo;

import com.cr.WatcherHolder;
import com.cr.ZK;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WatcherPath {

    private ZooKeeper zk;

    public void watchNode() throws InterruptedException, KeeperException {
        CountDownLatch latch = new CountDownLatch(1);
        WatcherHolder.holder.set((event) -> {
            if (Watcher.Event.EventType.None == event.getType()) {
                latch.countDown();
            } else if(Watcher.Event.EventType.NodeChildrenChanged == event.getType()){
                try {
                    nodeChanged();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        zk = ZK.getInstance();
        latch.await();

        List<String> childrens = zk.getChildren("/", true); //true:只会通知一次
        childrens.forEach(children -> System.out.println(children));
    }

    public void nodeChanged() throws KeeperException, InterruptedException {
        List<String> childrens = zk.getChildren("/", false); //false:下次改变就不会通知
        childrens.forEach(children -> System.out.println(children));
    }

    public static void main(String[] args) throws KeeperException, InterruptedException {
        WatcherPath wp = new WatcherPath();
        wp.watchNode();
        TimeUnit.SECONDS.sleep(100);
    }

}
