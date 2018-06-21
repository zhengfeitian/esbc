#! /bin/bash
for end_of_ip in 2 3 4 5 6 7 8 9 10
#for end_of_ip in 95 96 97
do 
	echo ${end_of_ip}
	scp -r ~/spark pc@slave${end_of_ip}:/home/pc/
	ssh root@slave${end_of_ip} "yes | sudo apt-get install scala"
	scp /etc/profile root@slave${end_of_ip}:/etc/
	ssh root@slave${end_of_ip} "source /etc/profile"
done


