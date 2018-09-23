import java.util.Scanner;

public class Naloga5 {
	
	// definicija spremenljivk, ki vsebujejo dimenzije labirinta
	static int xFieldDimension;
	static int yFieldDimension;
	
	// glavna metoda
	public static void main(String[] args) {
		// kreiranje objekta Scanner za branje podatkov
		Scanner sc = new Scanner(System.in);
		
		// branje dimenzij labirinta
		yFieldDimension = sc.nextInt();
		xFieldDimension = sc.nextInt();
		//kreiranje polja, ki predstavlja labirint
		byte[][] field = new byte[yFieldDimension][xFieldDimension];
		
		
		// branje zgradbe labirinta v dvodimenzionalno tabelo
		for(int i = 0; i < yFieldDimension; i++) {
			for(int j = 0; j < xFieldDimension; j++) {
				field[i][j] = sc.nextByte();
			}
		}
		
		// branje stevila funkcij
		int numFunctions = sc.nextInt();
		
		// kreiranje tabele objektov razreda Function, ki predstavljajo funkcije, ki jih bo izvajal robot
		Function[] functions = new Function[numFunctions];
		
		// branje funkcij v tabelo objektoc razreda Function
		for(int i = 0; i < numFunctions; i++) {
			// beremo stevilo ukazov
			int numCommands = sc.nextInt();
			// kreiramo novo instanco razreda Function
			functions[i] = new Function(numCommands);
			// v tabelo ukazov v kreirani instanci razreda Function beremo ukaze
			for(int j = 0; j < numCommands; j++) {
				String instruction = sc.next();
				// ce je ukaz FUN, beremo se dodatni podatek (index klicane funkcije)
				if(instruction.equals("FUN")) {
					int FUN_property = sc.nextInt();
					functions[i].addCommand(new Command(instruction, FUN_property));
				} else {
					functions[i].addCommand(new Command(instruction));
				}
			}
		}
		
		// branje zacetne pozicije in orientacije robota
		int yStart = sc.nextInt();
		int xStart = sc.nextInt();
		int orientationStart = sc.nextInt();
		
		// kreiranje instance robota z zacetnimi koordinatami in zacetno orientacijo
		// y-pozicijo robota prevedemo v "racunalniski" koordinatni sistem, kjer je [0, 0] v levem zgornjem kotu
		Robot robot = new Robot(xStart, Math.abs(yStart - (yFieldDimension - 1)), orientationStart);
		// kreiranje nove instance sklada, v katerega shranjujemo trenutni klicni sklad
		LinkedListStack callStack = new LinkedListStack();
		
		//kreiranje nove instance vrste kontekstov izvajanja
		Queue executionContexts = new Queue();
		
		// branje stevila korakov izvajanja
		int numSteps = sc.nextInt();

		// zapiranje objekta tipa Scanner
		sc.close();
		
		// definicija in inicializacija stevca izvajanja
		Counter stepCounter = new Counter();
		
		// ---------------- izvajanje programa robota ----------------------------
		// indeks funkcije ki jo izvajamo in indeks ukaza v funkciji, ki ga izvajamo
		int functionIndex = 0;
		int commandIndex = 0;
		
		// zanka izvajanja ukazov
		for(;;) {
			executionLoop: {
				// gremo cez ukaze v funkciji, ki jo izvajamo - zacnemo pri ukazu z indeksom commandIndex
				for(int executionIndex = commandIndex; executionIndex < functions[functionIndex].getCommands().length; executionIndex++) {
					
					// priprava ukaza za izvrsitev
					Command currentCommand = functions[functionIndex].getCommands()[executionIndex];
					String currentInstruction = currentCommand.getInstruction();
	
					// ali se je ukaz izvrsil uspesno
					boolean success = false;
					
					// izvrsevanje ukaza
					switch(currentInstruction) {
					case "FWD":
						// obravnavamo ukaz FWD
						success = handle_FWD(field, robot);
						// povecamo stevec korakov
						stepCounter.increment();
						//System.out.println("stepCounter == " + stepCounter.getValue());
						// ce smo dosegli nastavljeno stevilo korakov izvajanja, zakljucimo izvajanje in izpisemo rezultat
						if(stepCounter.getValue() >= numSteps) {
							printResult(field, yFieldDimension, robot);
						}
						break;
					case "RGT":
						success = handle_RGT(field, robot);
						// povecamo stevec korakov
						stepCounter.increment();
						// ce smo dosegli nastavljeno stevilo korakov izvajanja, zakljucimo izvajanje in izpisemo rezultat
						//System.out.println("stepCounter == " + stepCounter.getValue());
						if(stepCounter.getValue() >= numSteps) {
							printResult(field, yFieldDimension, robot);
						}
						break;
					case "LFT":
						// obravnavamo ukaz LFT
						success = handle_LFT(field, robot);
						// povecamo stevec korakov
						stepCounter.increment();
						// ce smo dosegli nastavljeno stevilo korakov izvajanja, zakljucimo izvajanje in izpisemo rezultat
						//System.out.println("stepCounter == " + stepCounter.getValue());
						if(stepCounter.getValue() >= numSteps) {
							printResult(field, yFieldDimension, robot);
						}
						break;
					case "FUN":
						// dodaj trenutno funkcijo in index trenutnega ukaza na klicni sklad
						callStack.push(new CallStackElement(functionIndex, executionIndex));
						// pojdi na klicano funkcijo
						functionIndex = currentCommand.getFUN_property() - 1;
						// novo funkcijo zacni od zacetka
						commandIndex = 0;
						// pojdi na zacetek izvajanja nove funkcije
						break executionLoop;
					case "SETJMP":
						// trenutno funkcijo in trenutni index ukaza daj na sklad, da se bos lahko vrnil na to mesto
						callStack.push(new CallStackElement(functionIndex, executionIndex));
						// dodaj trenutni kontekst izvajanja v vrsto kontekstov
						try {
						executionContexts.add(new ExecutionContext(LinkedListStack.deepCopy(callStack)));
						} catch(Exception e) {
							System.out.println("tralala");
						}
						// trenutno funkcijo in trenutni index ukaza po shranjevanju odstranimo in nadaljujemo
						callStack.pop();
						success = true;
						break;
					case "JMP":
						// ce je vrsta kontekstov prazna, 
						if(executionContexts.isEmpty()) {
							success = false;
							break;
						// iz vrste kontekstov poberi zadnji kontekst
						} else {
							ExecutionContext newContext = executionContexts.remove();
							// iz pridobljenega konteksta poberi klicni sklad
							callStack = newContext.getCallStack();
							// iz pridobljenega klicnega sklada pridobimo funkcijo na vrhu
							CallStackElement gotoFunction = callStack.pop();
							// functionIndex nastavimo na indeks pridobljene funkcije
							functionIndex = gotoFunction.getFunctionIndex();
							// commandIndex nastavimo na naslednji ukaz za ukazom, kjer je bil shranjen kontekst izvajanja
							commandIndex = gotoFunction.getExecutionIndex() + 1;
							// gremo na izvajanje nove funkcije
							break executionLoop;
						}
					}
					// ce se je izvajanje funkcije prekinilo zaradi neuspesno izvedenega ukaza
					if(!success) {
						// ce klicni sklad ni prazen
						if(!callStack.isEmpty()) {
							// pridobimo funkcijo na vrhu klicnega sklada
							CallStackElement gotoFunction = callStack.pop();
							// functionIndex nastavimo na index funkcije, ki smo jo vzeli s sklada
							functionIndex = gotoFunction.getFunctionIndex();
							// commandIndex nastavimo na naslednji ukaz za ukazom, kjer je bil shranjen kontekst izvajanja
							commandIndex = gotoFunction.getExecutionIndex() + 1;
							/* ce je commandIndex sedaj vecji od stevila ukazov v trenutni funkciji to pomeni, da smo
							prisli do konca funkcije in se moramo vrniti na klicoco funkcijo*/
							while(commandIndex >= functions[functionIndex].getCommands().length && !callStack.isEmpty()) {
								// pridobimo funkcijo na vrhu klicnega sklada
								gotoFunction = callStack.pop();
								// functionIndex nastavimo na index funkcije, ki smo jo vzeli s sklada
								functionIndex = gotoFunction.getFunctionIndex();
								// commandIndex nastavimo na naslednji ukaz za ukazom, kjer je bil shranjen kontekst izvajanja
								commandIndex = gotoFunction.getExecutionIndex() + 1;
							}
							// ce smo izpraznili klicni sklad in smo izvedli zadnji ukaz v funkciji
							if(callStack.isEmpty() && commandIndex >= functions[functionIndex].getCommands().length) {
								// gremo na naslednjo funkcijo
								functionIndex++;
								// ce smo prisli do konca ukazov zadnje funkcije pri praznem klicnem skladu, zakljucimo z izvajanjem
								if(functionIndex <= numFunctions) {
									printResult(field, yFieldDimension, robot);
								}
							}
							// sicer gremo nazaj na izvajanje
							break executionLoop;
						// ce je klicni sklad prazen
						} else {
							// zacnemo na zacetku naslednje funkcije
							commandIndex = 0;
							functionIndex++;
							// ce se je prekinila zadnja funkcija v programu, izpisemo rezultat in zakljucimo izvajanje
							if(functionIndex >= numFunctions) {
								printResult(field, yFieldDimension, robot);
							}
						}
					}
				}
				// ce smo uspesno izvedli zadnji ukaz v funkciji in ce je klicni sklad prazen, grem na naslednjo funkcijo
				if(callStack.isEmpty()) {
					functionIndex++;
					if(functionIndex <= numFunctions) {
						//testPrint(executionIndex, currentCommand, stepCounter, robot, yFieldDimension, xFieldDimension);
						printResult(field, yFieldDimension, robot);
					}
				// sicer, ce smo uspesno izvedli zadnji ukaz v funkciji, kjer klicni sklad ni prazen
				} else {
					// pridobimo funkcijo na vrhu klicnega sklada
					CallStackElement gotoFunction = callStack.pop();
					// functionIndex nastavimo na index funkcije, ki smo jo vzeli s sklada
					functionIndex = gotoFunction.getFunctionIndex();
					// commandIndex nastavimo na naslednji ukaz za ukazom, kjer je bil shranjen kontekst izvajanja
					commandIndex = gotoFunction.getExecutionIndex() + 1;
					/* ce je commandIndex sedaj vecji od stevila ukazov v trenutni funkciji to pomeni, da smo
					prisli do konca funkcije in se moramo vrniti na klicoco funkcijo*/
					while(commandIndex >= functions[functionIndex].getCommands().length && !callStack.isEmpty()) {
						// pridobimo funkcijo na vrhu klicnega sklada
						gotoFunction = callStack.pop();
						// functionIndex nastavimo na index funkcije, ki smo jo vzeli s sklada
						functionIndex = gotoFunction.getFunctionIndex();
						// commandIndex nastavimo na naslednji ukaz za ukazom, kjer je bil shranjen kontekst izvajanja
						commandIndex = gotoFunction.getExecutionIndex() + 1;
					}
					
					// ce smo izpraznili klicni sklad
					if(callStack.isEmpty() && commandIndex >= functions[functionIndex].getCommands().length) {
						// gremo na naslednjo funkcijo
						functionIndex++;
						// ce smo prisli do konca ukazov zadnje funkcije pri praznem klicnem skladu, zakljucimo z izvajanjem
						if(functionIndex <= numFunctions) {
							printResult(field, yFieldDimension, robot);
						}
					}
					// sicer gremo nazaj na izvajanje
					break executionLoop;
				}
			}		
			// ------------------------------------------------------------------------
		}
	}
	
