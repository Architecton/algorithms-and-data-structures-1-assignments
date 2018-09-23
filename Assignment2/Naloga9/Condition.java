class Condition {
	// atribut, ki predstavlja indeks prijatelja
	private int indexFriend;
	// atribut, ki predstavlja pogoj, ki ga postavlja prijatelj z indeksom indexFriend
	private int condition;
	
	// konstruktor
	public Condition(int indexFriend, int condition) {
		this.indexFriend = indexFriend;
		this.condition = condition;
	}
	
	// getters and setters
	public int getIndexFriend() {
		return this.indexFriend;
	}
	
	public int getCondition() {
		return this.condition;
	}
}
