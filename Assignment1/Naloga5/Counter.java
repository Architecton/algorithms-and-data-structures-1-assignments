// razred, ki predstavlja stevec, ki ga lahko metodam podamo po referenci
class Counter {
	// atribut - trenutna vrednost
	private int value;
	
	// konstruktor
	public Counter() {
		this.value = 0;
	}
	
	// getter za trentno vrednost
	public int getValue() {
		return this.value;
	}
	
	// metoda za inkrementacijo vrednosti stevca
	public void increment() {
		(this.value)++;
	}
}