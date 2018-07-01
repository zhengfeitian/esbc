import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class randomlogger {
	static String filepath="                         ";
	static int num=800;//8000000;
	static long totallog=200000L;//5000000000L;
	static long remainlog;
	static int []aid=new int[num];
	static String sh;
	static String sm;
	static String ss;
	static String strtime;
	static String strinorout;
	static String strlog;
	static long number=0L;
	public static void main(String[] args) throws IOException {
		for(int i=0;i<num;i++) {
			aid[i]=0;
		}
		remainlog=totallog;
		writelog();
	}
	public static int getrandomid() {
		return (int) (1+Math.random()*num);
	} 
	public static void gettime(int h,int m, int s) {
		if(h<10)sh="0"+h;
		else sh=""+h;
		if(m<10)sm="0"+m;
		else sm=""+m;
		if(s<10)ss="0"+s;
		else ss=""+s;
		strtime="2018-6-14 "+sh+":"+sm+":"+ss;
	}
	public static void getinorout(int i) {
		int n=aid[i-1];		
		if(n%2==0)strinorout="in";
		else strinorout="out";
		aid[i-1]++;
	}
	public static void writelog() throws IOException {
		File file = new File(filepath);
		if(file.isFile()){file.delete();}   
		file.createNewFile();      
		FileOutputStream fos =new FileOutputStream(file); 
		OutputStreamWriter outWriter = new OutputStreamWriter(fos, "UTF-8");  
        BufferedWriter bufWrite = new BufferedWriter(outWriter);  
		for(int h=0;h<24;h++) {
			for(int m=0;m<60;m++) {
				for(int s=0;s<60;s++) {
					gettime(h,m,s);
					int logthissec=getrandomid();  
					remainlog-=logthissec;
					if(remainlog<0) logthissec+=remainlog;
					for(int i=0;i<logthissec;i++) {
					int id=getrandomid();
					getinorout(id);
					strlog=strtime+" "+strinorout+" "+id;					
					bufWrite.write(strlog+"\r\n");
					number++;
					}
					if(remainlog<=0) break;
				}
				if(remainlog<=0) break;
			}
			if(remainlog<=0) break;
		}
		for(int i=0;i<remainlog;i++) {
			int id=getrandomid();
			getinorout(id);
			strlog=strtime+" "+strinorout+" "+id;					
			bufWrite.write(strlog+"\r\n");
			number++;
		}
		bufWrite.flush();
		fos.close();
		System.out.println("number is "+number);
	}
}
