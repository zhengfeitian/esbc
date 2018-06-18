import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class LogReducer extends Reducer<Text,Userbean,Text,Userbean> {
    //框架每传递一组数据调用一次reduce方法
    //reduce中的业务逻辑是遍历values，然后进行累加求和后输出
    protected void reduce(Text key, Iterable<Userbean> value, org.apache.hadoop.mapreduce.Reducer.Context context)
            throws java.io.IOException, InterruptedException {
        int inh = 0, inm = 0, ins = 0, outh = 0, outm = 0, outs = 0;
        for (Userbean b : value) {
            String[] outhms = b.getouttime().split(":");
            if (outhms[0].charAt(0) == '0') {
                outh += Integer.parseInt(String.valueOf(outhms[0].charAt(1)));
            } else outh += Integer.parseInt(outhms[0]);
            if (outhms[1].charAt(0) == '0') {
                outm += Integer.parseInt(String.valueOf(outhms[1].charAt(1)));
            } else outm += Integer.parseInt(outhms[1]);
            if (outhms[2].charAt(0) == '0') {
                outs += Integer.parseInt(String.valueOf(outhms[2].charAt(1)));
            } else outs += Integer.parseInt(outhms[2]);
            String[] inhms = b.getintime().split(":");
            if (inhms[0].charAt(0) == '0') {
                inh += Integer.parseInt(String.valueOf(inhms[0].charAt(1)));
            } else inh += Integer.parseInt(inhms[0]);
            if (inhms[1].charAt(0) == '0') {
                inm += Integer.parseInt(String.valueOf(inhms[1].charAt(1)));
            } else inm += Integer.parseInt(inhms[1]);
            if (inhms[2].charAt(0) == '0') {
                ins += Integer.parseInt(String.valueOf(inhms[2].charAt(1)));
            } else ins += Integer.parseInt(inhms[2]);
        }
        //写出时，value是一个bean对象，因为要写到文件中去，所以要重写其toString()方法
        String sinh, sinm, sins, south, soutm, souts, sin, sout;
        if (inh < 10) sinh = "0" + inh;
        else sinh = "" + inh;
        if (inm < 10) sinm = "0" + inm;
        else sinm = "" + inm;
        if (ins < 10) sins = "0" + ins;
        else sins = "" + ins;
        if (outh < 10) south = "0" + outh;
        else south = "" + outh;
        if (outm < 10) soutm = "0" + outm;
        else soutm = "" + outm;
        if (outs < 10) souts = "0" + outs;
        else souts = "" + outs;
        sin = sinh + ":" + sinm + ":" + sins;
        sout = south + ":" + soutm + ":" + souts;
        context.write(key, new Userbean(key.toString(), sin, sout));
    }
}
