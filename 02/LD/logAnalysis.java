import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;


public class logAnalysis {



    public static JavaPairRDD<String, Integer> createPair (JavaRDD<String> lines) {

        PairFunction keyData =
                new PairFunction<String,String,Integer>() {
                    public Tuple2<String, Integer> call(String line) throws Exception {

                        line = line.replace(":", " ");     //00:00:00 to 00 00 00
                        String[] arr = line.split(" ");    //拆分
                        int h, m, s;
                        if (arr[1].charAt(0) == '0') {
                            h = Integer.parseInt(String.valueOf(arr[1].charAt(1)));    //0X to X  
                        } else {
                            h = Integer.parseInt(arr[1]);     //X不变直接拿下来
                        }
                        if (arr[2].charAt(0) == '0') {
                            m = Integer.parseInt(String.valueOf(arr[2].charAt(1)));    // 0X to X
                        } else {
                            m = Integer.parseInt(arr[2]);    //X不变直接拿下来
                        }
                        if (arr[3].charAt(0) == '0') {
                            s = Integer.parseInt(String.valueOf(arr[3].charAt(1)));     //0X to X
                        } else {
                            s = Integer.parseInt(arr[3]);     //X不变直接拿下来
                        }

                        s = 3600 * h + 60 * m + s;     //转化为距离00:00:00 的秒数

                        if (arr[4].equals("in")) {
                            s = -s;               // in时间设为负，方便计算差值
                        }
                        return new Tuple2<String, Integer>(arr[5], s);   // <秒数，in or out>
                    }
                };

        JavaPairRDD pairs = lines.mapToPair(keyData);
        return pairs;

    }


    public static JavaPairRDD<String,Integer> logForCount(JavaPairRDD<String,Integer> LogPairRDD){
        return LogPairRDD.reduceByKey(new Function2<Integer,Integer,Integer>() {
            public Integer call(Integer num1, Integer num2) throws Exception {
                return num1 + num2;    //登入登出时间差
            }
        });
    }


    public long countTime(JavaPairRDD<String, Integer> countLogPairRDD ){
        long sum =0;
        countLogPairRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            public void call(Tuple2<String, Integer> pairs) throws Exception {
                sum+=pairs._2;
            }
        });


    }
}
