
// Razred, ki predstavlja vozlisce v izraznem drevesu
class Node {
	
	// atribut, ki predstavlja vsebino tega vozlisca
	String value;
	// atribut, ki predstavlja trenutno logicno vrednost tega vozlisca
	String logicalValue;
	// kazalci na sinova
	Node left, right;
	
	// konstruktor
	Node(String item) {
		this.value = item;
		this.left = this.right = null;
		this.logicalValue = "NONE";
	}
	
	// overloaded konstruktor za terme z vnaprej znane logicne vrednosti
	Node(String item, String logicalValue) {
		this.value = item;
		this.left = this.right = null;
		this.logicalValue = logicalValue;
	}
	
	// toString: metoda za testiranje
	@Override
	public String toString() {
		String result = "";
		result += value;
		return result;
	}
	
	@Override
	public boolean equals(Object other) {
		Node toCompare = (Node)other;
		if(this.value.equals(toCompare.value)) {
			return true;
		}
		return false;
	}
}