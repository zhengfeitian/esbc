from pyspark import SparkContext, SparkConf

sc = SparkContext()



def extractRDD(rdd):
    hasWritten = False
    while not hasWritten:
        try:
            with open('uv_all.log','a') as f:
	        f.write('\n'+str(rdd.collect()[0]))
            hasWritten = True
        except:
            pass

def merge_user(a):
	l = sorted(a[1],key=lambda x:x[0])
	res = 0
	pre = 0
	for tp in l:
		if tp[1]=="out":
			res+=tp[0]-pre
		pre=tp[0]
	return [pre,1]

kk=sc.textFile("/user/LOG.txt")
lines=kk.map(lambda x : x[1].split(' ')[2]).countByValue().count()
extractRDD(lines)

