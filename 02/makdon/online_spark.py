from pyspark import SparkContext
sc = SparkContext()

def map_to_UserID_log(log):
    """
    This function is use to take out the UserID
    :param log:String
    :return list([userID, log:Sting])
    """
    log_list = log.split()
    try:
        userID = log_list[2]
        return userID, log
    except:
        return None

def get_time_by_second_from_log(log):
    """
    This function is used to get the time from log, return the num of second form 00:00:00
    :param log: String, presented as 2018-6-13 0:14:24 1157881 out
    :return int
    """
    time = log.split()[1].split(":")
    hour = int(time[0])
    minute = int(time[1])
    second = int(time[2])
    time_by_second = hour*3600 + minute*60 + second
    return time_by_second

def count_one_user_online_mapper(item):
    """
    This function is used to count one user's online time
    :param logs: list of logs, in which are String
    :param return: online_time
    """
    userID = item[0]
    logs_iter = item[1]
    online_time = 0
    logs = list()
    for iter in logs_iter:
        logs.append(iter)
    # logs = list(logs).sort()
    if logs[0].split()[3] == "out":
        # start with logout log
        first_log = logs.pop(0)
        time_by_second = get_time_by_second_from_log(first_log)
        online_time += time_by_second
    
    if logs[-1].split()[3] == "in":
        # end with login log
        end_log = logs.pop(-1)
        time_by_second = get_time_by_second_from_log(end_log)
        online_time += ( 86400 - time_by_second)
    login_time = 0
    logout_time = 0
    for log in logs:
        if log.split()[3] == "in":
            login_time = get_time_by_second_from_log(log)
        if log.split()[3] == "out":
            logout_time = get_time_by_second_from_log(log)
            online_time += logout_time - login_time
    return online_time
        
        

def count_total_time_reducer(time_a, time_b):
    """
    This function is used to count the total online time of all user
    :param time_a: the first time to combine
    :param time_b: the second time to combine
    :return the sum of the two time
    """
    return time_a + time_b



def count(file_path):
    """
    This function is use to count a total average online of a very big log file
    :param file_path: Sting ,the path of the log in the HDFS
    :return result:int
    """
    logs_raw = sc.textFile(file_path)
    userID_and_log = logs_raw.map(map_to_UserID_log).groupByKey()
    user_and_online_time = userID_and_log.map(count_one_user_online_mapper)
    user_and_online_time.cache()
    num_of_user = user_and_online_time.count()
    total_online_time = user_and_online_time.reduce(count_total_time_reducer)
    result = total_online_time / num_of_user
    return result


if __name__ == "__main__":
    result = count("/root/logs_spark")
    result_RDD = sc.parallelize([result])
    result_RDD.saveAsTextFile("/root/sparkresult")
    
