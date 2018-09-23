import java.util.Scanner;

public class Naloga1 {
	// glavna metoda
	public static void main(String[] args) {
		// kreiranje objekta Scanner
		Scanner sc = new Scanner(System.in);
		
		// branje stevila trakov in dolzine trakov
		int numPorts = sc.nextInt();
		int portLength = sc.nextInt();
		
		// ustvarjanje seznama vrst, ki predstavljajo trake (z ustreznim stevilom vrst)
		//final List<ArrayDeque<String>> ports = new ArrayList<ArrayDeque<String>>(numPorts);
		final Deque[] ports = new Deque[numPorts];
		
		// inicializacija vrst v seznamu vrst, ki predstavlja nase trakove
		for(int i = 0; i < numPorts; i++) {
			ports[i] = new Deque();
		}
		
		// Kreiranje instance objekta vozicka
		Cart cart = new Cart();
		
		// Branje elementov na vhodnem platoju in dekompozicija v polje (locimo po vejici)
		String rawItems = sc.next();
		String[] items = rawItems.split(",");
		
		// kreiranje objekta stevca predmetov na vhodnem platoju - lahko ga spreminjamo v metodah preko njegove reference
		// potreben, ker vhodni plato ni predstavljen kot vrsta
		ItemsCounter itemsCounter = new ItemsCounter();
		
		// definicija in inicializacija spremenljivke, ki bo drzala stevilko traku v ukazu PREMIK
		int gotoPosition = -1;
		
		// "ukazna zanka"
		while(sc.hasNext()) {
			// branje naslednjega ukaza
			String instruction = sc.next();
			
			// ce je ukaz enak PREMIK, preberi se lokacijo premika
			if(instruction.equals("PREMIK")) {
				gotoPosition = sc.nextInt();
			}
			
			// izvrsevanje ustreznega ukaza z ustreznimi metodami
			switch(instruction) {
				case "PREMIK":
					//System.out.println("instruction == " + instruction + " " + gotoPosition);
					handlePREMIK(ports, numPorts, portLength, items, gotoPosition, cart);
					break;
				case "NALOZI":
					//System.out.println("instruction == " + instruction);
					handleNALOZI(ports, numPorts, portLength, items, itemsCounter, cart);
					break;
				case "ODLOZI":
					//System.out.println("instruction == " + instruction);
					handleODLOZI(ports, numPorts, portLength, items, cart);
					break;
				case "GOR":
					//System.out.println("instruction == " + instruction);
					handleGOR(ports, numPorts, portLength, items, cart);
					break;
				case "DOL":
					//System.out.println("instruction == " + instruction);
					handleDOL(ports, numPorts, portLength, items, cart);
			}
			// testni izpisi -------------------------------------------------------------------------
			//System.out.printf("cart position == %d, cart load == %s\n", cart.getCurrentPosition(), cart.getLoad());
			//System.out.println("Ports state:");
			//printPortsTesting(ports, numPorts, portLength, items, itemsCounter);
			// ---------------------------------------------------------------------------------------
		}
		// izpis koncnega stanja traku
		printPortsFinal(ports, numPorts);
		
		// zapiranje objekta sc razreda Scanner
		sc.close();
	}
	
	// metoda za obravnavo ukaza PREMIK
	private static void handlePREMIK(Deque[] ports, int numPorts, int portLength, String[] items, int gotoPosition, Cart cart) {
		// za neveljaven gotoPosition ne stori nicesar
		if(gotoPosition > numPorts) {
			return;
		}
		// spremeni trenutno mesto vozicka
		cart.setCurrentPosition(gotoPosition);
	}
	
	// metoda za obravnavo ukaza NALOZI
	private static void handleNALOZI(Deque[] ports, int numPorts, int portLength, String[] items, ItemsCounter itemsCounter, Cart cart) {
		// ce vozicek ni prazen, ne naredi nicesar
		if(cart.getLoad() != null) {
			return;
		// ce se vozicek nahaja na mestu 0, obravnavaj specificnost nalaganja iz tega mesta
		} else if(cart.getCurrentPosition() == 0) {
			// ce je vhodni plato prazen, ne stori nicesar
			if(itemsCounter.getIndex() >= items.length) {
				return;
			}
			cart.SetLoad(getFromitems(items, itemsCounter.getIndex()));
			itemsCounter.increment();
		// sicer
		} else {
			// ce je trenuten trak prazen ali pa na traku na prvem mestu ni predmeta, ne naredi nic
			if(ports[cart.getCurrentPosition() - 1].isEmpty() || 
					(!ports[cart.getCurrentPosition() - 1].isEmpty() && ports[cart.getCurrentPosition() - 1].peek().equals(""))) {
				return;
			}
			// sicer nastavi tovor vozicka na prvi element v traku
			cart.SetLoad(ports[cart.getCurrentPosition() - 1].pop());
			// odvzeti element na traku zamenjaj za prazni prostor
			ports[cart.getCurrentPosition() - 1].push("");
		}
	}
	
