#!/usr/bin/python
import sys
from random import *
import datetime

USER_NUM = 8000000
LOG_NUM = 5000000000 	
SECONDS = 24*60*60
LOG_PER_MAP = 100

LogFile = 'random.log'


# for line in log_io.readlines():
for line in sys.stdin:
        # no line

        line = line.strip()
        date, time, user_id, state = line.split(' ',4)
        pt = datetime.datetime.strptime(time,'%H:%M:%S')
        seconds = pt.hour*60*60+pt.minute*60+pt.second

        if state.startswith('in'):
        	seconds *= -1

        print('%s %s' % (user_id,seconds))
        #output.write('%s %s\n' % (user_id,seconds))

