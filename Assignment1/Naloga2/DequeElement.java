import java.io.Serializable;

// razred, ki predstavlja element v vrsti
class DequeElement implements Serializable {
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
