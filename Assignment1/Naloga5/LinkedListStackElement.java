import java.io.Serializable;

// razred, ki predstavlja element na skladu
class LinkedListStackElement implements Serializable{
	// vsebina
	CallStackElement element;
	// kazalec na naslednji element
	LinkedListStackElement next;
	
	// konstruktor
	LinkedListStackElement(CallStackElement obj) {
		element = obj;
		next = null;	}
	
	// konstruktor (overloaded)
	LinkedListStackElement(CallStackElement obj, LinkedListStackElement next) {
		this.element = obj;
		this.next = next;
	}
}