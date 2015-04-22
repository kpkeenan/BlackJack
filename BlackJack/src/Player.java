
public class Player {
		private  String name;
		private  int score;
		private int balance;
		private boolean isDealer;
		
		public Player(){
			name = "";
			score = 0;
			balance = 1000;
			isDealer = false;
		}
		
		public Player(String name, boolean d){
			this.name = name;
			this.score = 0;
			this.balance = 1000;
			this.isDealer = d;
		}
	
		public void setBalance(int n){
			balance = n;
		}
		
		public int getBalance(){
			return balance;
		}
		
		public void setRole(boolean b){
			this.isDealer = b;
		}
		
		public boolean isDealer(){
			return isDealer;
		}
		
		public void deduct(){
			balance-=100;
		}
		public void deposit(){
			balance+=100;
		}

		public String getName(){
			return name;
		}
		
		
		public void setName(String n){
			name = n;
		}
		
		public void updateScore(int s){
			score += s;
		}
		
		public void resetScore(){
			score = 0;
		}
		
		public void updateScore(Card c1, Card c2){
			score = score + c1.getValue() + c2.getValue();
		}
		
		public int getScore(){
			return score;
		}
		
		
		public boolean hasBlackJack(){
			return score == 21;
		}		
}
