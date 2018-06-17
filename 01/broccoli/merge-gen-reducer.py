#!/usr/bin/python
import sys
from random import *

USER_NUM = 8000000
LOG_NUM = 5000000000
SECONDS = 24*60*60
LOG_PER_MAP = 1000000


for line in sys.stdin:
	line = line.strip()
	date,time, userid, state = line.split(' ',3)

	date = datetime.date.today()

	output_line = date.isoformat() + ' ' + \
		str(datetime.timedelta(seconds=int(seconds))) + ' ' + \
		str(userid) + ' ' + \
		state 
	print (output_line)

