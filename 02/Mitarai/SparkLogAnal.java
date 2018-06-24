import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

public class SparkLogAnal {

    static long alltime=0;

    public static void main(String[] args) {
        /*Create a JavaSparkContext that loads settings from system properties
         (for instance, when launching with ./bin/spark-submit).*/
        JavaSparkContext sc = new JavaSparkContext();
        JavaRDD<String> lines = sc.textFile("/user/esmLog.txt");
        JavaPairRDD<String, Integer> LogPairRDD = mapLogRDD2Pair(lines);
        JavaPairRDD<String, Integer> countLogPairRDD =  logCount(LogPairRDD);
        //reduce后log中的用户数量
        long n=countLogPairRDD.keys().count();
        countLogPairRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            public void call(Tuple2<String, Integer> pairs) throws Exception {
                alltime+=pairs._2;
            }
        });
        double average=((double) alltime)/n;
        System.out.println("the average online time per user and per day is "+average+" seconds");
        sc.close();
    }

    private static JavaPairRDD<String, Integer> mapLogRDD2Pair(JavaRDD<String> lines){
        return lines.mapToPair(new PairFunction<String, String, Integer>() {
            //line是对应文件中的一行数据
            //log pattern: 2018-6-14 00:00:00 in 1
            public Tuple2<String,Integer> call(String line) throws Exception {
                //00:00:00 to 00 00 00
                line=line.replace(":"," ");
                String[] arr = line.split(" ");
                int h,m,s;
                if(arr[1].charAt(0)=='0'){
                    h=Integer.parseInt(String.valueOf(arr[0].charAt(1)));
                }else{
                    h=Integer.parseInt(arr[0]);
                }
                if(arr[2].charAt(0)=='0'){
                    m=Integer.parseInt(String.valueOf(arr[1].charAt(1)));
                }else{
                    m=Integer.parseInt(arr[1]);
                }
                if(arr[3].charAt(0)=='0'){
                    s=Integer.parseInt(String.valueOf(arr[2].charAt(1)));
                }else{
                    s=Integer.parseInt(arr[2]);
                }
                //时间转换为秒
                s=3600*h+60*m+s;
                //in 把时间设为负
                if(arr[4].equals("in")){
                    s=-s;
                }
                return new Tuple2<String, Integer>(arr[5],s);
            }
        });
    }

    private static JavaPairRDD<String,Integer> logCount(JavaPairRDD<String,Integer> LogPairRDD){
        return LogPairRDD.reduceByKey(new Function2<Integer,Integer,Integer>() {
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
    }
}
