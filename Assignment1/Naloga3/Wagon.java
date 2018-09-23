// razred, ki predstavlja vagon
class Wagon {
	// atributa, ki predstavljata maksimalno tezo tovora na vagonu in tovor na vagonu
	private int maxWeight;
	private CargoLinkedList cargo;
	private int numCargoTypes;
	private int totalCargoWeight;
	
	// konstruktor
	public Wagon(int maxWeight) {
		this.maxWeight = maxWeight;
	}
	
	// konstruktor (ce imamo vec podatkov)
	public Wagon(int maxWeight, CargoLinkedList cargo, int numCargoTypes, int totalCargoWeight) {
		this.maxWeight = maxWeight;
		this.cargo = cargo;
		this.numCargoTypes = numCargoTypes;
		this.totalCargoWeight = totalCargoWeight;
	}
	
	// getters in setters
	public void setMaxWeight(int maxWeight) {
		this.maxWeight = maxWeight;
	}
	
	public int getMaxWeight() {
		return this.maxWeight;
	}
	
	public void setCargo(CargoLinkedList cargo) {
		this.cargo = cargo;
	}
	
	public CargoLinkedList getCargo() {
		return this.cargo;
	}
	
	public int getNumCargoTypes() {
		return this.numCargoTypes;
	}
	
	public int getTotalCargoWeight() {
		return this.totalCargoWeight;
	}
	
	/* poglej, ce tovor na vagonu vsebuje tovor opisan z imenom find in vrni index tega tovora v seznamu tovora
	ce tovora ne najdes, vrni -1 */
	public int containsCargo(String find) {
		// ustvarimo instanco razreda CargoLinkedListElement
		CargoLinkedListElement el = this.cargo.first.next;
		// stevec, ki steje indekse pregledanih elementov
		int index = 0;
		// dokler nismo prisli do konca seznama
		while(el != null) {
			// ce se tip tovora ujema z iskanim tipom, vrnemo indeks
			if(el.element.getCargoType().equals(find)) {
				return index; 
			}
			el = el.next;
			index++;
		}
		// ce nismo nasli iskanega tipa tovora, vrnemo signalno vrednost -1
		return -1;
	}
	
	// vrni true, ce je teza tovora na vagonu prekoracuje maksimalno dovoljeno obremenitev vagona, sicer vrni false
	public boolean exceedsMaxWeight() {
		if(this.totalCargoWeight > this.maxWeight) {
			return true;
		}
		return false;
	}
	
	// metoda za dodajanje teze na ta vagon
	public void addCargoWeight(int weight) {
		this.totalCargoWeight += weight;
	}
	
	// metoda za odstranjevanje teze s tega vagona
	public void removeCargoWeight(int weight) {
		this.totalCargoWeight -= weight;
	}
	
	// metoda za inkrementacijo stevila tipov tovora na posameznem vagonu
	public void incremenetNumCargoTypes() {
		(this.numCargoTypes)++;
	}
	
	// metoda za dekrementacijo stevila tipov tovora na posameznem vagonu
	public void decrementNumCargoTypes() {
		(this.numCargoTypes)--;
	}
}