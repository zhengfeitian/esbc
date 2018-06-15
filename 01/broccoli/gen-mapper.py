#!/usr/bin/python
import sys
from random import *

USER_NUM = 8000000
LOG_NUM = 5000000000
SECONDS = 24*60*60
LOG_PER_MAP = 100

f = open('input.txt','r')
ot = open('output.txt','w')

for line in f.readlines():
        # no line

        state = ['in','out']
        
        for i in range(LOG_PER_MAP):
	        user_id = randrange(1,USER_NUM)
	        key = randrange(0,SECONDS)
	        val = str(user_id) + " " + state[randrange(2)] 
	        ot.write('%s %s\n' % (key,val))

f.close()
ot.close()