
public class PrimeThread  {
	public static void main(String[] args){
		(new Thread(new BJGui())).start();
		(new Thread(new Log())).start();
	}
}
