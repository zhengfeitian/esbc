import java.util.StringTokenizer;

public class testsomething {
    public static void main(String[] args) {
        StringTokenizer itr = new StringTokenizer("2018-09-01 11:11:11 1234567  in");
        while (itr.hasMoreTokens()){
            System.out.println(itr.nextToken());
        }
    }
}
