import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import java.io.IOException;

public class LogMapper extends Mapper<Text,Text,Text,Userbean> {
    //拿到日志中的一行数据,并切分成各个字段,抽取出我们需要的字段,接着封装成k-v发送出去
    @Override
    protected void map(Text key, Text value, org.apache.hadoop.mapreduce.Mapper.Context context)
            throws IOException, InterruptedException {
        //拿一行数据出来
        String line = value.toString();
        String[] fields = line.split(" ");
        //取出需要的字段
        String time = fields[1];
        String inorout = fields[2];
        String id = fields[3];
        //封装数据并输出
        if(inorout.equals("in")) {
            context.write(new Text(id), new Userbean(id,time,"00:00:00"));
        }else {
            context.write(new Text(id), new Userbean(id,"00:00:00",time));
        }
    }
}
