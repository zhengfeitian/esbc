from kafka import KafkaProducer
from kafka.errors import KafkaError
import time
import math
import random

topic_name = 'uv'
producer = KafkaProducer()
counter = 0
send_speed = 1

james = time.time()

def send_x_times(s):
    # userID_range = s / 2
    global counter
    userID = 1
    for x in range(int(s)):
        counter += 1
        print(counter)
        log = time.strftime("%Y-%m-%d %H:%M:%S") + " " + str(userID)
        future = producer.send(topic_name, log) 
        userID += 1 
while True:
    # send_time = math.floor(random.random() * 50000)
    if time.time() - james >= 1:
        james = time.time()
    	send_x_times(send_speed)
        send_speed *= 2
    





