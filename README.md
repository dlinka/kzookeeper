# kzookeeper

## 单机部署

    tar -zxvf zookeeper-3.4.14.tar.gz  -C /usr/local/
    mv zoo_sample.cfg zoo.cfg
    
## 集群部署

    //集群中有几台机器就重复几次,下面的操作也一样
    tar -zxvf zookeeper-3.4.14.tar.gz -C /usr/local/zookeeper-3.4.14_1

    mkdir data
    //第一台输入1
    vi myid
    
    vi zoo.cfg
    dataDir=/usr/local/zookeeper-3.4.14_1/data
    clientPort=2181
    //假设集群中有三台
    //server.A=B.C.D
    //A:myid的值
    //B:IP
    //C:与集群中Leader交换信息的端口
    //D:选举时服务器相互通信的端口
    server.1=127.0.0.1:2887:3887
    server.2=127.0.0.1:2888:3888
    server.3=127.0.0.1:2889:3889
    
    遇到Cannot open channel to X at election address这个问题
    //修改IP为0.0.0.0
    server.1=0.0.0.0:2887:3887
    
## 服务端和客户端

    ./bin/zkServer.sh start
    ./bin/zkServer.sh start-foreground
    ./bin/zkServer.sh status
    ./bin/zkServer.sh stop
    
    ./bin/zkCli.sh

---