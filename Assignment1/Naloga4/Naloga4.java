import java.util.Scanner;

public class Naloga4 {
	public static void main(String[] args) {
		
		// kreiranje nove instance razreda Scanner
		Scanner sc = new Scanner(System.in);
		
		// kreiranje povezanega seznama, ki bo predstavljal kompozicijo vlaka
		LinkedList train = new LinkedList();
		
		// branje zacetnih podatkov - stevilo kosov tovora in najvecjo dovoljeno tezo vagona
		int numCargo = sc.nextInt();
		int maxWagonWeight = sc.nextInt();
		
		// kreiranje tabele tovora, ki ga imamo na zacetku
		Cargo[] toLoad = new Cargo[numCargo];
		
		// branje podatkov o tovoru, ki ga moramo razporediti v kompozicijo
		for(int i = 0; i < numCargo; i++) {
			// branje tipa in teze tovora
			String cargoType = sc.next();
			int cargoWeight = sc.nextInt();
			
			// kreiranje novega objekta, ki predstavlja tovor ustreznega tipa z ustrezno tezo
			toLoad[i] = new Cargo(cargoType, cargoWeight);
		}
		
		sc.close();
		/* razporejanje tovora:
		 * 1. vzami prvi tovor in ga razporedi na prvi vagon
		 * 2. poisci najtezji kos tovora istega tipa, ki se gre v isti vagon (ce obstaja) in ga daj v vagon
		 * 3. ko taksnega kosa vec ni, vzami naslednji tovor in ga daj v vagon
		 * 4. ponavljaj
		 * 
		 */
		
		// sortiranje tovora po tezi z uporabo merge sort algoritma
		toLoad = mergeSort(toLoad);
		
		// gremo cez ves tovor, ki ga moramo naloziti
		for(int i = 0; i < toLoad.length; i++) {
			if(toLoad[i] == null) {
				continue;				
			}
			// dodamo nasledji tovor na naslednji vagon
			Wagon wagon = new Wagon(maxWagonWeight);
			Cargo toAdd = toLoad[i];
			wagon.addCargo(toAdd);
			toLoad[i] = null;
			// gremo cez seznam tovora in pogledamo, ce obstaja kos tovora istega tipa, ki se gre na vagon				
			// izberemo najvecji kos tovora, ki se gre zraven na vagon (ce obstaja) in ga dodamo na vagon
			// to pocnemo, dokler se trenutnega vagona ne da vec napolniti
			boolean fill = true;
			while(fill) {
				int maxFit = -1;
				int indexMaxFit = -1;
				// gremo cez tovor
				for(int j = 0; j < toLoad.length; j++) {
					if(toLoad[j] == null) {
						continue;
					}
					// ce je tovor istega tipa kot tovor na trenutnem vagonu in ce ga lahko dodamo na trenutni vagon
					if(toLoad[j].getCargoType().equals(toAdd.getCargoType()) && (toLoad[j].getCargoWeight() + wagon.getTotalWeight()) <= wagon.getMaxWeight() && toLoad[j].getCargoWeight() > maxFit) {
						maxFit = toLoad[j].getCargoWeight();
						indexMaxFit = j;
					}
				}
				// ce smo nasli neko maksimalno tezo tovora istega tipa, ki se pase na trenutni vagon, ga dodamo na ta vagon
				if(maxFit != -1) {
					wagon.addCargo(toLoad[indexMaxFit]);
					toLoad[indexMaxFit] = null;
				} else {
					// ce nismo nasli tovora, ki bi ga se lahko dodali na vagon, gremo ven iz zanke
					fill = false;
				}
			}
			// na kompozicijo dodamo vagon
			train.add(wagon);
			// sicer zacnemo postopek znova
		}
		
		/* zdruzevanje vagonov:
		 * 1. Uredi vagone po tezi
		 * 2. Zacensi z najtezjim za vsak vagon preveri, ce bi se dalo ta vagon zdruziti s katerim drugim
		 * 3. Ce se da, prestavi tovor in pobrisi vagon, katerega tovor si prestavil
		 */
		
		// urejanje vagonov po njihovi tezi
		train.first.next = LinkedList.mergeSort(train.getFirstLinkedListElement().next);
		
		// ker vemo, da je trenutno v vsakem vagonu le en tip tovora,
		// je trenutna cena kompozicije enaka stevilu vagonov * 2
		int cost = train.size() * 2;
		
		// stevec, ki steje koliko zdruzitev vagonov smo opravili
		int mergeCounter = 0;
		
		// gremo cez urejen seznam vagonov (v dveh zankah)
		for(int i = train.size() - 1; i >= 0; i--) {
			for(int j = train.size() - 1; j >= 0; j--) {
				// ce smo na istem vagonu, preskocimo iteracijo
				if(i == j) {
					continue;
				}
				// ce je mozno tovora na vagonih zdruziti, brez da bi presegli maksimalno dovoljeno maso
				if(train.get(i).getTotalWeight() + train.get(j).getTotalWeight() <= maxWagonWeight) {
					// tovor iz vagona na indeksu i prestavimo na vagon z indeksom j
					train.get(j).addAllCargo(train.get(i).getCargo());
					// odstranimo vagon, katerega tovor smo prestavili
					train.remove(i);
					// povecamo stevec zdruzitev
					mergeCounter++;
				}
			}
		}
		
		// od cene odstejemo stevilo zdruzitev, ki smo jih opravili 
		// (-2 zaradi odstranitve vagona in +1, ker je na zdruzenem vagonu sedaj en tip tovora vec)
		cost = cost - mergeCounter;
		
		// izpisemo ceno kompozicije
		System.out.println(cost);
	}
	
	// pomozna metoda merge za merge sort urejanje
	private static Cargo[] merge(Cargo[] a, Cargo[] b) {
		// definiramo novo tabelo tovorov, ki ima za dolzino vsoto dolzin tabel, ki jih zdruzujemo
		Cargo[] c = new Cargo[a.length + b.length];
		// definiramo dve indeksni spremenljivki
		int i = 0, j = 0;
		// zdruzujemo
		for(int k = 0; k < c.length; k++) {
        	if(i >= a.length) {
        		c[k] = b[j++];
        	} else if (j >= b.length) {
        		c[k] = a[i++];
        	} else if (a[i].getCargoWeight() >= b[j].getCargoWeight()) {
            	c[k] = a[i++];
            } else {
            	c[k] = b[j++];
            }
        }
		// vrnemo zdruzeno tabelo
        return c;
	}

	// metoda za izvajanje merge sort algoritma nad tabelo tovorov
	public static Cargo[] mergeSort(Cargo[] toSort) {
    	int len = toSort.length;
    	// robni pogoj
        if(len <= 1) {
        	return toSort;
        }
        // definiramo novi tabeli, ki bosta vsebovali polovici tabele, ki jo sortiramo
        Cargo[] a = new Cargo[len/2];
        Cargo[] b = new Cargo[len - len/2];
        // napolnimo tabeli z ustreznimi elementi
        for(int i = 0; i < a.length; i++) {
        	a[i] = toSort[i];
        }
        for(int i = 0; i < b.length; i++) {
        	b[i] = toSort[i + len/2];
        }
        // rekurzivna klica za polovici tabele in na koncu zdruzitev
        return merge(mergeSort(a), mergeSort(b));
    }
}