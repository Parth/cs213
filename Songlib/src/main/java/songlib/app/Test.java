package songlib.app;

public class Test {
	public Test() {
		System.out.println(getClass().getResource("/SongLib.fxml"));
	}

	public static void main(String args[]) {
		Test t = new Test();
	}
}
