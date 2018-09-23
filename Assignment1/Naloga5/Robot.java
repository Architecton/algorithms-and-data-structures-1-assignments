// razred, ki predstavlja robota
class Robot {
	// atributi
	private int xPosition;
	private int yPosition;
	private int orientation;
	
	// konstruktor
	public Robot(int xPosition, int yPosition, int orientation) {
		this.xPosition = xPosition;
		this.yPosition = yPosition;
		this.orientation = orientation;
	}
	
	// getter za x koordinato trenutne lege robota
	public int getXPosition() {
		return this.xPosition;
	}
	
	// getter za y koordinato trenutne lege robota
	public int getYPosition() {
		return this.yPosition;
	}
	
	// metoda za inkrementacijo x koordinate robota
	public void incrementXPosition() {
		(this.xPosition)++;
	}
	
	// metoda za inkrementacijo y koordinate robota
	public void incrementYPosition() {
		(this.yPosition)--;
	}
	
	// metoda za dekrementacijo x lege robota
	public void decrementXPosition() {
		(this.xPosition)--;
	}
	
	// metoda za dekrementacijo y lege robota
	public void decrementYPosition() {
		(this.yPosition)++;
	}
	
	// getter za trenutno orientacijo robota
	public int getOrientation() {
		return this.orientation;
	}
	
	// metoda za rotacijo robota v desno
	public void rotateRight() {
		this.orientation = (++(this.orientation)) % 4;
	}
	
	// metoda za rotacijo robota v levo
	public void rotateLeft() {
		switch(this.orientation) {
			case 0:
				this.orientation = 3;
				break;
			case 1:
				this.orientation = 0;
				break;
			case 2:
				this.orientation = 1;
				break;
			case 3:
				this.orientation = 2;
				break;
		}
	}
}