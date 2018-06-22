from pyspark import SparkContext, SparkConf

sc = SparkContext()


def linesplit(line):
	line = line.split()
	time = line[1].split(":")
	sec = int(time[0])*3600+int(time[1])*60+int(time[2])
	return [line[2],[(sec,line[3])]]

def merge_user(a):
	l = sorted(a[1],key=lambda x:x[0])
	res = 0
	pre = 0
	for tp in l:
		if tp[1]=="out":
			res+=tp[0]-pre
		pre=tp[0]
	return [pre,1]

textFile=sc.textFile("/user/LOG.txt")
tf = textFile.map(linesplit).reduceByKey(lambda a,b:a+b)
tf = tf.map(merge_user).reduce(lambda a,b:(a[0]+b[0],a[1]+b[1]))

ans = int(float(tf[0])/tf[1])


fl = open("/home/pc/eser/spmr_res.txt","w")

fl.write(str(ans)+" s\n")

fl.write("%s:%s:%s\n"%(str(ans/3600),str(ans%3600/60),str(ans%60)))

fl.close()
