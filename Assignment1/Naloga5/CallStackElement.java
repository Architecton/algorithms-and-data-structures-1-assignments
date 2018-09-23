import java.io.Serializable;

// razred, ki predstavlja element na klicnem skladu
class CallStackElement implements Serializable {
	// atributa - indeks funkcije in indeks ukaza znotraj funkcije
	private int functionIndex;
	private int executionIndex;
	
	// konstruktor
	public CallStackElement(int indexFunction, int commandIndex) {
		this.functionIndex = indexFunction;
		this.executionIndex = commandIndex;
	}
	
	// getter za indeks ukaza
	public int getExecutionIndex() {
		return this.executionIndex;
	}
	
	// getter za indeks funkcije
	public int getFunctionIndex() {
		return this.functionIndex;
	}

	// metoda toString za generiranje testnih izpisov
	@Override
	public String toString() {
		String result = "function index == " + this.functionIndex + " execution index == " + this.executionIndex;
		return result;
	}
}