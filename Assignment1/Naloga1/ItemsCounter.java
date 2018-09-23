
// razred, ki se uporablja za indeksiranje predmetov na vhodnem platoju
// razlog je zmoznost, da ga v metodah lahko inkrementiramo preko reference
class ItemsCounter {
	// atribut - trenuten index
	private int index;
	
	// konstruktor - zacetni indeksa je 0
	public ItemsCounter() {
		this.index = 0;
	}
	
	// metoda za inkrementacijo indeksa
	public void increment() {
		this.index++;
	}
	
	// metoda za pridobitev trenutnega indeksa
	public int getIndex() {
		return this.index;
	}
}
