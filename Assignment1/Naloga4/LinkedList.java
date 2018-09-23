// Implementacija povezanega seznama
class LinkedList {
	protected LinkedListElement first;
	protected LinkedListElement last;
	private int length;
	
	// konstruktor, ki inicializira nov LinkedList
	LinkedList() {
		makenull();
	}
	
	// Funkcija makenull. inicializira seznam
	public void makenull() {
		first = new LinkedListElement(null, null);
		last = null;
		this.length = 0;
	}
	
	// metoda ki vrne true, ce je ta seznam prazen
	public boolean isEmpty() {
		// ce "last" kaze na null, je seznam prazen
		if(last == null) {
			return true;
		} else {
			return false;
		}
	}
	
	// metoda, ki vrne prvi element v seznamu brez da ga odstrani
	public Wagon peek() {
		return this.first.next.element;
	}
	
	// metoda, ki pridobi prvi element v seznamu in ga odstrani
	public Wagon pop() {
		// pridobimo element, ki ga bomo odstranili (ce seznam ni prazen)
		Wagon result = null;
		if(!this.isEmpty()) {
			result = this.first.next.element;
		} else {
			return null;
		}
		// odstranimo element - klicemo funkcijo remove(int index)
		this.remove(0);
		return result;
	}
	
	// Funkcija add doda nov element na konec seznama
	public void add(Wagon obj) {
		// naredimo nov element
		LinkedListElement newEl = new LinkedListElement(obj, null);
		// povecamo pomnjeno dolzino seznama
		(this.length)++;
		// po dogovoru velja: ce je seznam prazen, potem kazalec "last" ne kaze nikamor
		if(last == null) {
			// ce seznam vsebuje samo en element, kazalca "first" in "last" kazeta na glavo seznama
			first.next = newEl;
			newEl.previous = first;
			// "last" in "first" kazeta na isto mesto
			last = first;
		} else {
			// sicer dodamo element na konec
			last.next.next = newEl;
			newEl.previous = last.next;
			// kazalec "last" povecamo za eno mesto
			last = last.next;
		}
	}
	
	// Funkcija push doda nov element na prvo mesto v seznamu (takoj za glavo seznama)
	void push(Wagon obj) {
		// najprej naredimo nov element
		LinkedListElement newEl = new LinkedListElement(obj);
		
		// nov element postavimo za glavo seznama
		newEl.next = first.next;
		newEl.next.previous = newEl;
		newEl.previous = first;
		first.next = newEl;
		
		// povecamo pomnjeno dolzino seznama
		(this.length)++;
		
		// preverimo, ce je to edini element v seznamu
		if(last == null) {
			last = first;
		// ce je v seznamu do sedaj bil samo en element
		} else if (last == first) {
			last = newEl;
		}
	}
	
	//Funkcija size vrne pomnjeno dolzino tega seznama
	int size() {
		return this.length;
	}
	
	// metoda, ki vrne element z indeksom index
	Wagon get(int index) {
		// deklrariramo novo instanco razreda LinkedListElement
		LinkedListElement el;
		
		// zacnemo pri glavi seznama
		el = first;
		
		// premaknemo se index-krat
		for(int i = 0; i <= index; i++) {
			el = el.next;
			// ce iskani index ne obstaja
			if(el == null) {
				return null;
			}
		}
		return el.element;
	}

	// metoda, ki izbrise element na indeksu index
	boolean remove(int index) {
		// deklariramo dve instanci razreda LinkedListElement
		LinkedListElement el;
		// zacnemo pri glavi seznama
		el = first;
		// premaknemo se index-krat (eno mesto za element, ki ga zelimo izbrisati)
		for(int i = 0; i < index; i++) {
			el = el.next;
			if(el == null) {
				return false;
			}
		}
		
		// ce nismo prisli do zadnjega elementa v seznamu
		if(el.next != null) {
			// preden izlocimo element preverimo, ali je potrebno popraviti kazalec "last"
			// ce brisemo predzadnji element (kamor trenutno kaze kazalec "last")
			if(last == el.next) {
				last = el;
				el.next.next.previous = el;
			// ce brišemo cisto zadnji element v seznamu (element za kazalcem last)
			} else if (last == el) { 
				// nastavimo kazalec "last" na prejsni element
				last = el.previous;
			}
			// zmanjsamo pomnjeno velikost seznama
			(this.length)--;
			
			// izlocimo nasledji element
			el.next = el.next.next;
			return true;
		} else {
			return false;
		}
	}
	
	// metoda, ki vrne zadnji element v vrsti, brez da ga odstrani
	public Wagon peekLast() {
		//ce je seznam prazen, vrnemo null
		if(this.isEmpty()) {
			return null;
		} else {
			// vrnemo zadnji element v vrsti
			return this.last.next.element;
		}
	}
	
	// metoda ki odstrani in vrne zadnji element v seznamu
	public Wagon removeLast() {
		Wagon result = null;
		// ce seznam ni prazen, pridobimo zadnji element
		if(!this.isEmpty()) {
			result = this.last.next.element;
		} else {
			return null;
		}
		
		// prevezemo kazalce
		this.last = this.last.previous;
		this.last.next.next = null;
		
		// zmanjsamo pomnjeno velikost seznama
		(this.length)--;
		
		// vrnemo rezultat
		return result;
	}
	
	// metoda, ki vrne referenco na element first v tem seznamu
	public LinkedListElement getFirstLinkedListElement() {
		return this.first;
	}
	
	// TODO komentarji
	private static LinkedListElement split(LinkedListElement head) {
		LinkedListElement fast = head, slow = head;
		while (fast.next != null && fast.next.next != null) {
			fast = fast.next.next;
			slow = slow.next;
		}
		LinkedListElement temp = slow.next;
		slow.next = null;
		return temp;
	}
	 
	public static LinkedListElement mergeSort(LinkedListElement LinkedListElement) {
		if (LinkedListElement == null || LinkedListElement.next == null) {
			return LinkedListElement;
		}
		LinkedListElement second = split(LinkedListElement);
		
		// Recur for left and right halves
		LinkedListElement = mergeSort(LinkedListElement);
		second = mergeSort(second);	 
		// Merge the two sorted halves
		return merge(LinkedListElement, second);
	}
 
	// Function to merge two linked lists
	private static LinkedListElement merge(LinkedListElement first, LinkedListElement second) {
		// If first linked list is empty
		if(first == null) {
			return second;
		}
		
		// If second linked list is empty
		if(second == null) {
			return first;
		}
		// Pick the smaller value
		if(first.element.getTotalWeight() <= second.element.getTotalWeight()) {
			first.next = merge(first.next, second);
			first.next.previous = first;
			first.previous = null;
			return first;
		} else {
			second.next = merge(first, second.next);
			second.next.previous = second;
			second.previous = null;
			return second;
		}
	}
}