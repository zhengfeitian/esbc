import java.util.Vector;

public class Main {

    public static void test() {
        Vector v = new Vector(1000);
        for (int i = 1;i < 1000; i++) {
            Object o = new Object();
            v.add(o);
            o = null;
        }
        // 其他代码
        v = null;
        // 其他不使用v的代码
        // 此处程序相对来说，在VECTOR中的引用导致了NEW出来的OBJECT无法被回收，即造成了内存泄漏
    }

    public static void main(String[] args) {

        test();

    }
}
