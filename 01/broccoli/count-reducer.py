#!/usr/bin/python
from operator import itemgetter 
import datetime
import sys 
from itertools import groupby

def read_mapper_output(file,separator=' '):
	for line in file:
		yield line.rstrip().split(separator,1)

def main(separator=' '):
	data = read_mapper_output(sys.stdin, separator=separator)

	total_user = 0

	for user_id, time in groupby(data, itemgetter(0)):
		total_user += 1

	print (total_user)





if __name__ == '__main__':
	main()