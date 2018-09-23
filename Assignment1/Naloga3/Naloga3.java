import java.util.Scanner;

public class Naloga3 {
	public static void main(String[] args) {
		// kreiranje instance razreda Scanner za branje podatkov
		Scanner sc = new Scanner(System.in);
		
		// branje maksimalne teze lokomotive in stevila vagonov
		int maxTrainWeight = sc.nextInt();
		int numWagons = sc.nextInt();
		
		// kreiranje instance objekra LinkedList, ki bo predstavljal lokomotivo
		LinkedList train = new LinkedList();
		
		// --- branje podatkov o kompoziciji vlaka -----------------------
		
		// Vnos podatkov o kompozicija vlaka v glavo seznama
		// branje podatkov o vagonih
		for(int i = 0; i < numWagons; i++) {
			
			// branje najvecje teze vagona
			int maxWagonWeight = sc.nextInt();
			
			// branje stevila razlicnih tipov tovora na vagonu
			int numCargo = sc.nextInt();
			
			// stevec teze vsega tovora vagonu
			int totalCargoWeight = 0;
			
			//ustvarjanje tabele objektov Cargo in branje podatkov o tovoru
			CargoLinkedList cargo = new CargoLinkedList();
			for(int j = 0; j < numCargo; j++) {
				String itemName = sc.next();
				int itemWeight = sc.nextInt();
				totalCargoWeight += itemWeight;
				cargo.add(new Cargo(itemName, itemWeight));
			}
			// dodajanje novega vagona v kompozicijo vlaka (na konec kompozicije)
			train.add(new Wagon(maxWagonWeight, cargo, numCargo, totalCargoWeight));
		}
		// --- KONEC branja podatkov o kompoziciji vlaka --------------
		
		// definicija in inicializacija dodatnih parametrov posameznih ukazov
		int ODSTRANI_HET_property = 0;
		int ODSTRANI_ZAS_property = 0;
		String PREMAKNI_tip = "";
		int PREMAKNI_vagon1 = 0;
		int PREMAKNI_vagon2 = 0;
		
		// "ukazovna zanka"
		while(sc.hasNext()) {
			// branje ukaza
			String instruction = sc.next();
			
			// branje morebitnih dodatnih parametrov ukaza
			if(instruction.equals("ODSTRANI_HET")) {
				ODSTRANI_HET_property = sc.nextInt();
			}
			if(instruction.equals("ODSTRANI_ZAS")) {
				ODSTRANI_ZAS_property = sc.nextInt();
			}
			if(instruction.equals("PREMAKNI")) {
				PREMAKNI_tip = sc.next();
				PREMAKNI_vagon1 = sc.nextInt();
				PREMAKNI_vagon2 = sc.nextInt();
			}
			// izvrsevanje ustreznega ukaza
			switch(instruction) {
				case "ODSTRANI_LIHE":
					handle_ODSTANI_LIHE(train);
					break;
				case "ODSTRANI_HET":
					handle_ODSTANI_HET(train, ODSTRANI_HET_property);
					break;
				case "ODSTRANI_ZAS":
					handle_ODSTRANI_ZAS(train, ODSTRANI_ZAS_property);
					break;
				case "OBRNI":
					handle_OBRNI(train);
					break;
				case "UREDI":
					handle_UREDI(train);
					break;
				case "PREMAKNI":
					handle_PREMAKNI(train, PREMAKNI_tip, PREMAKNI_vagon1, PREMAKNI_vagon2);
					break;
			}
			
		}
		// --- testni izpis kompozicije vlaka
		printComposition(maxTrainWeight, train);
		// --- KONEC testnega izpisa kompozicije vlaka ----------------
		sc.close();
	}
	
	// ------------- metode za obravnavo ukazov----------------------
	
