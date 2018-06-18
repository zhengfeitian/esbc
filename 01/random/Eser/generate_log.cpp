//
//  main.cpp
//  Playground
//
//  Created by Crysple on 19/03/2018.
//  Copyright © 2018 Crysple. All rights reserved.
//
//  This is used to generate login/logout log randomly
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <dirent.h>
#include <time.h>
#include <iostream>
#include <unordered_map>
using namespace std;
const int MAX_USER=1600000;
//const int MAX_SECOND=86400;
//Max Seconds for a day
const int MAX_SECOND=86400;
//Count from START_SECOND. It is designed for generate log Parallel
const int START_SECOND=0;
//Number of total records
const long long RECORD_NUM=1000000000;
//Records for every second. We assumed that it comforms to even distribution
const long long USER_PER_SECOND=RECORD_NUM/86400;
int main(int argc, char* argv[]){
    if(argc<2) return 0;
    srand((unsigned)time(NULL));
    unordered_map<int, int> user_login;
    int fd=open(argv[1],O_WRONLY|O_CREAT,0777);
    if(fd==-1) {printf("ERROR OPEN\n");return 0;}
    char buf[100];
    memset(buf,0,sizeof(buf));
    int hour=0,minute=0,second=0,user_id=0;
    int per_minute=-1;
    for(int s=START_SECOND;s<MAX_SECOND;++s){
        hour=s/3600;
        minute=(s-hour*3600)/60;
	second=(s-minute*60-hour*3600);
        if(per_minute!=minute){
            per_minute=minute;
            printf("Current Time: %d\:%d\n",hour,minute);
        }
        for(int i=0;i<USER_PER_SECOND;++i){
            user_id=rand()%MAX_USER;
            sprintf(buf, "2018-6-13 %d\:%d\:%d %d %s\n",hour,minute,second,user_id,user_login[user_id]?"out":"in");
            if(user_login[user_id]) user_login[user_id]=0;
            else user_login[user_id]=1;
            write(fd,buf,strlen(buf));
        }
    }
    close(fd);
    return 0;
}
