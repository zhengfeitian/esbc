import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class SecondMapper extends Mapper<Object, Text, Text, IntWritable> {
    Text one = new Text("result:");
    IntWritable time = new IntWritable();
    public void map(Object key, Text value, Context context) throws IOException,InterruptedException {
        StringTokenizer itr = new StringTokenizer(value.toString());
        String user_id = itr.nextToken();
        String time_text = itr.nextToken();
        time.set(Integer.valueOf(time_text));
        context.write(one,time);
    }
}
