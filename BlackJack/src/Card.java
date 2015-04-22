
public class Card {

	private  String suit;
	private  int value;
	private String path;
	
	public Card(String su, int val,String p){
		suit = su;
		value = val;
		path = p;
	}
	
	public String getSuit(){
		return suit;
	}
	
	public int getValue(){
		return value;
	}
	
	public void setSuit(String s){
		suit = s;
	}
	public void setValue(int v){
		value = v;
	}
	
	public void setPath(String p){
		this.path = path;
	}
	
	public String getPath(){
		return path;
	}
	
}
