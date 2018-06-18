import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class countMapper extends Mapper<Text,Text,Text,Userbean> {


    protected void map(Text key, Text value, org.apache.hadoop.mapreduce.Mapper.Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        String[] fields = line.split(" ");

        String time = fields[1];
        String inorout = fields[2];
        String id = fields[3];

        if(inorout.equals("in")) {
            context.write(new Text(id), new Userbean(id,time,"00:00:00"));
        }else {
            context.write(new Text(id), new Userbean(id,"00:00:00",time));
        }
    }
}
