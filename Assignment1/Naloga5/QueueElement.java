// razred, ki predstavlja element v vrsti
class QueueElement {
	// vsebina elementa vrste
	ExecutionContext element;
	// kazalec na naslednji element v vrsti
	QueueElement next;
	
	// konstruktor
	QueueElement(ExecutionContext element) {
		this.element = element;
		this.next = null;
	}
}