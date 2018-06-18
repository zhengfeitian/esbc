import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class OnlineCountReducer extends Reducer<IntWritable, Text,IntWritable, IntWritable> {
    int online_time_second = 0;
    IntWritable result = new IntWritable();
    Pattern pattern = Pattern.compile("[0-9][0-9]:[0-9][0-9]:[0-9][0-9]");
    Pattern pattern_in_out = Pattern.compile("(in)|(out)");

    public void reduce(IntWritable userID, Iterable<Text> logs_raw, Context context) throws IOException,InterruptedException {
        // int num = 0;
        // for(Text val:logs_raw){
        //     num += 1;
        // }
        // context.write(userID,new IntWritable(num));
        online_time_second = 0;
        ArrayList<ArrayList<String>> logs = new ArrayList<ArrayList<String>>();
        for(Text log_raw:logs_raw) {
            //log[String log,int second_from_00_00_00, String in_or_out]
            ArrayList<String> log = new ArrayList<String>();
            String log_text = new String(log_raw.getBytes());
            log.add(log_text);

            Matcher m = pattern.matcher(log_text);
            if(m.find()) {
                int second = 0;
                String time_00_00_00 = m.group(0);
                StringTokenizer itr = new StringTokenizer(time_00_00_00, ":");
                second += Integer.valueOf(itr.nextToken()) * 3600;
                second += Integer.valueOf(itr.nextToken()) * 60;
                second += Integer.valueOf(itr.nextToken());
                log.add(second + "");
            }

            Matcher n = pattern_in_out.matcher(log_text);
            if(n.find()) {
                String string_in_out = n.group(0);
                log.add(string_in_out);
                logs.add(log);
            }

        }
        // 对log 进行排序
        Collections.sort(logs, new Comparator<ArrayList<String>>() {
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return Integer.valueOf(o1.get(1)) > Integer.valueOf(o2.get(1)) ? 1:-1;
            }
        });

        /*
        for(ArrayList<String> log:logs){
            online_time_second += Integer.valueOf(log.get(1));
        }
        */
        // 开始计算一个用户的在线时间

        //以登出记录开头的

        while (logs.get(0).get(2)=="out"){
            online_time_second += Integer.getInteger(logs.get(0).get(1));
            logs.remove(0);
        }
        while (logs.get(logs.size()-1).get(2)=="in"){
            online_time_second += (86400 - Integer.getInteger(logs.get(logs.size()-1).get(1)) );
            logs.remove(logs.size()-1);
        }

        int out_time = 86400;
        int in_time = 0;
        while (!logs.isEmpty()){

            if(logs.get(logs.size()-1).get(2)=="out") {
                out_time = Integer.getInteger(logs.get(logs.size() - 1).get(1));
                logs.remove(logs.size() - 1);
            }
            else if (logs.get(logs.size()-1).get(2)=="in") {
                in_time = Integer.getInteger(logs.get(logs.size() - 1).get(1));
                logs.remove(logs.size() - 1);
            }
            else
            {
                logs.remove(logs.size() - 1 );
            }
            online_time_second += (out_time - in_time);

        }


        result.set(online_time_second);
        context.write(userID,result);

    }
}