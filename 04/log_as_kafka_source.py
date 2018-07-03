from kafka import KafkaProducer
from kafka.errors import KafkaError
import time
import math
import random

topic_name = 'uv'
producer = KafkaProducer()
counter = 0


def send_x_times(s):
    userID_range = s / 2
    global counter
    for x in range(int(s)):
        counter += 1
        print(counter)
        userID = str(int(math.floor(random.random() * userID_range)))
        log = time.strftime("%Y-%m-%d %H:%M:%S") + " " + userID
        future = producer.send(topic_name, log) 
    

while True:
    send_time = math.floor(random.random() * 50000)
    send_x_times(send_time)     




