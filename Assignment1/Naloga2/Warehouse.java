import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// operiranje - posebna metoda za izstavljanje ukazov objektu Warehouse
class Warehouse implements Serializable {
	// atributi razreda
	private int numPorts;
	private int portLength;
	private Deque[] ports;
	private Cart cart;
	private boolean GORdeletion;
	private boolean DOLdeletion;
	// prejsni ukaz
	private String previousInstruction;
	// seznam do sedaj izvedenih ukazov
	private String commandList;

	// konstruktor
	public Warehouse(int numPorts, int portLength) {
		this.numPorts = numPorts;
		this.portLength = portLength;
		this.ports = new Deque[numPorts];
		for(int i = 0; i < this.ports.length; i++) {
			ports[i] = new Deque();
		}
		this.cart = new Cart();
		this.previousInstruction = "";
		this.commandList = "";
		this.GORdeletion = false;
		this.DOLdeletion = false;
	}
	
	// getters and setters
	public Deque[] getPorts() {
		return this.ports;
	}
	
	public int getCartPosition() {
		return this.cart.getCurrentPosition();
	}
	
	public String getCartLoad() {
		return this.cart.getLoad();
	}
	
	// Metoda za izvajanje ukazov
	public void parseInstruction(String instruction) {
		switch(instruction) {
		case "NALOZI":
			//System.out.println("instruction == " + instruction);
			handleNALOZI(ports, numPorts, portLength, cart);
			break;
		case "ODLOZI":
			//System.out.println("instruction == " + instruction);
			handleODLOZI(ports, numPorts, portLength, cart);
			break;
		case "GOR":
			//System.out.println("instruction == " + instruction);
			handleGOR(ports, numPorts, portLength, cart);
			break;
		case "DOL":
			//System.out.println("instruction == " + instruction);
			handleDOL(ports, numPorts, portLength, cart);
		}
		
		// ponastavimo niz, ki predstavlja prejsni ukaz
		this.previousInstruction = instruction;
		
		// dodamo obravnavan ukaz v listo obravnavanih ukazov
		this.commandList += instruction + '\n';
	}
	
	// overloaded metoda za izvajanje ukazov (za ukaz PREMIK)
	public void parseInstruction(String instruction, int gotoPosition) {
		handlePREMIK(ports, numPorts, portLength, gotoPosition, cart);

		// ponastavimo niz, ki predstavlja prejsni ukaz
		this.previousInstruction = instruction;
		
		// dodamo obravnavan ukaz v listo obravnavanih ukazov
		this.commandList += instruction + ' ' + gotoPosition + '\n';
	}
	
	// getter metoda za pridobivanje prejsnega ukaza
	public String getPreviousInstruction() {
		return this.previousInstruction;
	}

	// metoda za obravnavo ukaza PREMIK
	private void handlePREMIK(Deque[] ports, int numPorts, int portLength, int gotoPosition, Cart cart) {
		// za neveljaven gotoPosition ne stori nicesar
		if(gotoPosition > numPorts) {
			return;
		}
		// spremeni trenutno mesto vozicka
		cart.setCurrentPosition(gotoPosition);
	}
	
	// metoda za obravnavo ukaza NALOZI
	private void handleNALOZI(Deque[] ports, int numPorts, int portLength, Cart cart) {
		// ce vozicek ni prazen, ne naredi nicesar
		if(cart.getLoad() != null) {
			return;
		// ce se vozicek nahaja na mestu 0, obravnavaj specificnost nalaganja iz tega mesta
		} else {
			// ce je trenuten trak prazen ali pa na traku na prvem mestu ni predmeta, ne naredi nic
			if(ports[cart.getCurrentPosition() - 1].isEmpty() || 
					(!ports[cart.getCurrentPosition() - 1].isEmpty() && ports[cart.getCurrentPosition() - 1].peek().equals(""))) {
				return;
			}
			// sicer nastavi tovor vozicka na prvi element v traku
			cart.setLoad((String)ports[cart.getCurrentPosition() - 1].pop());
			ports[cart.getCurrentPosition() - 1].push("");
		}
	}
	
