// razred, ki predstavlja tovor na posameznem vagonu
class Cargo {
	// atributa, ki predstavljata tip tovor in tezo tovora
	private String cargoType;
	private int cargoWeight;
	
	// konstruktor
	public Cargo(String cargoType, int cargoWeight) {
		this.cargoType = cargoType;
		this.cargoWeight = cargoWeight;
	}
	
	// getters and setters
	public void setCargoType(String cargoType) {
		this.cargoType = cargoType;
	}
	
	public String getCargoType() {
		return this.cargoType;
	}
	
	public void setCargoWeight(int cargoWeight) {
		this.cargoWeight = cargoWeight;
	}
	
	public int getCargoWeight() {
		return this.cargoWeight;
	}
	
	@Override
	public String toString() {
		return "Type == " + this.cargoType + ", weight == " + this.cargoWeight;
	}
}