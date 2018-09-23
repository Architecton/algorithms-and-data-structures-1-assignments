import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class LinkedListStack implements Serializable {
	protected LinkedListStackElement first;
	
	// konstruktor, ki inicializira nov LinkedList
	LinkedListStack() {
		makenull();
	}
	
	// Funkcija makenull. inicializira seznam
	public void makenull() {
		first = new LinkedListStackElement(null, null);
	}
	
	// metoda ki vrne true, ce je ta seznam prazen
	public boolean isEmpty() {
		// ce "last" kaze na null, je seznam prazen
		if(this.first.next == null) {
			return true;
		} else {
			return false;
		}
	}
	
	// metoda, ki vrne vsebino prvega elementa v skladu brez da ga odstrani
	public Object peek() {
		// ce je sklad prazen
		if(this.isEmpty()) {
			return null;
		}
		// sicer vrnemo vsebino elementa za glavo
		return this.first.next.element;
	}
	
	// metoda, ki pridobi vsebino prvega elementa v skladu in ga odstrani
	public CallStackElement pop() {
		// pridobimo element, ki ga bomo odstranili (ce seznam ni prazen)
		CallStackElement result = null;
		if(!this.isEmpty()) {
			result = this.first.next.element;
		} else {
			return null;
		}
		// odstranimo element - klicemo funkcijo remove(int index)
		this.removeFirst();
		return result;
	}
	
	// Funkcija push doda nov element na ta sklad (takoj za glavo seznama, ki predstavlja sklad)
	void push(CallStackElement obj) {
		// najprej naredimo nov element
		LinkedListStackElement newEl = new LinkedListStackElement(obj);
		
		// nov element postavimo za glavo seznama, ki predstavlja sklad
		newEl.next = first.next;
		first.next = newEl;
	}
	
	// metoda, ki izbrise prvi element na skladu
	private boolean removeFirst() {
		// kreiramo novo instanco razreda LinkedListStackElement
		LinkedListStackElement el;
		// el kaze na zacetku kaze na glavo seznama, ki predstavlja sklad
		el = this.first;
		// ce za glavo obstaja element (sklad ni prazen),
		if(el.next != null) {
			// izlocimo nasledji element
			el.next = el.next.next;
			return true;
		// sicer vrnemo false
		} else {
			return false;
		}
	}
	
	// metoda za za ustvarjanje globoke kopije tega objekta (kopiramo tudi vse vsebovanje objekte)
	static public LinkedListStack deepCopy(LinkedListStack oldObj) throws Exception {
		// deklariramo vhodna tokova
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// definiramo ObjectOutputStream, ki pise v ByteArrayOutputStream
			oos = new ObjectOutputStream(bos);
			// v bos zapisemo objekt oldObj
			oos.writeObject(oldObj);
			// zapisemo vso morebitno vsebino medpomnilnika
			oos.flush();
			// beremo iz tabele bajtov, v katero smo zapisali vsebino objekta
			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
			// definiramo nov tok, za input objekta, ki ga bomo prebrali iz tabele bos
			ois = new ObjectInputStream(bin);
			// v ois preberemo objekt in ga castamo v ustrezen tip, ter ga vrnemo
			return (LinkedListStack)ois.readObject();
		// ulovimo izjeme
		} catch(Exception e) {
			// izjemo podamo visje v klicoco metodo
			throw(e);
		// v vsakem primeru zapremo tokova
		} finally {
	         oos.close();
	         ois.close();
		}
	}
}