	//ports, numPorts, portLength, cart
	// metoda za obravnavo ukaza ODLOZI
	private void handleODLOZI(Deque[] ports, int numPorts, int portLength, Cart cart) {
		// ce se vozicek nahaja na mestu 0 ali ce nima tovora, ne stori nicesar
		if(cart.getCurrentPosition() == 0 || cart.getLoad() == null || (!ports[cart.getCurrentPosition() - 1].isEmpty() && !ports[cart.getCurrentPosition() - 1].peek().equals(""))) {
			return;
		}
		// Ce je na prvem mestu na traku prazno mesto, se ga "znebi" in na to mesto polozi tovor	
		if(!ports[cart.getCurrentPosition() - 1].isEmpty() && ports[cart.getCurrentPosition() - 1].peek().equals("")) {
			ports[cart.getCurrentPosition() - 1].pop();
			ports[cart.getCurrentPosition() - 1].push(cart.getLoad());
			cart.setLoad(null);
		// ce je vozicek na mestu 0 ali pa ce na traku ni prostora, ne stori nicesar
		} else if(ports[cart.getCurrentPosition() - 1].size() == portLength) {
			return;
		// sicer, odlozi tovor na trak
		} else {
			ports[cart.getCurrentPosition() - 1].push(cart.getLoad());
			cart.setLoad(null);
		}
	}
	
	// metoda za obravnavo ukaza GOR
	private void handleGOR(Deque[] ports, int numPorts, int portLength, Cart cart) {
		// ce je trenuten trak prazen, ne stori nicesar
		if(ports[cart.getCurrentPosition() - 1].isEmpty()) {
			this.GORdeletion = false;
			return;
		}
		// na trenutni trak "porini" prazno mesto
		ports[cart.getCurrentPosition() - 1].push("");
		// preveri, ce mora kaksen element odpasti - ce mora, ga odstrani
		if(ports[cart.getCurrentPosition() - 1].size() > portLength) {
			ports[cart.getCurrentPosition() - 1].removeLast();
			this.GORdeletion = true;
			//pocisti morebitne "strlece" null vrednosti na koncu vrste
			while(ports[cart.getCurrentPosition() - 1].peekLast() != null && ports[cart.getCurrentPosition() - 1].peekLast().equals("")) {
				ports[cart.getCurrentPosition() - 1].removeLast();
			}
		} else {
			this.GORdeletion = false;
		}
	}
	
	// metoda za obravnavo ukaza DOL
	private void handleDOL(Deque[] ports, int numPorts, int portLength, Cart cart) {
		// ce je trenuten trak prazen, ne stori nicesar
		if(ports[cart.getCurrentPosition() - 1].isEmpty()) {
			this.DOLdeletion = false;
			return;
		}
		// sicer se znebimo cesarkoli na prvem mestu v traku
		String res = ports[cart.getCurrentPosition() - 1].pop();
		if(res.equals("")) {
			this.DOLdeletion = false;
		} else {
			this.DOLdeletion = true;
		}
		// pocistimo morebitna prazna mesta na koncu traku
		while(ports[cart.getCurrentPosition() - 1].peekLast() != null && ports[cart.getCurrentPosition() - 1].peekLast().equals("")) {
			ports[cart.getCurrentPosition() - 1].removeLast();
		}
	}
	
	public boolean getDOLdeletion() {
		return this.DOLdeletion;
	}
	
	public boolean getGORdeletion() {
		return this.GORdeletion;
	}
	
