// razred, ki predstavlja element povezanega seznama
class LinkedListElement {
	// atributi
	Wagon element;
	LinkedListElement next;
	LinkedListElement previous;
	
	// konstruktor, ce poznamo element
	LinkedListElement(Wagon obj) {
		element = obj;
		next = null;
		previous = null;
	}
	
	// konstruktor, ce poznamo element in naslednje vozlisce povezanega seznama
	LinkedListElement(Wagon obj, LinkedListElement next) {
		this.element = obj;
		this.next = next;
	}
	
	// konstruktor, ce poznamo element, naslednje in prejsnje vozlisce povezanega seznama
	LinkedListElement(Wagon obj, LinkedListElement next, LinkedListElement previous) {
		this.element = obj;
		this.next = next;
		this.previous = previous;
	}
}
