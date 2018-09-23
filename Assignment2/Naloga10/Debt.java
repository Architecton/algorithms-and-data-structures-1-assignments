// razred Debt, ki predstavlja dolg med dvema osebama
class Debt {
	// atributi
	private String personA;
	private int indexA;
	private String personB;
	private int indexB;
	private int value;
	
	// konstruktor
	public Debt(String personA, String personB, int value) {
		this.personA = personA;
		this.personB = personB;
		this.value = value;
		// izracun indeksov oseb iz njihovih oznak (preberemo stevilo za vodilnim 'v')
		this.indexA = Integer.parseInt(personA.substring(1));
		this.indexB = Integer.parseInt(personB.substring(1));
	}
	
	// getters in setters
	public int getIndexA() {
		return this.indexA;
	}
	
	public int getIndexB() {
		return this.indexB;
	}
	
	public int getValue() {
		return this.value;
	}
	
	
}
