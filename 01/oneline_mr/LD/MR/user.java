import org.apache.hadoop.io.Writable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class User implements Writable {
    private String id;
    private String intime;
    private String outtime;

    public user() {
        super();
    }
    public user(String id,String intime,String outtime) {
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

    public void write(DataOutput out) throws IOException {
        out.writeUTF(id);
        out.writeUTF(intime);
        out.writeUTF(outtime);
    }

    public void readFields(DataInput in) throws IOException {
        id = in.readUTF();
        intime = in.readUTF();
        outtime = in.readUTF();
    }


    public String toString() {
        return id+" "+intime+" "+outtime;
    }
}
