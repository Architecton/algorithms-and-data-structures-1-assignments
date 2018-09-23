// razred, ki predstavlja vagon
class Wagon {
	
	// atributa, ki predstavljata maksimalno tezo tovora na vagonu,
	// trenutno tezo tovora na vagonu in tovor na vagonu
	private int maxWeight;
	private int totalWeight;
	private CargoLinkedList cargo;
	
	// konstruktor
	public Wagon(int maxWeight) {
		this.maxWeight = maxWeight;
		this.cargo = new CargoLinkedList();
	}
	
	// metode razreda
	
	// metoda za dodajanje tovora na ta vagon (dodamo tovor in povecamo skupno tezo tovora na tem vagonu)
	public void addCargo(Cargo cargo) {
		this.cargo.add(cargo);
		this.totalWeight += cargo.getCargoWeight();
	}
	
	// metoda za dodajanje tovora na ta vagon, ce imamo podan seznam tovora
	public void addAllCargo(CargoLinkedList cargo) {
		CargoLinkedListElement el = cargo.getFirstLinkedListElement().next;
		while(el != null) {
			this.addCargo(el.element);
			el = el.next;
		}
	}
	
	// getters in setters
	public int getMaxWeight() {
		return this.maxWeight;
	}
	
	public int getTotalWeight() {
		return this.totalWeight;
	}
	
	public CargoLinkedList getCargo(){
		return this.cargo;
	}
	
	@Override
	public String toString() {
		return "Total weight == " + this.totalWeight;
	}
}