#!/usr/bin/python
from operator import itemgetter 
import datetime
import sys 
from itertools import groupby
import IPython

def read_mapper_output(file,separator=' '):
	for line in file:
		yield line.rstrip().split(separator,1)

def main(separator=' '):
	data = read_mapper_output(sys.stdin, separator=separator)


	total_time = 0
	for user_id, u_time in groupby(data, itemgetter(0)):
              
 
                time = [int(item[1]) for item in u_time]
		log_time = sorted(time, key=abs)
		flag = 0
		last = None
		for i in log_time:
			if flag==0 and i<0:
				last = i
				flag = 1
			if flag==1 and i<0:
				last = i
			if flag == 1 and i>0:
				total_time += i+last
				flag = 0

	print total_time





if __name__ == '__main__':
	main()
