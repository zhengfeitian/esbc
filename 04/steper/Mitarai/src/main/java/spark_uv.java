import kafka.serializer.StringDecoder;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;
import scala.Tuple2;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class spark_uv {
    static int []users;
    public static void main(String[] args) throws InterruptedException {
        String brokers = args[0];
        String topics = args[1];
        users=new int[8000000];
        // Create context with a 5 seconds batch interval
        SparkConf sparkConf = new SparkConf().setAppName("spark_uv");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf,Durations.seconds(5));
        HashSet<String> topicsSet = new HashSet<String>(Arrays.asList(topics.split(",")));
        HashMap<String, String> kafkaParams = new HashMap<String, String>();
        kafkaParams.put("metadata.broker.list", brokers);
        // Create direct kafka stream with brokers and topics
        JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class,
                kafkaParams,
                topicsSet
        );
        // Get the lines
        JavaRDD<String> lines = messages.map(new Function<Tuple2<String, String>, String>() {
            @Override
            public String call(Tuple2<String, String> tuple2) {
                return tuple2._2();
            }
        });
        JavaPairRDD<String, Integer> LogPairRDD = mapLogRDD2Pair(lines);
        users=null; // 防止内存泄漏
        JavaPairRDD<String, Integer> uvCountPairRDD =  uvCount(LogPairRDD);
        uvCountPairRDD.saveAsTextFile("hdfs://localhost/user");
        uvCountPairRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            public void call(Tuple2<String, Integer> pairs) throws Exception {
                System.out.println(pairs.toString());
            }
        });
        jssc.start();
        jssc.awaitTermination();
    }
    private static JavaPairRDD<String, Integer> mapLogRDD2Pair(JavaRDD<String> lines){
        return lines.mapToPair(new PairFunction<String, String, Integer>() {
            public Tuple2<String,Integer> call(String line) throws Exception {
                line=line.replace(":"," ");
                String[] arr = line.split(" ");
                String time=arr[0]+" "+arr[1]+" h "+arr[2]+" m ";
                int id=Integer.parseInt(arr[5]);
                if(arr[4].equals("in")&&users[id]==0){
                    users[id]=1;
                    return new Tuple2<String, Integer>(time,1);
                }
                else {
                    return new Tuple2<String, Integer>(time,0);
                }
            }
        });
    }
    private static JavaPairRDD<String,Integer> uvCount(JavaPairRDD<String,Integer> LogPairRDD){
        return LogPairRDD.reduceByKey(new Function2<Integer,Integer,Integer>() {
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1 + v2;
            }
        });
    }
}
