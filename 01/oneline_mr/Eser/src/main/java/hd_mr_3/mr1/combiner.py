#Calculate the average online time of a person in a day, according to the output of the mr


file = open("part-r-00000","r").read().split('\n')

totle_time = 0
for line in file:
	line = line.split()
	if len(line)!=0:
		totle_time+=int(line[1])

totle_time/=len(file)
hour = totle_time/3600
minute = (totle_time-hour*3600)/60
res_sec = totle_time-hour*3600-minute*60
print "Total Second\t:",totle_time
print "HH:MM:SS\t:","%d:%d:%d"%(hour,minute,res_sec)
		