	// metoda za obravnavavo ukaza ODSTRANI_LIHE
	public static void handle_ODSTANI_LIHE(LinkedList train) {
		int removalCounter = 0;
		// gremo cez vagone z lihimi indeksi in jih odstranimo
		for(int i = 0; i < train.size(); i++) {
			int originalIndex = i + removalCounter;
			if(originalIndex % 2 != 0) {
				train.remove(i);
				removalCounter++;
			}
		}
	}
	
	// metoda za obravnavo ukaza ODSTRANI_HET
	public static void handle_ODSTANI_HET(LinkedList train, int ODSTRANI_HET_property) {
		// gremo cez vse vagone
		// gremo preko seznama v obratni smeri, da se izognemo problemom z indeksi
		for(int i = train.size() - 1; i >= 0; i--) {
			// ce ima vagon N ali vec razlicnih tipov tovora, ga odstrani
			if(train.get(i).getNumCargoTypes() >= ODSTRANI_HET_property) {
				train.remove(i);
			}
		}
		/*for (Wagon wagon : toRemove) {
		    train.remove(wagon);
		}*/
	}
	
	// metoda za obravnavo ukaza ODSTRANI_ZAS
	public static void handle_ODSTRANI_ZAS(LinkedList train, int ODSTRANI_ZAS_property) {
		// LinkedList<Wagon> toRemove = new LinkedList<Wagon>();
		// gremo cez vse vagone
		for(int i = train.size() - 1; i >= 0; i--) {
			// izracunamo procent uporabljene razpolozljive obremenitve
			double percentOfMax = ((double)train.get(i).getTotalCargoWeight() * 100.0)/((double)train.get(i).getMaxWeight());
			// ce je procent vecji od P, vagon odstranimo
			if(percentOfMax >= (double)ODSTRANI_ZAS_property) {
				train.remove(i);
			}	
		}
		
		/*for (Wagon wagon : toRemove) {
		    train.remove(wagon);
		}*/
	}
	
	public static void handle_OBRNI(LinkedList train) {
		train.reverse();
	}
	
	// metoda, ki aplicira merge sort algoritem na seznam, ki predstavlja vlak in
	// kompozicijo vagonov uredi po tezi vagonov (narascujoce)
	public static void handle_UREDI(LinkedList train) {
		train.first.next = LinkedList.mergeSort(train.getFirstLinkedListElement().next);
		train.first.next.previous = train.first;
		LinkedListElement el = train.first;
		while(el.next != null) {
			el = el.next;
		}
		train.last = el.previous;
	}
	
