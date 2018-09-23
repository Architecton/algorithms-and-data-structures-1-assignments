
// razred, ki predstavlja podatkovni tip vrsta
class Deque {
	// kazalca, ki kazeta na zacetek in na konec vrste
	private DequeElement front;
	private DequeElement rear;
	private int size;
	
	// konstruktor
	public Deque() {
		makenull();
		this.size = 0;
	}
	
	// metoda, ki inicializira prazno vrsto
	public void makenull() {
		front = null;
		rear = null;
	}
	
	// metoda, ki vrne true, ce je vrsta prazna in false, ce ni prazna
	public boolean isEmpty() {
		return (this.front == null);
	}
	
	// metoda, ki pridobi naslednji element iz vrste
	public String pop() {
		// ce je vrsta prazna, vrnemo null
		if(this.front == null){
			return null;
		// ce je v vrsti samo en element, ta element vrnemo in kazalca front in rear nastavimo na null
		} else if(this.front.equals(this.rear)) {
			String res = this.front.element;
			this.front = this.rear = null;
			(this.size)--;
			return res;
		// sicer vrnemo element, na katerega kaze kazalec front, 
		// kazalec front premaknemo naprej
		} else {
			String res = this.front.element;
			this.front = this.front.next;
			// zmanjsamo pomnjeno velikost te vrste
			(this.size)--;
			return res;
		}
	}
	
	// metoda, ki doda element na zacetek te vrste
	public void push(String obj) {
		DequeElement el = new DequeElement(obj);
		// ce je vrsta prazna
		if(this.front == null) {
			this.front = this.rear = el;
		// sicer, ce je v vrsti ze kaj
		} else {
			// ustrezno prevezemo kazalce
			el.next = this.front;
			this.front.previous = el;
			this.front = el;
		}
		// povecamo polnjeno velikost te vrste
		(this.size)++;
	}
	
	// metoda, ki vrne prvi element v vrsti, brez da ga odstrani
	public String peek() {
		if(this.front == null) {
			return null;
		}
		return this.front.element;
	}
	
	// metoda, ki vrne pomnjeno velikost te vrste
	public int size() {
		return this.size;
	}
	
	// metoda, ki odstrani zadnji element v tej vrsti in ga vrne
	public String removeLast() {
		// ce je vrsta prazna
		if(this.rear == null) {
			return null;
		// ce je v vrsti samo en element
		} else if(this.rear.equals(this.front)) {
			String result = this.rear.element;
			this.rear = this.front = null;
			return result;
		// sicer
		} else {
			String result = this.rear.element;
			this.rear = this.rear.previous;
			this.rear.next = null;
			// zmanjsamo pomnjeno velikost te vrste
			(this.size)--;
			return result;
		}
	}
	
	// metoda, ki vrne zadnji element v vrsti, brez da bi ga odstranila
	public String peekLast() {
		// ce je vrsta prazna, vrni null
		if(this.rear == null) {
			return null;
		}
		return this.rear.element;
	}
	
	// metoda, ki vrne prvi element v vrsti, brez da bi ga odstranila
	public DequeElement getFirstElement() {
		// ce je vrsta prazna, vrni null
		if(this.front == null) {
			return null;
		} else {
			return this.front;
		}
	}
}