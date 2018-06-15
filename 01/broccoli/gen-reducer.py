#!/usr/bin/python
from operator import itemgetter 
import datetime
import sys 


f = open('output.txt','r')
log = open('random.log','w')
# for line in sys.stdin:
for line in f.readlines():
	line = line.strip()
	seconds, userid, state = line.split(' ',3)

	date = datetime.date.today()

	output_line = date.isoformat() + ' ' + \
		str(datetime.timedelta(seconds=int(seconds))) + ' ' + \
		str(userid) + ' ' + \
		state 
	print (output_line)
	log.write(output_line+'\n')

f.close()
log.close()