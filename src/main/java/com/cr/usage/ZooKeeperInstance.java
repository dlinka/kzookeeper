package com.cr.usage;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZooKeeperInstance {
    private volatile static ZooKeeper instance;

    private ZooKeeperInstance() {
    }

    public static ZooKeeper get() {
        if (instance == null) {
            synchronized (ZooKeeperInstance.class) {
                if (instance == null) {
                    try {
                        ThreadLocal<Watcher> holder = ZooKeeperWatcherHolder.holder;
                        Watcher watcher = holder.get();
                        if (watcher == null) {
                            watcher = (event) -> {};
                        }
                        instance = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 2000, watcher);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }
}