	// --- Metode za obravnavo ukazov --------
	// metoda za obravnavo ukaza FWD
	public static boolean handle_FWD(byte[][] field, Robot robot) {
		// pridobimo podatke o trenutni legi in orientaciji robota
		int robotOrientation = robot.getOrientation();
		int robotX = robot.getXPosition();
		int robotY = robot.getYPosition();
		// preveri, ce je polje, na katerega se naj bi robot premaknil, prosto
		switch(robotOrientation) {
			case 0:
				if(field[robotY - 1][robotX] == 0) {
					robot.incrementYPosition();
					return true;
				} else {
					return false;
				}
			case 1:
				if(field[robotY][robotX + 1] == 0) {
					robot.incrementXPosition();
					return true;
				} else {
					return false;
				}
			case 2:
				if(field[robotY + 1][robotX] == 0) {
					robot.decrementYPosition();
					return true;
				} else {
					return false;
				}
			case 3:
				if(field[robotY][robotX - 1] == 0) {
					robot.decrementXPosition();
					return true;
				} else {
					return false;
				}
		}
		return false;
	}
	
	// metoda za obravnavo ukaza RGT
	public static boolean handle_RGT(byte[][] field, Robot robot) {
		// robotu spremenimo orientacijo
		robot.rotateRight();
		return true;
	}
	
