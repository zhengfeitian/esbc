class A{
	private String a;
	public B b;
	public A(){
		a = "a";
	}
	public void include(B b_){
		b = b_;
	}
}

class B{
	private String b;
	public A a;
	public B(){
		b = "b";
	}
	public void include(A a_){
		a = a_;
	}
}


public class CircularReferences {
	public static void main(String[] args) {
		while(true){
			A a = new A();
			B b = new B();
			a.include(b);
			b.include(a);
		}
	}
}
