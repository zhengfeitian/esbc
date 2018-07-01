import java.util.Arrays;

/*
 * 在Java中，内存泄漏就是存在一些被分配的对象，
 * 这些对象有下面两个特点:
 * 是可达的;
 * 是无用的，即程序以后不会再使用这些对象。
 * 如果对象满足这两个条件，这些对象就可以判定为Java中的内存泄漏，
 * 这些对象不会被GC所回收，然而它却占用内存。
 */

public class MemoryLeak {
	
	 private static final int DEFAULT_LENGTH = 1;
	 private Object[] elements=new Object[DEFAULT_LENGTH];
	 private int size = 0;
	 
	 public MemoryLeak() {}
	 
	 public void push(Object o) {
		  if (elements.length == size)
			  elements = Arrays.copyOf(elements, 2 * size + 1);
		  elements[size++] = o;
	    }
	 
	 /*
	  * elements[--size]占用的内存没有释放，造成内存泄漏
	  * 可改为：
	  * public Object pop() throws Exception {
		 if (size == 0)
			 throw new Exception();
		 Object o=elements[size-1];
		 elements[--size]=null;
		 return o;
		 }
	  */
	 
	 public Object pop() throws Exception {
		 if (size == 0)
			 throw new Exception();
		 return elements[--size];
		 }

}
