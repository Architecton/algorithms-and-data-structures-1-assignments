// razred, ki predstavlja ukaz v funkciji
class Command {
	// atributa - instrukcija in morebitni dodatni parameter ukaza FUN
	private String instruction;
	private int FUN_property;
	
	// konstruktor
	public Command(String instruction) {
		this.instruction = instruction;
		this.FUN_property = -1;
	}
	
	// konstruktor - overloaded
	public Command(String instruction, int FUN_property) {
		this.instruction = instruction;
		this.FUN_property = FUN_property;
	}
	
	// getter za instrukcijo
	public String getInstruction() {
		return this.instruction;
	}
	
	// getter za dodatni parameter ukaza FUN
	public int getFUN_property() {
		return this.FUN_property;
	}
}