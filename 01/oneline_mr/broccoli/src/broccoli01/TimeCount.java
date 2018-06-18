package broccoli01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class TimeCount {

  public static class TokenizerMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      String line = value.toString().replaceAll("\n", "");
      String[] items = line.split(" ");
      
      String state = items[3];
      String user_id = items[2];
      String[] time = items[1].split(":");
      int hour = Integer.parseInt(time[0]);
      int minute = Integer.parseInt(time[1]);
      int second = Integer.parseInt(time[2]);
      
      int total_second = hour*60*60 + minute*60 +second;
      
      if(state.equals("in")){
    	  total_second *= -1;
      }

      context.write(new Text(user_id),
    		  new IntWritable(total_second));
    }
  }

  public static class SumReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      ArrayList<Integer> time = new ArrayList<>();
      for (IntWritable val : values) {
    	  time.add(val.get());
      }
      time.sort(new Comparator<Integer>() {
    	  @Override
    	  public int compare(Integer a, Integer b){
    		  return Math.abs(a)-Math.abs(b);
    	  }
		});
      int flag = 0;
      int prev = 0;
      int total_time = 0;
      for (int i =0;i<time.size();i++){
    	  if (flag==0 && time.get(i)<0){
    		  flag = 1;
    		  prev = time.get(i);
    	  }
    	  else if(flag==0 && time.get(i)>0){
    		  total_time += time.get(i)+prev;
    		  flag = 0;
    	  }
    	  else if(flag == 1 && time.get(i)<0){
    		  prev = time.get(i);
    	  }
      }
      result.set(total_time);
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(TimeCount.class);
    job.setMapperClass(TokenizerMapper.class);
    job.setCombinerClass(SumReducer.class);
    job.setReducerClass(SumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}

