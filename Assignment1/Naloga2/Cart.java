import java.io.Serializable;

// Deklaracija razreda, ki predstavlja vozicek
class Cart implements Serializable {
	// atributi - tovor in trenutna pozicija vozicka
	private String load;
	private int currentPosition;
	// konstruktor - spocetka vozicek nima tovora in se nahaja na poziciji 0
	public Cart() {
		this.load = null;
		this.currentPosition = 0;
	}
	
	// getters and setters
	public int getCurrentPosition() {
		return this.currentPosition;
	}
	
	public void setCurrentPosition(int position) {
		this.currentPosition = position;
	}
	
	public String getLoad() {
		return this.load;
	}
	
	public void setLoad(String load) {
		this.load = load;
	}
}