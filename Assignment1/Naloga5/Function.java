// razred, ki predstavlja funkcijo v programu robota
class Function {
	// atributa - seznam ukazov in indeks ukaza znotraj polja ukazov
	private Command[] commands;
	private int index;
	
	// konstruktor
	public Function(int numCommands) {
		this.commands = new Command[numCommands];
		this.index = 0;
	}
	
	// getter za seznam ukazov
	public Command[] getCommands() {
		return this.commands;
	}
	
	// metoda za dodajanje ukaza v seznam ukazov
	public void addCommand(Command command) {
		this.commands[index] = command;
		this.index++;
	}
}