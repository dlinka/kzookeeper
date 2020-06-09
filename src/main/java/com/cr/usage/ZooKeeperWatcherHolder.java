package com.cr.usage;

import org.apache.zookeeper.Watcher;

public class ZooKeeperWatcherHolder {
    public static ThreadLocal<Watcher> holder = new ThreadLocal<>();
}
