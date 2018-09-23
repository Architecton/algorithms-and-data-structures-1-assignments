import java.util.Scanner;

public class Naloga2 {
	static boolean[] hashMem = new boolean[1000000];
	public static void main(String[] args) {
		
		// kreiranje instance razreda Scanner za branje podatkov
		Scanner sc = new Scanner(System.in);
	
		// preberemo podatke o skladiscu
		int numPorts, portsLength;
		numPorts = sc.nextInt();
		portsLength = sc.nextInt();
		
		// kreiramo izhodiscno instanco razreda skladisce z zacetno urejenostjo
		Warehouse initialWarehouse = new Warehouse(numPorts, portsLength);
		
		// preberemo podatke o trakovih ter jih vnesemo v izhodiscno instanco skladisca
		String[] port;
		// gremo cez vse trakove
		for(int i = 0; i < numPorts; i++) {
			// preberemo trak in podatke locimo po ","
			port = sc.next().split(",");
			
			// iz prve celice odstranimo oznako traku
			port[0] = port[0].substring(2, port[0].length());
			if(port.length == 1 && port[0].equals("")) {
				continue;
			}
			// v ustrezni trak v instanci skladisca vnesemo prebrano zacetno vsebino traku
			for(int j = port.length - 1; j >= 0; j--) {
				initialWarehouse.getPorts()[i].push(port[j]);
			}
		}
		// tabela nizov, ki hrani pricakovane ureditve trakov (za vsak trak posebej)
		String[] resultByLines = new String[numPorts];
		// dvosmerna vrsta, ki vsebuje elemente, ki se nahajajo v ciljni ureditvi
		Deque goalItems = new Deque();
		// v niz preberemo pricakovano ureditev skladisca
		String goalComposition = "";
		for(int i = 0; i < numPorts; i++) {
			// preberemo trak in podatke locimo po ","
			port = sc.next().split(",");
			port[0] = port[0].substring(2, port[0].length());
			String lineResult = "";
			goalComposition += (i + 1) + ":";
			lineResult += (i + 1) + ":";
			// v ustrezni trak v instanci skladisca vnesemo prebrano zacetno vsebino traku
			// zacetna celica port vsebuje oznako traku in jo izpustimo
			for(int j = 0; j < port.length; j++) {
				goalComposition += ((j == port.length - 1) ? port[j] + "\n" : port[j] + ",");
				lineResult += ((j == port.length - 1) ? port[j] : port[j] + ",");
				// v vrsto pricakovanih elementov damo trenutni predmet
				if(!port[j].equals("")) {
					goalItems.push(port[j]);
				}
			}
			// v trenutno linijo shranimo rezultat
			resultByLines[i] = lineResult;
		}
		
		// kreiramo novo instanco razreda Queue, kamor shranjujemo naslednje poteze
		Queue queue = new Queue();
		
		// v vrsto dodamo izhodiscno ureditev skladisca
		queue.add(initialWarehouse);
		
		// dokler vrsta ni prazna pregledamo vse naslednje poteze, ki se nahajajo v vrsti
		// za vsako potezo, ki ni pripeljala do ciljne ureditve, zgeneriramo nove poteze
		while(!queue.isEmpty()) {
			Warehouse warehouse = queue.remove();
			
			/*for(int i = 0; i < numPorts; i++) {
				while(warehouse.getPorts()[i].peekLast() != null && warehouse.getPorts()[i].peekLast().equals("")) {
					warehouse.getPorts()[i].removeLast();
				}
			}*/
			
			// preverimo, ce smo prisli do ureditve, ki smo jo dobili ze prej, z manj ukazi (redundanca)
			if(hashMem[Math.abs(warehouse.toStringMem().hashCode()) % hashMem.length] == true) {
				continue;
			} else {
				hashMem[Math.abs(warehouse.toStringMem().hashCode()) % hashMem.length] = true;
			}
			
			// preverimo, ce kompozicija ne vsebuje katerega od predmetov, ki so v ciljni ureditvi (redundanca)
			boolean redundant = false;
			DequeElement el = goalItems.getFirstElement();
			// gremo cez vse elemente v ciljni ureditvi
			while(el != null) {
				// ce trenutna ureditev ne vsebuje tega elementa, je redundantna
				if(!warehouse.containsItem(el.element)) {
					redundant = true;
					break;
				}
				el = el.next;
			}
			// ce je ureditev redundantna, gremo naprej
			if(redundant) {
				continue;
			}
			
			// ce ureditev ni prava, generiramo vse smiselne nadaljne ukaze in jih damo v vrsto
			
			// ce prejsni ukaz ni bil tipa PREMAKNI, generiramo vse smiselne premike in jih damo v vrsto
			if(!warehouse.getPreviousInstruction().equals("PREMIK")) {
				// gremo cez vse mozne pozicije vozicka
				for(int i = 1; i <= numPorts; i++) {
					// ce nismo pri poziciji, kjer je trenutno vozicek
					if(warehouse.getCartPosition() != i && !warehouse.isSorted(resultByLines[i - 1], i - 1)) {
						try {
						// ustvarimo globoko kopijo trenutnega skladisca
						Warehouse generatedWarehouse = Warehouse.deepCopy(warehouse);
						
						// ce se s praznim vozickom zelimo premakniti na prazen trak, tak premik ni smiselen in ga preskocimo
						if(warehouse.getCartLoad() == null && warehouse.getPorts()[i - 1].isEmpty()) {
							continue;
						}
						// kopiji skladisca izstavimo ukaz premik z ustrezno lego
						generatedWarehouse.parseInstruction("PREMIK", i);
						if(generatedWarehouse.toString().equals(goalComposition)) {
							printResult(generatedWarehouse);
						}
						// kopijo skladisca dodamo v vrsto
						queue.add(generatedWarehouse);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			// ce trak, kjer je trenutno vozicek ni prazen (obravnava ukazov GOR in DOL)
			if(warehouse.getCartPosition() != 0 && !warehouse.getPorts()[warehouse.getCartPosition() - 1].isEmpty()) {
				try {
					// generiramo kopijo trenutnega skladisca in ji izstavimo ukaz GOR
					// samo v primeru, ce prejsni ukaz ni bil DOL, ki ni izbrisal nobenega elementa
					
					if(!warehouse.getPreviousInstruction().equals("DOL") || warehouse.getDOLdeletion()) {
						Warehouse generatedWarehouse = Warehouse.deepCopy(warehouse);
						generatedWarehouse.parseInstruction("GOR");
						if(generatedWarehouse.toString().equals(goalComposition)) {
							printResult(generatedWarehouse);
						}
						// kopijo skladisca damo v vrsto
						queue.add(generatedWarehouse);
					}
					
					// generiramo kopijo trenutnega skladisca in ji izstavimo ukaz DOL
					// samo v primeru, ce prejsni ukaz ni bil GOR, ki ni izbirsal nobenega elementa
					if(!warehouse.getPreviousInstruction().equals("GOR") || warehouse.getGORdeletion()) {
						Warehouse generatedWarehouse2 = Warehouse.deepCopy(warehouse);
						generatedWarehouse2.parseInstruction("DOL");
						// preverimo pravilnost konfiguracije
						if(generatedWarehouse2.toString().equals(goalComposition)) {
							printResult(generatedWarehouse2);
						}
						// kopijo skladisca damo v vrsto
						queue.add(generatedWarehouse2);
					}
					
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			// obravnava ukaza NALOZI
			if(!warehouse.getPreviousInstruction().equals("GOR") && warehouse.getCartPosition() != 0 && !warehouse.getPreviousInstruction().equals("ODLOZI") && !warehouse.getPorts()[warehouse.getCartPosition() - 1].isEmpty() && !warehouse.getPorts()[warehouse.getCartPosition() - 1].peek().equals("")) {
				try {
					Warehouse generatedWarehouse = Warehouse.deepCopy(warehouse);
					generatedWarehouse.parseInstruction("NALOZI");
					if(generatedWarehouse.toString().equals(goalComposition)) {
						printResult(generatedWarehouse);
					}
					queue.add(generatedWarehouse);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			// obravnava ukaza ODLOZI
			if(warehouse.getCartPosition() != 0 && !warehouse.getPreviousInstruction().equals("NALOZI") && warehouse.getCartLoad() != null && (warehouse.getPorts()[warehouse.getCartPosition() - 1].isEmpty() || warehouse.getPorts()[warehouse.getCartPosition() - 1].peek().equals(""))) {
				try {
					Warehouse generatedWarehouse = Warehouse.deepCopy(warehouse);
					generatedWarehouse.parseInstruction("ODLOZI");
					if(generatedWarehouse.toString().equals(goalComposition)) {
						printResult(generatedWarehouse);
					}
					queue.add(generatedWarehouse);
				} catch(Exception e) {
					e.printStackTrace();
				}				
			}
		}
		sc.close();
	}
	
	// koncni izpis ukazov, ki so vodili do pravilne ureditve
	public static void printResult(Warehouse warehouse) {
		System.out.print(warehouse.getCommandList());
		System.exit(0);
	}
}