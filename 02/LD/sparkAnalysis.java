import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;



public class sparkAnalysis {

    public static void main (String[] args ){

        logAnalysis l = new logAnalysis();
        JavaSparkContext sc = new JavaSparkContext();
        JavaRDD<String> lines = sc.textFile("/user/esmLog.txt");
        JavaPairRDD<String, Integer> LogPairRDD = mapLogRDD2Pair(lines);
        JavaPairRDD<String, Integer> countLogPairRDD =  l.logForCount(LogPairRDD);
        long n=countLogPairRDD.keys().count();
        double average=l.countTime((l.logForCount))/n;
        System.out.println("平均在线时间为 "+average+" 秒");
        sc.close();

    }
}
