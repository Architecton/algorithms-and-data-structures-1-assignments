import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;

public class Naloga10 {
	// glavna metoda
	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		
		// Definicija in inicializacija tokov za branje podatkov iz datotek //////
		FileReader reader = new FileReader(args[0]);
		PrintWriter writer = new PrintWriter(args[1]);
		BufferedReader inStream = new BufferedReader(reader);
		//////////////////////////////////////////////////////////////////////////
		
		// spremenljivke in podatkovne strukture /////////////////////////////////
		Debt[] debts;
		ArrayList<Integer> balances;
		int numDebts;
		int numPersons;
		
		//////////////////////////////////////////////////////////////////////////
		
		// branje vhodnih podatkov ///////////////////////////////////////////////
		
		// preberi stevilo dolgov
		numDebts = Integer.parseInt(inStream.readLine());
		
		// inicializiraj tabelo dolgov
		debts = new Debt[numDebts];
		
		// branje dolgov
		for(int i = 0; i < numDebts; i++) {
			// prebrano vrstico razclenimo po vejici
			String[] data = inStream.readLine().split(",");
			// v tabelo dolgov vstavimo nov dolg
			debts[i] = new Debt(data[0], data[1], Integer.parseInt(data[2]));
		}
		
		// izracunaj stevilo oseb, ki nastopajo v bilancah
		numPersons = getNumPersons(debts);
		
		// inicializiraj seznam bilanc
		balances = new ArrayList<Integer>();
		
		//////////////////////////////////////////////////////////////////////////

		// izracun vrednosti v tabeli bilanc /////////////////////////////////////
		
		// pojdi cez vse indekse oseb
		for(int personIndex = 1; personIndex <= numPersons; personIndex++) {
			// zacetna balanca je 0
			int balance = 0;
			// pojdi cez vse dolgove
			for(Debt debt : debts) {
				// ce oseba z personIndex nastopa v dolgu na mestu A
				if(debt.getIndexA() == personIndex) {
					// od bilance osebe odstej vrednost dolga
					balance -= debt.getValue();
				}
				// ce oseba z personIndex nastopa v dolgu na mestu B
				if(debt.getIndexB() == personIndex) {
					// bilanci osebe pristej vrednost dolga
					balance += debt.getValue();
				}
			}
			// dodaj bilanco osebe z personIndex v seznam bilanc (ce ni nicelna)
			if(balance != 0) {
				balances.add(balance);
			}
		}
		
		// prepis v dve tabeli kreditorjev in dolznikov
		
		// seznam bilanc uredi narascujoce
		Collections.sort(balances);
		
		// kreiraj in napolni tabeli kreditorjev in dolznikov
		int[] owes = new int[balances.size()];
		int[] owed = new int[balances.size()];
		
		// polnjenje tabel
		for(int i = 0; i < balances.size(); i++) {
			if(balances.get(i) < 0) {
				owes[i] = Math.abs(balances.get(i));
				owed[i] = 0;
			} else {
				owes[i] = 0;
				owed[i] = balances.get(i);
			}
		}
		
		
		//////////////////////////////////////////////////////////////////////////

		// izracun minimalnega potrebega stevila transakcij za poravnavo dolgov //

		// stevec "optimizacijskih" transakcij, ki jih opravimo pred glavno optimizacijsko zanko
		int startTransactionCounter = 0;
		
		// pojdi cez tabeli kreditorjev in dolznikov, in iznici morebitne identicne vrednosti v razlicnih tabelah
		for(int i = 0; i < owes.length; i++) {
			for(int j = 0; j < owed.length; j++) {
				if(owes[i] != 0 && owed[j] != 0 && owes[i] == owed[j]) {
					owes[i] = owed[j] = 0;
					startTransactionCounter++;
				}
			}
		}
		
		// definicija in inicializacija enotne spremenljivke za dolzino tabel
		int balancesLength = owes.length;

		// instantiacija optimizacijske vrste
		Deque<Balance> optimizationQueue = new LinkedList<Balance>();
		
		// v otimizacijsko vrsto daj zacetno bilanco (ter stevilo opravljenih predoptimizacijskih transakcij)
		optimizationQueue.add(new Balance(owes, owed, startTransactionCounter));
		
		// spremenljivka, ki bo vsebovala rezultat
		int minimumTransactions = 0;
		
