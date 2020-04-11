package com.cr;

import org.apache.zookeeper.Watcher;

public class WatcherHolder {
    public static ThreadLocal<Watcher> holder = new ThreadLocal<>();
}
