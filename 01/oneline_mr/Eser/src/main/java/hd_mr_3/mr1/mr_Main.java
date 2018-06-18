package hd_mr_3.mr1;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
//没有注释的吗
public class mr_Main {
	public static class TokenizerMapper extends Mapper<Object, Text, Text, Text>{
		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();
		private Text Tval = new Text();
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String[] line = value.toString().split(" ");
			String[] time = line[1].split(":");
			int hour = Integer.parseInt(time[0]);
			int minute = Integer.parseInt(time[1]);
			int second = Integer.parseInt(time[2]);
			int all_sec = hour*3600+minute*60+second;
			String st_sec = String.valueOf(all_sec);
			String val_towrite = st_sec+" "+line[3];
			word.set(line[2]);
			Tval.set(val_towrite);
			context.write(word,Tval);
			}
		}
	public static class TLog {
		public int time;
		public boolean login;
	}
	
	public static void quickSort(TLog[] numbers, int start, int end) {   
		if (start < end) {   
			TLog base = numbers[start];
			TLog temp; 
			int i = start, j = end;   
			do {   
				while ((numbers[i].time < base.time) && (i < end))   
					i++;   
				while ((numbers[j].time > base.time) && (j > start))   
					j--;   
				if (i <= j) {   
					temp = numbers[i];   
					numbers[i] = numbers[j];   
					numbers[j] = temp;   
					i++;   
					j--;   
				}   
			} while (i <= j);   
			if (start < j)   
				quickSort(numbers, start, j);   
			if (end > i)   
				quickSort(numbers, i, end);   
			}
		}  
	public static class IntSumReducer extends Reducer<Text,Text,Text,IntWritable> {
		private IntWritable Time_wr = new IntWritable();
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			int pre_login_time=0;
			int total_time=0;
			ArrayList<TLog> tlogarr = new ArrayList<TLog>();
			for (Text val : values) {
				TLog temptlog = new TLog();
				String[] eachval = val.toString().split(" ");
				boolean login = eachval[1].equals("in");
				int now_sec = Integer.parseInt(eachval[0]);
				temptlog.time=now_sec;
				temptlog.login=login;
				tlogarr.add(temptlog);
				}
			int end=tlogarr.size()-1;
			TLog[] tlog = (TLog[])tlogarr.toArray(new TLog[end+1]);
			quickSort(tlog,0,end);
			for (TLog tt:tlog){
				if(tt.login){
					pre_login_time=tt.time;
					}
				else{
					total_time+=tt.time-pre_login_time;
					pre_login_time = total_time;
					}
				}
			Time_wr.set(total_time);
			context.write(key, Time_wr);
			}
		}
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "word count");
		job.setJarByClass(mr_Main.class);
		job.setMapperClass(TokenizerMapper.class);
		//job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
		}
	}
