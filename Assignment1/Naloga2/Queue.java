import java.io.Serializable;

// razred, ki predstavlja podatkovni tip vrsta
class Queue implements Serializable{
	// kazalca, ki kazeta na zacetek in na konec vrste
	private QueueElement front;
	private QueueElement rear;
	
	// konstruktor
	public Queue() {
		makenull();
	}
	
	// metoda, ki inicializira prazno vrsto
	public void makenull() {
		front = null;
		rear = null;
	}
	
	// metoda, ki vrne true, ce je vrsta prazna in false, ce ni prazna
	public boolean isEmpty() {
		return (front == null);
	}
	
	// metoda, ki doda element v vrsto (enqueue)
	public void add(Warehouse obj) {
		// kreiramo novo instanco razreda QueueElement z ustrezno vsebino
		QueueElement el = new QueueElement(obj);
		// ce je bila vrsta prazna, bosta oba kazalca sedaj kazala na ta element
		if(this.front == null) {
			this.front = this.rear = el;
		// sicer
		} else {
			this.rear.next = el;
			this.rear = el;
		}
	}
	
	// metoda, ki pridobi naslednji element iz vrste
	public Warehouse remove() {
		// ce je vrsta prazna, vrnemo null
		if(this.front == null){
			return null;
		// ce je v vrsti samo en element, ta element vrnemo in kazalca front in rear nastavimo na null
		} else if(this.front == this.rear) {
			Warehouse res = this.front.element;
			this.front = this.rear = null;
			return res;
		// sicer vrnemo element, na katerega kaze kazalec front, 
		// kazalec front premaknemo naprej
		} else {
			Warehouse res = this.front.element;
			this.front = this.front.next;
			return res;
		}
	}
}