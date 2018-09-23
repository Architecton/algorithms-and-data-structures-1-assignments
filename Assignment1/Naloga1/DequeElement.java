
// razred, ki predstavlja element v vrsti
class DequeElement {
	// vsebina elementa vrste
	String element;
	// kazalec na naslednji element v vrsti
	DequeElement next;
	// kazalec na prejsni element v vrsti
	DequeElement previous;
	
	// konstruktor
	DequeElement(String element) {
		this.element = element;
		this.next = null;
		this.previous = null;
	}
}