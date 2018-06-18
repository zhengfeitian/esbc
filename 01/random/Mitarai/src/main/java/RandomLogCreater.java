import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.net.URI;

public class RandomLogCreater {

    static int num=8000000;//8000000;
    static long totallog=5000000000L;//5000000000L;
    static long remainlog;
    static int []aid=new int[num];
    static String sh;
    static String sm;
    static String ss;
    static String strtime;
    static String strinorout;
    static String strlog;
    static long number=0L;
    //其中hdfs://group2是在Hadoop配置文件中写好的路径，不需要带端口号，/test.txt就是在HDFS根目录下的test.txt
    static String filePath = "hdfs://master:9000/user/mitarai/logtest.txt";

    public static void main(String[] args){
        for(int i=0;i<num;i++) {
            aid[i]=0;
        }
        remainlog=totallog;
        writelog();
    }

    public static int getrandomid() {
        return (int) (1+Math.random()*num);
    }

    public static void gettime(int h,int m, int s) {
        if(h<10)sh="0"+h;
        else sh=""+h;
        if(m<10)sm="0"+m;
        else sm=""+m;
        if(s<10)ss="0"+s;
        else ss=""+s;
        strtime="2018-6-14 "+sh+":"+sm+":"+ss;
    }

    public static void getinorout(int i) {
        int n=aid[i-1];
        if(n%2==0)strinorout="in";
        else strinorout="out";
        aid[i-1]++;
    }

    public static void writelog() {
        try {
            //获取Hadoop的配置信息
            Configuration conf = new Configuration();
            // 不设置该代码会出现错误：java.io.IOException: No FileSystem for scheme: hdfs
            conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

            Path path = new Path(filePath);

            // 这里需要设置URI，否则出现错误：java.lang.IllegalArgumentException: Wrong FS: hdfs://127.0.0.1:9000/test/test.txt, expected: file:///
            FileSystem fs = FileSystem.get(new URI(filePath), conf);
            FSDataOutputStream os = fs.create(path);

            for(int h=0;h<24;h++) {
                for (int m = 0; m < 60; m++) {
                    for (int s = 0; s < 60; s++) {
                        gettime(h, m, s);
                        int logthissec = getrandomid() / 10;  //
                        remainlog -= logthissec;
                        if (remainlog < 0) logthissec += remainlog;
                        for (int i = 0; i < logthissec; i++) {
                            int id = getrandomid();
                            getinorout(id);
                            strlog = strtime + " " + strinorout + " " + id;
                            byte[] buff = strlog.getBytes();
                            os.write(buff, 0, buff.length);
                            number++;
                        }
                        if (remainlog <= 0) break;
                    }
                    if (remainlog <= 0) break;
                }
                if (remainlog <= 0) break;
            }
            for(int i=0;i<remainlog;i++) {
                int id=getrandomid();
                getinorout(id);
                strlog=strtime+" "+strinorout+" "+id;
                byte[] buff = strlog.getBytes();
                os.write(buff,0, buff.length);
                number++;
            }
            os.close();
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
