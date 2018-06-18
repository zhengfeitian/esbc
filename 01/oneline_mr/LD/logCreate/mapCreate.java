import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.text.*;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;



public class mapCreate extends Mapper<Object, Text, IntWritable,Text> {
    IntWritable time_offset = new IntWritable(1);
    Text word = new Text();
    int multiple = 10;
    int num_of_users = 8000000;

    public void createLog(Date date,int second_offset,int userID,boolean in_or_out, Context context)
            throws IOException,InterruptedException{
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        int hour = second_offset/3600;
        int minute = (second_offset%3600)/60;
        int second = second_offset % 60;
        String log = ft.format(date) + " " +
                (hour>=10?String.valueOf(hour):("0"+String.valueOf(hour)))
                + ":"+String.valueOf(minute) + ":"+
                String.valueOf(second) + " " + userID + (in_or_out?"  in":" out");
        word.set(log);
        time_offset.set(second_offset/3600);
        context.write(time_offset,word);
    }

    public void mapper(Object key, Text value, Context context) throws IOException,InterruptedException {

        for(int i=0;i<multiple;i++){

            double random_number = (int)(Math.random()*100)+50;
            int userID = (int)(Math.random()*num_of_users);


            ArrayList<Integer> times=new ArrayList<Integer>();
            for(int j=0;j<random_number;j++){
                times.add((int)(Math.random()*86400));
            }
            Collections.sort(times);

            if(random_number%56==0){

                Boolean in_out_flag = false;
                while(times.size()!=0){
                    int second_offset = times.get(times.size()-1);
                    Date date = new Date();
                    create_one_log(date,second_offset,userID,in_out_flag,context);
                    in_out_flag = !in_out_flag;
                    times.remove(times.size()-1);
                }

            }
            else if(random_number%7==0){

                if(random_number%2==0){

                    times.remove(times.size()-1);
                }
                Boolean in_out_flag = false;
                while(times.size()!=0){
                    int second_offset = times.get(times.size()-1);
                    Date date = new Date();
                    create_one_log(date,second_offset,userID,in_out_flag,context);
                    in_out_flag = !in_out_flag;
                    times.remove(times.size()-1);
                }

            }
            else if(random_number%8==0){


                times.remove(times.size()-1);
                Boolean in_out_flag = true;
                while(times.size()!=0){
                    int second_offset = times.get(times.size()-1);
                    Date date = new Date();
                    create_one_log(date,second_offset,userID,in_out_flag,context);
                    in_out_flag = !in_out_flag;
                    times.remove(times.size()-1);
                }
            }
            else{

                if(random_number%2==0){

                    times.remove(times.size()-1);
                }
                Boolean in_out_flag = true;
                while(times.size()!=0){
                    int second_offset = times.get(times.size()-1);
                    Date date = new Date();
                    create_one_log(date,second_offset,userID,in_out_flag,context);
                    in_out_flag = !in_out_flag;
                    times.remove(times.size()-1);
                }
            }
        }
    }
}