import sys
from pyspark import SparkConf
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils
import time

batch_interval = 5
def main():
        conf = SparkConf()
        sc = SparkContext(master="local[4]",appName="fds")
        ssc  = StreamingContext(sc,batch_interval)
        kafka = KafkaUtils.createDirectStream(ssc,["uv"],{"metadata.broker.list":"master:9092"})

        temp = kafka.map(lambda x:(x[1].split(" ")[2],0)).groupByKey().count()

        temp.pprint()

        ssc.start()
        ssc.awaitTermination()



if __name__ == '__main__':
        main()
