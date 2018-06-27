# 分析文档

## kafka's jstack

- 运行进程罗列


| Process Name                                | Priority | OS_Priority | 
|---------------------------------------------|----------|-------------| 
| Attach Listener                             | 9        | 0           | 
| kafka-socket-acceptor-PLAINTEXT-9092        | 5        | 0           | 
| kafka-network-thread-0-PLAINTEXT-2          | 5        | 0           | 
| kafka-network-thread-0-PLAINTEXT-1          | 5        | 0           | 
| kafka-network-thread-0-PLAINTEXT-0          | 5        | 0           | 
| main-SendThread(localhost:2181)             | 5        | 0           | 
| RMI TCP Accept-0                            | 5        | 0           | 
| Service Thread                              | 9        | 0           | 
| C1 CompilerThread1                          | 9        | 0           | 
| C2 CompilerThread0                          | 9        | 0           | 
| Signal Dispatcher                           | 9        | 0           | 
| Surrogate Locker Thread (Concurrent GC)     | 9        | 0           | 
| VM Thread                                   | /        | 0           | 
| Gang worker#0 (Parallel GC Threads)         | /        | 0           | 
| G1 Main Concurrent Mark GC Thread           | /        | 0           | 
| Gang worker#0 (G1 Parallel Marking Threads) | /        | 0           | 
| G1 Concurrent Refinement Thread#0           | /        | 0           | 
| G1 Concurrent Refinement Thread#1           | /        | 0           | 
| VM Periodic Task Thread                     | /        | 0           | 


## Flume's jstat

此时flume的jstat明显是有问题的，正常情况应该时S0和S1两个区在一定时间内互相交换使用情况，但该图的S0一直为空，而S1大部分时间接近满的状态，小部分时间有小幅度下降，程序内存管理异常。

观察到此时的O区均为100%可以知道内存被塞爆了，说明垃圾回收机制不能有效地回收内存，程序发生了内存泄漏或者程序太大机子太垃圾运行不了。

于此我们进一步观察Flume的jmap stat如下

## Flume's jmap histo

可以看到占用内存最多且实例为Byte是比较正常的，但是接下来的三个分别时hashMap,LinkedBlockingDeque和flume.event.SimpleEvent，可以猜想内存泄漏很大可能就是这三个东西造成的，之后需要进一步检查程序代码debug
