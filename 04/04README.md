数据流：
由python实时随机速度生成log数据——>kafka——>spark-->本地文件-->Flask Web服务器-->网页数据可视化
                                        |
                                        v
                                    HDFS持久化


使用文档：
* 以下操作皆为master上运行

- 开启系统后，先使用init-slave.sh，为每个电脑初始化hostname，并启动zookeeper
- 然后cd进入kafka目录， ./bin/kafka-server-start.sh config/server.properties 启动kafka服务器
- 使用命令python log_as_kafka_source.py 实时生成log数据塞进去kafka
- 使用spk_uv.py作为spark streaming读取kafka的数据，并持久化到本地和HDFS
- 运行python uv.py，然后访问localhost:5000查看网页。此步骤需要当前目录内有index.html和static文件夹。