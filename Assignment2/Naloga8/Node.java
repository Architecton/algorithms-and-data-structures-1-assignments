
// Razred, ki predstavlja vozlisce v izraznem drevesu
class Node {
	// atribut, ki predstavlja vsebino tega vozlisca
	String value;
	// kazalci na sinova
	Node left, right;
	
	// konstruktor
	Node(String item) {
		this.value = item;
		this.left = this.right = null;
	}
	
	// toString: metoda za testiranje
	@Override
	public String toString() {
		String result = "";
		result += value;
		return result;
	}
}