		// definicija in inicializacija spremenljivk za upravljanje s panicnim nacinom
		boolean panicMode = false;
		
		for(;;) {
			// po 3.5 sekundah izvajanja preidi v panicni nacin
			// v panicnem nacinu preverjaj samo prvega kreditorja v tabeli
			if(System.currentTimeMillis() - startTime > 2500 && !panicMode) {
				panicMode = true;
				//System.out.printf("PANIC @ %.3f\n", (double)(System.currentTimeMillis() - startTime)/1000.0);
				optimizationQueue.removeAll(optimizationQueue);
				optimizationQueue.add(new Balance(owes, owed, startTransactionCounter));
			}
			
			// iz optimizacijske vrste vzami naslednjo konfiguracijo bilancs
			Balance currentBalance = optimizationQueue.remove();
			// pridobi tabele dolznikov in kreditorjev
			int[] workingOwes = currentBalance.getOwes();
			int[] workingOwed = currentBalance.getOwed();
			
			// ce sta obe tabeli izniceni, shrani stevilo potrebnih transakcij in pojdi iz optimizacijske zanke
			if(zeroed(workingOwes, workingOwed)) {
				minimumTransactions = currentBalance.getTransactionCounter();
				break;
			}
			
			// sicer povecamo stevec transakcij za trenutno bilanco
			currentBalance.incrementTransactionCounter();
			
			// optimizacijski proces - pojdi cez vse mozne poteze ter jih daj v zanko
			for(int i = 0; i < balancesLength; i++) {
				for(int j = 1; (panicMode) ? workingOwed[j - 1] == 0 : j < balancesLength; j++) {
					if(workingOwes[i] != 0 && workingOwed[j] != 0){
						if(workingOwes[i] - workingOwed[j] < 0) {
							int[] newOwes = workingOwes.clone();
							int[] newOwed = workingOwed.clone();

							newOwed[j] -= newOwes[i];
							newOwes[i] = 0;
							optimizationQueue.add(new Balance(newOwes, newOwed, currentBalance.getTransactionCounter()));
						} else if(owes[i] - owed[j] > 0){
							int[] newOwes = workingOwes.clone();
							int[] newOwed = workingOwed.clone();

							newOwes[i] -= newOwed[j];
							newOwed[j] = 0;
							optimizationQueue.add(new Balance(newOwes, newOwed, currentBalance.getTransactionCounter()));
						} else {
							int[] newOwes = workingOwes.clone();
							int[] newOwed = workingOwed.clone();

							newOwes[i] = 0;
							newOwed[j] = 0;
							optimizationQueue.add(new Balance(newOwes, newOwed, currentBalance.getTransactionCounter()));
						}
					}
				}
			}
		}
		
		// v datoteko zapisemo minimalno stevilo transakcij
		writer.println(minimumTransactions);
		//////////////////////////////////////////////////////////////////////////
		
		// zapiranje tokov ///////////////////////////////////////////////////////
		inStream.close();
		writer.close();
		reader.close();
		/////////////////////////////////////////////////////////////////////////
	}
	
	
	// getNumPersons: metoda, ki vrne stevilo oseb, ki nastopajo v bilancah
	public static int getNumPersons(Debt[] debts) {
		int numPersons = 0;
		// pojdi cez vse dolgove in preveri indekse oseb
		for(Debt debt : debts) {
			// poisci najvecji indeks - predstavlja stevilo oseb, ki nastopajo v bilancah
			if(debt.getIndexA() > numPersons) {
				numPersons = debt.getIndexA();
			}
			if(debt.getIndexB() > numPersons) {
				numPersons = debt.getIndexB();
			}
		}
		// vrni stevilo oseb
		return numPersons;
	}
	
	// zeroed: metoda, ki vrne true, ce tabeli owes in owed vsebujeta izkljucno vrednosti 0.
	// predpostavi, da imata obe tabeli enako dolzino
	public static boolean zeroed(int[] owes, int[] owed) {
		// pojdi cez obe tabeli in vrni valse, ce najdes nenicelno vrednost
		for(int i = 0; i < owes.length; i++) {
			if(owes[i] != 0 || owed[i] != 0) {
				return false;
			}
		}
		// ce nenicelne vrednosti nisi nasel, vrni true
		return true;
	}
}