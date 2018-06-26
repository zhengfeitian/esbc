import java.util.ArrayList;

public class SuperLongArray {

	public static void main(String[] args) {
		ArrayList<String> superLongList = new ArrayList<String>();
		while(true){
			superLongList.add("this is an item");
			try{
				Thread.sleep(1);
			}catch(Exception e){ }
			Integer meaning_less_value = new Integer((int)System.currentTimeMillis());
		}
	}
}
