import java.io.Serializable;

// razred, ki predstavlja element v vrsti
class QueueElement implements Serializable {
	// vsebina elementa vrste
	Warehouse element;
	// kazalec na naslednji element v vrsti
	QueueElement next;
	
	// konstruktor
	QueueElement(Warehouse element) {
		this.element = element;
		this.next = null;
	}
}
