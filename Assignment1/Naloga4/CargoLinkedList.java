// razred, ki predstavlja povezani seznam za shranjevanje objektov razreda Cargo
class CargoLinkedList {
	protected CargoLinkedListElement first;
	protected CargoLinkedListElement last;
	
	// konstruktor, ki inicializira nov LinkedList
	CargoLinkedList() {
		makenull();
	}
	
	// Funkcija makenull. inicializira seznam
	public void makenull() {
		first = new CargoLinkedListElement(null, null);
		last = null;
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
	public Cargo peek() {
		return this.first.next.element;
	}
	
	// metoda, ki pridobi prvi element v seznamu in ga odstrani
	public Cargo pop() {
		// pridobimo element, ki ga bomo odstranili (ce seznam ni prazen)
		Cargo result = null;
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
	public void add(Cargo obj) {
		// naredimo nov element
		CargoLinkedListElement newEl = new CargoLinkedListElement(obj, null);
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
	void push(Cargo obj) {
		// najprej naredimo nov element
		CargoLinkedListElement newEl = new CargoLinkedListElement(obj);
		
		// nov element postavimo za glavo seznama
		newEl.next = first.next;
		newEl.next.previous = newEl;
		newEl.previous = first;
		first.next = newEl;
		
		// preverimo, ce je to edini element v seznamu
		if(last == null) {
			last = first;
		// ce je v seznamu do sedaj bil samo en element
		} else if (last == first) {
			last = newEl;
		}
	}
	
	//Funkcija length() vrne dolzino seznama (pri tem ne uposteva glave seznama)
	int size() {
		// inicializiramo stevec
		int counter = 0;
		// deklariramo novo instanco CargoLinkedListElement
		CargoLinkedListElement el;
		// zacnemo pri elementu za glavo seznama
		el = first.next;
		// stevec povecujemo, dokler nismo dosegli konca seznama
		while(el != null) {
			counter++;
			// gremo na naslednji element
			el = el.next;
		}
		return counter;
	}
	
	// metoda, ki vrne element z indeksom index
	Cargo get(int index) {
		// deklrariramo novo instanco razreda CargoLinkedListElement
		CargoLinkedListElement el;
		
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
		// deklariramo dve instanci razreda CargoLinkedListElement
		CargoLinkedListElement el;
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
			// izlocimo nasledji element
			el.next = el.next.next;
			return true;
		} else {
			return false;
		}
	}
	
	// metoda, ki vrne zadnji element v vrsti, brez da ga odstrani
	public Cargo peekLast() {
		//ce je seznam prazen, vrnemo null
		if(this.isEmpty()) {
			return null;
		} else {
			// vrnemo zadnji element v vrsti
			return this.last.next.element;
		}
	}
	
	// metoda ki odstrani in vrne zadnji element v seznamu
	public Cargo removeLast() {
		Cargo result = null;
		// ce seznam ni prazen, pridobimo zadnji element
		if(!this.isEmpty()) {
			result = this.last.next.element;
		} else {
			return null;
		}
		
		// prevezemo kazalce
		this.last = this.last.previous;
		this.last.next.next = null;
		
		// vrnemo rezultat
		return result;
	}
	
	// metoda, ki vrne referenco na element first v tem seznamu
	public CargoLinkedListElement getFirstLinkedListElement() {
		return this.first;
	}
}