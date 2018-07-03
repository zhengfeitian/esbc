import sys
from pyspark import SparkConf
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils
import time
import random

global_num = None

def extractRDD(time,rdd):
    hasWritten = False
    while not hasWritten:
        try:
            with open('uv.log','a') as f:
	        f.write('\n'+str(rdd.collect()[0]))
            hasWritten = True
        except:
            pass

if __name__ == '__main__':
    rigi=random.randint(1,100000)
    brokers = "master:9092"
    topic = "uv"
 
    conf = SparkConf()
    sc = SparkContext(master='local[4]', appName="Eser streaming")
    ssc = StreamingContext(sc, 5)
    kk = KafkaUtils.createDirectStream(ssc, [topic], {"metadata.broker.list": brokers})

    #print(kk.count())

    lines=kk.map(lambda x : x[1].split(' ')[2]).countByValue().count()
    #print(len(lines))
    lines.foreachRDD(extractRDD)
    lines.pprint()

 
    ssc.start()
    ssc.awaitTermination()