	public static Warehouse deepCopy(Warehouse oldObj) throws Exception {
		// deklariramo vhodna tokova
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			// definiramo ObjectOutputStream, ki pise v ByteArrayOutputStream
			oos = new ObjectOutputStream(bos);
			// v bos zapisemo objekt oldObj
			oos.writeObject(oldObj);
			// zapisemo vso morebitno vsebino medpomnilnika
			oos.flush();
			// beremo iz tabele bajtov, v katero smo zapisali vsebino objekta
			ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
			// definiramo nov tok, za input objekta, ki ga bomo prebrali iz tabele bos
			ois = new ObjectInputStream(bin);
			// v ois preberemo objekt in ga castamo v ustrezen tip, ter ga vrnemo
			return (Warehouse)ois.readObject();
		// ulovimo izjeme
		} catch(Exception e) {
			// izjemo podamo visje v klicoco metodo
			throw(e);
		// v vsakem primeru zapremo tokova
		} finally {
	         oos.close();
	         ois.close();
		}
	}
	
	// getter za seznam dosedaj izvedenih ukazov
	public String getCommandList() {
		return this.commandList;
	}
	
	// metoda ki preveri, ce dva objekta tega razreda predstavljata isti konfiguraciji skladisc
	@Override
	public boolean equals(Object warehouse) {
		// kreiramo instanco razreda, ki ga zelimo primerjati s tem razredom
		Warehouse toCompare = (Warehouse)warehouse;
		// ce je ta instanca kar ta objekt
		if(warehouse == this){
			return true;
		}
		// sicer primerjamo kljucne atribute in izpis metode toString
		if(this.getCartLoad() == toCompare.getCartLoad() && this.getCartPosition() == toCompare.getCartPosition() &&
				this.toString().equals(toCompare.toString())) {
			return true;
		}
		return false;
	}
	
	// metoda za izpis stanja trakov tega objekta, ki predstavlja skladisce
	@Override
	public String toString() {
		try {
			Warehouse printCopy = Warehouse.deepCopy(this);
			// deklariramo in inicializiramo niz
			String result = "";
			for(int i = 0; i < numPorts; i++) {
				// dodamo zaporedno stevilko traka
				result += (i + 1) + ":";
				//System.out.printf("%d:", i + 1);
				// dokler posamezen trak ni prazen, z njega pobiraj stvari in jih dodajaj v niz
				while(!printCopy.getPorts()[i].isEmpty()) {
					String item = (String)printCopy.getPorts()[i].pop();
					result += (printCopy.getPorts()[i].isEmpty()) ? item : item + ",";
					//System.out.printf((ports[i].isEmpty()) ? "%s" : "%s,", item);
				}
				// po dodajanju vsakega traku dodaj znak za novo vrstico
				result += '\n';
			}
			return result;
		} catch(Exception e) {
			return null;
		}
	}
	
	// metoda za generiranje izpisa tega razreda, ki ga uporabljamo za odstranjevanje redundantnih kopij
	public String toStringMem() {
		try {
			Warehouse printCopy = Warehouse.deepCopy(this);
			// deklariramo in inicializiramo niz
			String result = "";
			// v izpis dodamo trenutno pozicijo in trenutni tovor vozicka
			result += this.cart.getCurrentPosition();
			result += this.cart.getLoad();
			for(int i = 0; i < numPorts; i++) {
				// dodamo zaporedno stevilko traka
				result += (i + 1) + ":";
				//System.out.printf("%d:", i + 1);
				// dokler posamezen trak ni prazen, z njega pobiraj stvari in jih dodajaj v niz
				while(!printCopy.getPorts()[i].isEmpty()) {
					String item = printCopy.getPorts()[i].pop();
					result += (printCopy.getPorts()[i].isEmpty()) ? item : item + ",";
					//System.out.printf((ports[i].isEmpty()) ? "%s" : "%s,", item);
				}
				// po dodajanju vsakega traku dodaj znak za novo vrstico
				result += '\n';
			}
			return result;
		} catch(Exception e) {
			return null;
		}
	}
	
	// metoda, ki preveri, ce ta objekt, ki predstavlja skladisce vsebuje predmet toFind
	public boolean containsItem(String toFind) {
		// najprej preverimo, ce je predmet nalozen na vozicku
		if(this.cart.getLoad() != null && this.cart.getLoad().equals(toFind)) {
			return true;
		}
		// gremo cez vse trakove
		for(int i = 0; i < this.ports.length; i++) {
			DequeElement el = this.ports[i].getFirstElement();
			// gremo cez vse predmete na traku
			while(el != null) {
				if(el.element.equals(toFind)) {
					return true;
				}
				el = el.next;
			}
		}
		return false;
	}
	
	// metoda, ki preveri, ce je trak z indeksom i ze pravilno sortiran
	// (primerjamo z znano pravilno urejenostjo)
	public boolean isSorted(String resultLine, int i) {
		// naredimo izpis trenutnega traku
		resultLine += ',';
		String stringPort = (i + 1) + ":";
		DequeElement el = this.ports[i].getFirstElement();
		while(el != null) {
			stringPort += el.element + ',';
			el = el.next;
		}
		// primerjamo ga s podano znano pravilno urejenostjo traku z indeksom i
		if(resultLine.equals(stringPort)) {
			return true;
		} else {
			return false;
		}
	}
}