	// metoda za obravnavo ukaza ODLOZI
	private static void handleODLOZI(Deque[] ports, int numPorts, int portLength, String[] items, Cart cart) {
		// ce se vozicek nahaja na mestu 0 ali ce nima tovora, ne stori nicesar
		if(cart.getCurrentPosition() == 0 || cart.getLoad() == null || (!ports[cart.getCurrentPosition() - 1].isEmpty() && !ports[cart.getCurrentPosition() - 1].peek().equals(""))) {
			return;
		}
		// Ce je na prvem mestu na traku prazno mesto, se ga "znebi" in na to mesto polozi tovor	
		if(!ports[cart.getCurrentPosition() - 1].isEmpty() && ports[cart.getCurrentPosition() - 1].peek().equals("")) {
			ports[cart.getCurrentPosition() - 1].pop();
			ports[cart.getCurrentPosition() - 1].push(cart.getLoad());
			cart.SetLoad(null);
		// ce je vozicek na mestu 0 ali pa ce na traku ni prostora, ne stori nicesar
		} else if(ports[cart.getCurrentPosition() - 1].size() == portLength) {
			return;
		// sicer, odlozi tovor na trak
		} else {
			ports[cart.getCurrentPosition() - 1].push(cart.getLoad());
			cart.SetLoad(null);
		}
	}
	
	// metoda za obravnavo ukaza GOR
	private static void handleGOR(Deque[] ports, int numPorts, int portLength, String[] items, Cart cart) {
		// ce je trenuten trak prazen, ne stori nicesar
		if(ports[cart.getCurrentPosition() - 1].isEmpty()) {
			return;
		}
		// na trenutni trak "porini" prazno mesto
		ports[cart.getCurrentPosition() - 1].push("");
		// preveri, ce mora kaksen element odpasti - ce mora, ga odstrani
		if(ports[cart.getCurrentPosition() - 1].size() > portLength) {
			ports[cart.getCurrentPosition() - 1].removeLast();
			
			// pocisti morebitne "strlece" null vrednosti na koncu vrste
			while(ports[cart.getCurrentPosition() - 1].peekLast() != null && ports[cart.getCurrentPosition() - 1].peekLast().equals("")) {
				ports[cart.getCurrentPosition() - 1].removeLast();
			}
		}
	}
	
	// metoda za obravnavo ukaza DOL
	private static void handleDOL(Deque[] ports, int numPorts, int portLength, String[] items, Cart cart) {
		// ce je trenuten trak prazen, ne stori nicesar
		if(ports[cart.getCurrentPosition() - 1].isEmpty()) {
			return;
		}
		// sicer se znebimo cesarkoli na prvem mestu v traku
		ports[cart.getCurrentPosition() - 1].pop();
		
		// pocisti morebitne "strlece" null vrednosti na koncu vrste
		while(ports[cart.getCurrentPosition() - 1].peekLast() != null && ports[cart.getCurrentPosition() - 1].peekLast().equals("")) {
			ports[cart.getCurrentPosition() - 1].removeLast();
		}
	}
	
	// specificna metoda za pridobivanje stvari iz mesta 0
	private static String getFromitems(String[] items, int itemsIndex) {
		// vrni element iz vhodnega platoja, na katerega kaze itemsIndex, ki ga nato v klicoci metodi povecamo
		return items[itemsIndex];
	}
	
	// metoda za izpis koncnega stanja vrste
	private static void printPortsFinal(Deque[] ports, int numPorts) {
		// pojdi cez vse trakove
		for(int i = 0; i < numPorts; i++) {
			// izpisi zaporedno stevilko traka
			System.out.printf("%d:", i + 1);
			// dokler posamezen trak ni prazen, z njega pobiraj stvari in jih izpisi
			while(!ports[i].isEmpty()) {
				String item = ports[i].pop();
				System.out.printf((ports[i].isEmpty()) ? "%s" : "%s,", item);
			}
			// po izpisu vsakega traku pojdi v novo vrsto
			System.out.println();
		}
	}
	
}
