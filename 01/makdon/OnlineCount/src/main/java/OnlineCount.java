import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class OnlineCount {
    private static String inputPath = "./input";
    private static String outputPath = "./output";

    public static void main(String[] args) throws Exception {
        OnlineCount client = new OnlineCount();

        if (args.length == 2) {
            inputPath = args[0];
            outputPath = args[1];
        }
        else{
            System.err.println("Usage: hadoop jar OnlineCount.jar <in> <out>");
            System.exit(2);
        }
        client.execute();
    }

    private void execute() throws Exception {
        String tmpOutputPath = outputPath + "_tmp";
        user_average_Job(inputPath, tmpOutputPath);
        all_average_Job(tmpOutputPath, outputPath);
        System.exit(0);
    }

    private int user_average_Job(String inputPath, String outputPath) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "user_average");
        job.setJarByClass(OnlineCount.class);
        job.setMapperClass(OnlineCountMapper.class);
        job.setReducerClass(OnlineCountReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));


        return job.waitForCompletion(true) ? 0 : 1;
    }

    private int all_average_Job(String inputPath, String outputPath) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf, "all_average");
        job.setJarByClass(OnlineCount.class);
        job.setMapperClass(SecondMapper.class);
        job.setCombinerClass(SecondReducer.class);
        job.setReducerClass(SecondReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));
        return job.waitForCompletion(true) ? 0 : 1;
    }
}