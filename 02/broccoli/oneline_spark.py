from pyspark import SparkContext, SparkConf
from pyspark.streaming import StreamingContext

sc = SparkContext()

def mapper(line):
	line = line.strip()
	# date time user_id state
	items = line.split(" ")
	state = items[3]
	user_id = items[2]
	# time to second
	time = items[1].split(":")
	hour = int(time[0])
	miniute = int(time[1])
	second = int(time[2])
	total_second = hour*60*60+miniute*60+second
	if state.startswith("in"):
		total_second *= -1
	return int(user_id),total_second

def reducer(li):
	li = list(li[1])
	time = sorted(li,key=abs)
	flag = 0
	prev = 0
	total_time = 0
	for i in time:
		if i<0 and flag == 0:
			flag=1
			prev = i
		if i<0 and flag == 1:
			prev = i
		if i>0 and flag == 1:
			flag = 0
			total_time += prev + i
	return total_time
def red(a,b):
	return 1,reducer(a)+reducer(b)
#rdd = sc.textFile("/user/Broccoli/input_test")
rdd = sc.textFile("/user/esmLog.txt")
temp = rdd.map(mapper).groupByKey()
result = temp.map(reducer).reduce(lambda a,b:a+b)

people = 1600000 # this is get from database, because the total number of users is not related to number of people log in or out during a period
print('avg seconds:' + str(result/people))




		
	