	// metoda za obravnavo ukaza LFT
	public static boolean handle_LFT(byte[][] field, Robot robot) {
		// robotu spremenimo orientacijo
		robot.rotateLeft();
		return true;
	}
	
	// metoda za izpis koncne lege in orientacije robota, ki predstavlja tudi zakljucno tocko programa
	public static void printResult(byte[][] field, int yFieldDimension, Robot robot) {
		System.out.printf("%d %d %d\n", Math.abs(robot.getYPosition() - (yFieldDimension - 1)), robot.getXPosition(),
				robot.getOrientation());
		// zakljucimo program
		System.exit(0);
	}
	
	// ------- metode za testiranje programa -------------------------------------
	
	// metoda za izpis trenutnih podatkov o stanju robota
	/*public static void testPrint(int functionIndex, int executionIndex, Command currentCommand, Counter stepCounter, Robot robot, int xFieldDimension, int yFieldDimension) {
		// izpis podatkov o trenutnem stanju robota
		System.out.printf("robot x position == %d, robot y position == %d, robot orientation == %d, stepCounter == %d\n", 
				robot.getXPosition(), Math.abs(robot.getYPosition() - (yFieldDimension - 1)), robot.getOrientation(), stepCounter.getValue());
		System.out.printf("functionIndex == %d, executionIndex == %d, currentCommand == %s\n",functionIndex, executionIndex,
				currentCommand.getInstruction().equals("FUN") ? currentCommand.getInstruction() + " " + currentCommand.getFUN_property() : currentCommand.getInstruction());
		System.out.println();
	}*/
	
	// metoda za izpis vsebine trenutnega klicnega sklada
	/*public static void printCallStack(Stack<CallStackElement> callStack) {
		// naredimo kopijo trenutnega klicnega sklada
		@SuppressWarnings("unchecked")
		Stack<CallStackElement> stackCopy = (Stack<CallStackElement>)callStack.clone();
		// trenutni klicni sklad izpisemo
		while(!stackCopy.isEmpty()) {
			System.out.println("CallStackElement: " + stackCopy.pop().toString());
		}
	}*/
	// -----------------------------------------------------------------------------
}