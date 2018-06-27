# kafka安装:

- 从官网下载kafka，此处使用0.9.0.1版本，注意太新可能不被flume支持
- 解压，然后先启动zookeeper，然后使用bin/kafka-server-start.sh config/server.properties 启动，集群启动的话需要把server.properties里面的broker.id修改为不同的数值


# flume安装使用

- 下载，解压
- 修改配置文件，此处修改得到conf/flume-conf-hdfs2kafka.properties（实际上是从硬盘读取没有从HDFS读取
- 运行，使用命令 bin/flume-ng agent n flume2kafka_agent -f conf/flume-conf-hdfs2kafka.properties
- flume-conf-hdfs2kafkakeep.properties这个配置文件配合output_log.sh可以做到保持输入流

流拓扑：
flume source： exec： output_log.sh
        |
        v
flume channel： memory
        |
        v
flume cink: Kafka
        |
        v
kafka console consumer