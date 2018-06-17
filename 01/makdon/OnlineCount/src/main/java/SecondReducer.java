import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SecondReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    long sum = 0;
    long num = 0;
    IntWritable value = new IntWritable(33);
    Text one = new Text("result:");
    public void reduce(Text useless, Iterable<IntWritable> times, Context context) throws IOException,InterruptedException {
        sum = 0;
        num = 0;
        for(IntWritable val:times){
            num += 1;
            sum += val.get();
        }
        value.set((int)(sum/num));
        context.write(one, value);
    }
}
