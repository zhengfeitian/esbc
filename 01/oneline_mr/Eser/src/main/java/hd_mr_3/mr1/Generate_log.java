package hd_mr_3.mr1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Random;
import java.io.IOException;  

public class Generate_log {
	public static class MyMapper extends Mapper<Text, Text, IntWritable, IntWritable>{  
        private final static IntWritable one = new IntWritable(1);  
        private Text event = new Text();  
        private int MAX_USERID=5000000;
        private int MAX_SECOND=86400;
        //private int MUTIPLE=5000000/2;
        private int MUTIPLE=5;
        private IntWritable time=new IntWritable(0);
        private IntWritable user_id =new IntWritable(0);
        //private int EVERY=1000;
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        	int CNT=MUTIPLE;
        	Random random = new Random();
        	//System.out.println(123);
        	while(--CNT>=0) {
        		user_id.set(random.nextInt(MAX_USERID));
        		int logout_time = random.nextInt(MAX_SECOND);
        		int login_time = random.nextInt(logout_time);
        		//simulate the situation where there's only login | logout
        		//(1 --> ONLY logout) (2 --> ONLY login)
        		int flag = random.nextInt(40);
        		if(flag!=1) {
        			time.set(login_time);
        			context.write(time,user_id);
        		}
        		if (flag!=2) {
        			time.set(logout_time);
        			context.write(time,user_id);
        		}
        	}
        }  
    }  
   
	public static class IteblogPartitioner extends Partitioner<IntWritable, IntWritable> {
        @Override
        public int getPartition(IntWritable key, IntWritable value, int numPartitions) {
            int keyInt = Integer.parseInt(key.toString());
            if (keyInt < 30000) {
                return 0;
            } else if (keyInt < 56000) {
                return 1;
            } else {
                return 2;
            }
        }
    }
	
    public static class MyReducer extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable> {  
        //private IntWritable result = new IntWritable();  
   
        public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {  
            int sum = 0;
            //context.write(key, key);
            for (IntWritable val : values) {
            	context.write(key, val);  
            }
        }  
    }  
   
    public static void main(String[] args) throws Exception {  
    	
        Configuration conf = new Configuration();  
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();  
        if (otherArgs.length < 2) {  
            System.err.println("Usage: EventCount <in> <out>");  
            System.exit(2);  
        }  
        Job job = Job.getInstance(conf, "generate log");  
        job.setJarByClass(Generate_log.class);  
        job.setMapperClass(MyMapper.class);
        job.setPartitionerClass(IteblogPartitioner.class);
        job.setReducerClass(MyReducer.class);  
        
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        job.setOutputKeyClass(IntWritable.class);  
        job.setOutputValueClass(IntWritable.class);
        
        job.setNumReduceTasks(3);
        
        //System.out.println(otherArgs[0]);
        //System.out.println(otherArgs[1]);
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));  
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));  
        System.exit(job.waitForCompletion(true) ? 0 : 1); 

    } 
}
