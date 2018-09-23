// razred, ki predstavlja kontekst izvajanja
// v objekt tega razreda shranjujemo trenutni klicni sklad ter tudi trenutno funckijo
class ExecutionContext {
	private LinkedListStack callStack;
	
	// konstruktor
	public ExecutionContext(LinkedListStack callStack) {
		this.callStack = callStack;
	}
	
	// getter za sklad shranjen v tem kontekstu izvajanja
	public LinkedListStack getCallStack() {
		return this.callStack;
	}
}