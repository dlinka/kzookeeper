package com.cr;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

public class ZK {
    private volatile static ZooKeeper instance;

    private ZK() {
    }

    public static ZooKeeper getInstance() {
        if (instance == null) {
            synchronized (ZK.class) {
                if (instance == null) {
                    try {
                        instance = new ZooKeeper("127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183", 2000, (watcher) -> {
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }
}
