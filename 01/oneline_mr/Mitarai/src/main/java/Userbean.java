import org.apache.hadoop.io.Writable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Userbean implements Writable {
    private String id;
    private String intime;
    private String outtime;
    //在反序列化时，反射机制需要调用无参构造方法，所以显式定义了一个无参构造方法
    public Userbean() {
        super();
    }
    public Userbean(String id,String intime,String outtime) {
        super();
        this.id=id;
        this.intime=intime;
        this.outtime=outtime;
    }
    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id=id;
    }
    public String getintime() {
        return intime;
    }
    public void setintime(String intime) {
        this.intime=intime;
    }
    public String getouttime() {
        return outtime;
    }
    public void settime(String outtime) {
        this.outtime=outtime;
    }
    //重写序列化方法
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(id);
        out.writeUTF(intime);
        out.writeUTF(outtime);
    }
    //重写反序列化方法
    @Override
    public void readFields(DataInput in) throws IOException {
        id = in.readUTF();
        intime = in.readUTF();
        outtime = in.readUTF();
    }
    //重写toString方法
    @Override
    public String toString() {
        return id+" "+intime+" "+outtime;
    }
}