	// metoda za obravnavo premikanja tovora
	public static void handle_PREMAKNI(LinkedList train, String PREMAKNI_tip, int PREMAKNI_vagon1, int PREMAKNI_vagon2) {
		// Premakni ves tovor tipa Tip iz Vagon1 na Vagon2, kjer sta Vagon1 in Vagon2 indeksa ustreznih vagonov.
		// C je na Vagon2 ze ta tip tovora, se tezi sestejeta, ce se ni, se tovor doda na koncu. Ce na Vagon1 ni tega tipa, se ne zgodi nic
		// ce vagon z indeksom ne obstaja, se ne zgodi nic
		// ce vagon z indeksom ne obstaja ali pa ce na vagonu iz katerega jemljemo ni doticnega tovora ali ce gre za isti vagon, ne naredi nic
		if(PREMAKNI_vagon1 >= train.size() || PREMAKNI_vagon2 >= train.size() ||
				PREMAKNI_vagon1 == PREMAKNI_vagon2 || train.get(PREMAKNI_vagon1).containsCargo(PREMAKNI_tip) == -1) {
			return;
		}
		// gremo cez vagon na katerega dodajamo in preverimo, ce ta tovor ze obstaja
		for(int i = 0; i < train.get(PREMAKNI_vagon1).getCargo().size(); i++) {
			if(train.get(PREMAKNI_vagon1).getCargo().get(i).getCargoType().equals(PREMAKNI_tip)) {
				// tovor, ki ga zelimo premakniti
				Cargo toMove = train.get(PREMAKNI_vagon1).getCargo().get(i);
				int cargoIndex = train.get(PREMAKNI_vagon2).containsCargo(PREMAKNI_tip);
				// ce vagon na katerega premikamo ze vsebuje podani tovor, pristejemo tezo od vagona iz katerega jemljemo pa tovor odstranimo
				if(cargoIndex != -1) {
					train.get(PREMAKNI_vagon2).getCargo().get(cargoIndex).addCargoWeight(toMove.getCargoWeight());
					// tezo dodanega tovora pristejemo se k celotni tezi vagona
					train.get(PREMAKNI_vagon2).addCargoWeight(toMove.getCargoWeight());
					// odstranimo doticni tovor iz vagona 1
					train.get(PREMAKNI_vagon1).getCargo().remove(i);
					// tezo odstranjenega tovora odstejemo s celotne teze vagona
					train.get(PREMAKNI_vagon1).removeCargoWeight(toMove.getCargoWeight());
					train.get(PREMAKNI_vagon1).decrementNumCargoTypes();
					return;
				} else {
					// sicer dodamo nov tip tovora na vagon na katerega premikamo in iz vagona iz katerega smo tovor vzeli, tovor odstranimo
					train.get(PREMAKNI_vagon2).getCargo().add(toMove);
					train.get(PREMAKNI_vagon2).addCargoWeight(toMove.getCargoWeight());
					train.get(PREMAKNI_vagon2).incremenetNumCargoTypes();
					train.get(PREMAKNI_vagon1).getCargo().remove(i);
					train.get(PREMAKNI_vagon1).removeCargoWeight(toMove.getCargoWeight());
					train.get(PREMAKNI_vagon1).decrementNumCargoTypes();
					return;
				}
			}
		}
	}
	
	/* metoda za izracun veljavnosti kompozicije 
	 (ali so vsi vagoni obremenjeni enako ali manj od njihove maksimalne dovoljene obremenitve)
	 ter ali skupna teza vlaka ne presega maksimalne dovoljene za dano lokomotivo */
	public static boolean isValidComposition(LinkedList train, int maxTrainWeight) {
		boolean validity = true;
		// stevec celotne teze kompozicije
		int sumWeight = 0;
		
		LinkedListElement el = train.getFirstLinkedListElement().next;
		// gremo cez vse vagone
		while(el != null) {
			// k stevcu celotne teze kompozicije pristejemo celotno tezo trenutnega vagona
			sumWeight += el.element.getTotalCargoWeight();
			// ce trenuten vagon presega maksimalno tezo, potem kompozicija ni veljavna
			if(el.element.exceedsMaxWeight()) {
				validity = false;
			}
			el = el.next;
		}
		
		// ce celotna teza vlaka presega maksimalno obremenitev lokomomotive, kompozicija ni veljavna
		if(sumWeight > maxTrainWeight) {
			validity = false;
		}
		
		// vrni veljavnost
		return validity;
	}
	
	
	// metoda za koncni izpis kompozicije, ki jo predstavlja vlak
	public static void printComposition(int maxTrainWeight, LinkedList train) {
		// izpis podatkov o celotnem vlaku
		System.out.printf("%d %d\n", maxTrainWeight, train.size());
		
		// izpis o kompoziciji
		LinkedListElement el = train.getFirstLinkedListElement().next;
		while(el != null) {
			// izpisemo podatke o vagonu
			System.out.printf("%d %d\n", el.element.getMaxWeight(), el.element.getCargo().size());
			CargoLinkedListElement cel = el.element.getCargo().getFirstLinkedListElement().next;
			// izpisemo podatke o tovoru na vagonu
			while(cel != null) {
				System.out.printf("%s %d\n", cel.element.getCargoType(), cel.element.getCargoWeight());
				cel = cel.next;
			}
			el = el.next;
		}
		
		// izpis veljavnosti kompozicije - klic metode zgoraj
		System.out.printf(isValidComposition(train, maxTrainWeight) ? "DA\n" : "NE\n");
	}
}