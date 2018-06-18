import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class OnlineCountMapper extends Mapper<Object, Text, IntWritable, Text> {
    IntWritable user_id = new IntWritable(1);
    Text log = new Text("");

    public void map(Object key, Text value, Context context) throws IOException,InterruptedException {
        StringTokenizer itr = new StringTokenizer(value.toString());
        String date = itr.nextToken();
        String time = itr.nextToken();
        String userID = itr.nextToken();
        String in_or_out = itr.nextToken();
        log = value;
        user_id.set(Integer.valueOf(userID));
        context.write(user_id,log);
    }
}