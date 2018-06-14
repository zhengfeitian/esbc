#!/usr/bin/python
from operator import itemgetter 
import datetime
import sys 
for line in sys.stdin:
	line = line.strip()
	seconds, userid, state = line.split(' ',1)

	date = datetime.date.today()

	output_line = date.isoformat() + ' ' + \
		datetime.timedelta(seconds=seconds) + ' ' + \
		str(userid) + ' ' + \
		state 
	print output_line
