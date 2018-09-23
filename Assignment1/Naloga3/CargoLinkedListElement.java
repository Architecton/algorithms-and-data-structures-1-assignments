// razred, ki predstavlja povezani seznam za shranjevanje objektov razreda Cargo
class CargoLinkedListElement {
	// atributi
	Cargo element;
	CargoLinkedListElement next;
	CargoLinkedListElement previous;
	
	// konstruktor, ce poznamo samo element
	CargoLinkedListElement(Cargo obj) {
		element = obj;
		next = null;
		previous = null;
	}
	
	// konstruktor, ce poznamo element in kazalec next
	CargoLinkedListElement(Cargo obj, CargoLinkedListElement next) {
		this.element = obj;
		this.next = next;
	}
	
	// konstruktor, ce poznamo element, kazalec next in kazalec previous
	CargoLinkedListElement(Cargo obj, CargoLinkedListElement next, CargoLinkedListElement previous) {
		this.element = obj;
		this.next = next;
		this.previous = previous;
	}
}