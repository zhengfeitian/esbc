package memleak;

import java.util.HashMap;
import java.util.Map;

public class ClassLoaderLeakExample {
	  public static void main(String[] args) {
		    Map<Key, String> map = new HashMap<Key, String>(1000);   
		    int counter = 0;
		    while (true) {
		       // creates duplicate objects due to bad Key class
			  char[] data = new char[1000000];
		      map.put(new Key("dummyKey"),new String(data));
		      counter++;
		      if (counter % 1000 == 0) {
		        System.out.println("Map size: " + map.size());
		        System.out.println("Free memory after count " + counter
		         + " is " + getFreeMemory() + "MB");       
		        sleep(1000);
		      }     
		    }
		  }
		 
		  // inner class key without hashcode() or equals() 
		  // bad implementation causes memory leak
		  static class Key {
		    private String key; 
		    public Key(String key) {
		      this.key = key;
		    } 
		  }
		 
		  public static void sleep(long sleepFor) {
		    try {
		      Thread.sleep(sleepFor);
		    } catch (InterruptedException e) {
		      e.printStackTrace();
		    }
		  }
		 
		  public static long getFreeMemory() {
		    return Runtime.getRuntime().freeMemory() / (1024 * 1024);
		  }
		 
		}

		/**
		  static class Key {
		    private String key; 
		    public Key(String key) {
		      this.key = key;
		    }
		    @Override
		    public boolean equals(Object obj) { 
		      if (obj instanceof Key) {
		        return key.equals(((Key) obj).key);
		      } else {
		        return false;
		      } 
		    }
		 
		    @Override
		    public int hashCode() {
		      return key.hashCode();
		    }
		    **/

