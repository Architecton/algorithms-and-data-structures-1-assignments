import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Naloga9 {

	// signal, ki ga zapisemo v izhodno datoteko, ko sekvenca ne obstaja
	public static final int doesNotExistSignal = -1;
	
	// glavna metoda
	public static void main(String[] args) throws IOException {
		// Definicija in inicializacija tokov za branje podatkov iz datotek
		FileReader reader = new FileReader(args[0]);
		PrintWriter writer = new PrintWriter(args[1]);
		BufferedReader inStream = new BufferedReader(reader);
		////////////////////////////////////////////////////////////
		
		// spremenljivke in podatkovne strukture ///////////////////
		Set<Integer> confirmed = new HashSet<Integer>();
		ArrayList<Integer> callSequence = new ArrayList<Integer>();
		int numFriends;
		int numConditions;
		////////////////////////////////////////////////////////////
		
		// branje podatkov /////////////////////////////////////////
		numFriends = Integer.parseInt(inStream.readLine());
		numConditions = Integer.parseInt(inStream.readLine());
		
		// kreiranje tabele pogojev
		ConditionsArray[] conditions = new ConditionsArray[numFriends];
		for(int i = 0; i < conditions.length; i++) {
			conditions[i] = new ConditionsArray();
		}
		
		// branje informacij v tabelo pogojev
		for(int i = 0; i < numConditions; i++) {
			// prebran pogoj razclenimo po "," in shranimo v tabelo
			String[] splitCondition = inStream.readLine().split(",");
			// kreiramo novo instanco razreda Condition z prebranima informacijama
			conditions[Integer.parseInt(splitCondition[0]) - 1].addCondition(new Condition(Integer.parseInt(splitCondition[0]), Integer.parseInt(splitCondition[1])));
		}
		
		////////////////////////////////////////////////////////////
		
		boolean sequenceExists = true;
		
		// glavni algoritem ////////////////////////////////////////
		while(!allConfirmed(confirmed, numFriends)) {
			// na zacetku predpostavi, da smo v ciklu
			boolean cycle = true;
			
			// pojdi cez vse indekse prijateljev
			for(int indexFriend = 1; indexFriend <= numFriends; indexFriend++) {
				// ce prijatelja z indeksom indexFriend se ni v mnozici potrjenih...
				if(!confirmed.contains(indexFriend)) {
					// ...preveri, ce ga lahko poklices
					if(canBeCalled(indexFriend, conditions, confirmed)) {
						// ce ga lahko, ga dodaj v mnozico potrjenih
						confirmed.add(indexFriend);
						// dodaj ga v sekvenco klicev na ustrezno mesto
						callSequence.add(indexFriend);
						// ce smo prijatelja uspesno potrdili, nismo v ciklu
						cycle = false;
						// pojdi na nasleden obhod
						break;
					}
				}
			}
			// ce v obhodu nismo potrdili nobenega prijatelja (smo v ciklu)
			if(cycle) {
				// sekvenca ne obstaja
				sequenceExists = false;
				// pojdi ven iz iskanja
				break;
			}
		}
		////////////////////////////////////////////////////////////
		
		// izpis rezultata programa v datoteko /////////////////////
		
		// ce sekvenca obstaja jo izpisemo v izhodno datoteko
		if(sequenceExists) {
			for(int i = 0; i < callSequence.size(); i++) {
				writer.printf((i == callSequence.size() - 1) ? "%d\n" : "%d,", callSequence.get(i));
			}
		// sicer v izhodno datoteko zapisemo signal za neobstoj sekvence
		} else {
			writer.printf("%d\n", doesNotExistSignal);
		}
		////////////////////////////////////////////////////////////
		
		// zapiranje tokov
		writer.close();
		inStream.close();
		reader.close();
		
	}
	
	// metode
	
	// allConfirmed: metoda, ki vrne true, ce so vsi prijatelji potrjeni. Sicer vrne false.
	public static boolean allConfirmed(Set<Integer> confirmed, int numFriends) {
		// ce je mnozica velika kot je stevilo najvecjega indeksa prijatelja, vrni true
		if(confirmed.size() == numFriends) {
			return true;
		}
		return false;
	}
	
	// canBeCalled: metoda, ki vrne true, ce je prijatelja z indeksom friendIndex mozno poklicati (ima izpolnjene vse pogoje)
	public static boolean canBeCalled(int indexFriend, ConditionsArray[] conditions, Set<Integer> confirmed) {
		ArrayList<Condition> friendConditions = conditions[indexFriend - 1].getConditions();
		
		// pojdi cez vse pogoje v tabeli pogojev
		for(Condition condition : friendConditions) {
			// shranimo pogoj, s katerim prijatelj pogojuje potrditev
			int friendCondition = condition.getCondition();
			// ce pogoj ni izpolnjen, vrnemo false
			if(!confirmed.contains(friendCondition)) {
				return false;
			}
		}
		// ce prides cez vse pogoje brez neizpolnjenosti, vrni true
		return true;
	}	
}