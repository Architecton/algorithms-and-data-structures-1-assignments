import java.util.Stack;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Iterator;

// razred za delo z nizi, ki predstavljajo logicni izraz
class ExpressionFormatter {
    // pomozna metoda, ki vrne precedenco posameznega operatorja
    private static int precedence(String s) {
        switch(s) {
        case "NOT":
            return 1;
        case "AND":
            return 2;
        case "OR":
            return 3;
        }
        return -1;
    }
    // isOperand: pomozna metoda, ki preveri, ce podani term predstavlja operand
    private static boolean isOperand(String token) {
    	if(token.equals("NOT") || token.equals("AND") || token.equals("OR") || token.equals(")") || token.equals("(")) {
    		return false;
    	}
    	return true;
    }
      
    /* infixToPostfix: metoda, ki pretvori podan izraz v infiksni obliki
	 * (razdeljen v terme) v postfiksno obliko
	 * */
	public static String infixToPostfix(String[] exp) {
		// deklaracija in inicializacija praznega niza za hranjenje rezultata
		String result = "";
		
		// inicializacija sklada za hranjenje nizov
		Stack<String> stack = new Stack<String>();
		
		// Gremo cez vse terme izraza.
		for(int i = 0; i < exp.length; ++i) {
			// Trenuten term shranimo v niz s.
			String s = exp[i]; 
			// Ce je trenutni term operand, ga dodamo v rezultatni niz.
			if(isOperand(s)) {
				result += s;
				result += " ";
			}
			// Ce je prebrani term "(", ga porinemo na sklad.
			else if(s.equals("(")) {
				stack.push(s);
			}
			
			// Ce je trenuten term ")", odstranjujemo elemente iz sklada, dokler ne pridemo do "("
			else if(s.equals(")")) {
				while(!stack.isEmpty() && !stack.peek().equals("(")){
					result += stack.pop();
					result += " ";
				}
				// Ce sklad ni prazen in term na vrhu sklada ni "("
				if (!stack.isEmpty() && !stack.peek().equals("(")){
					return "Napacno formatiran izraz";
				} else {
					// sicer odstranimo element na vrhu sklada
					stack.pop();
				}
			/* Ce je trenutni term operator,
			 * praznimo sklad, dokler ni prazen oziroma naletimo na operator z visjo precedenco.
			 * */
			} else {
				while(!stack.isEmpty() && precedence(s) <= precedence(stack.peek())){
					result += stack.pop() + " ";
				}
				// trenuten term damo na sklad
				stack.push(s);
            }
      
        }
		// Vse operatorje odstranimo iz sklada
		while (!stack.isEmpty()){
			result += stack.pop();
			result += " ";
		}
		// vrnemo rezultatni niz
		return result;
	}

	/* formatInfixExpression: metoda, ki prebrani in razclenjenji niz, 
	 * ki predstavlja logicni izraz formatira tako, da nadomesti unarni NOT izraz s 
	 * psevdobinarnim NOT izrazom
	 */
	public static String formatInfixExpression(String[] splitExpressionArray, int ix) {
		// inicializiramo prazen niz, ki bo vseboval koncni niz
		String finalString = "";
		// gremo cez razclenjen izraz
		for(int i = 0; i < splitExpressionArray.length; i++) {
			// ce pridemo do operatorja NOT, ki se ni bil spremenjen v psevdobinarni operator
			if(splitExpressionArray[i].equals("NOT") && ix == 0 && i == 0 || ix == 0 && splitExpressionArray[i].equals("NOT") && !splitExpressionArray[i - 1].equals("0")) {
				// ce NOT izraz, ki ga zelimo konvertirati stoji pred izrazom v oklepaju
				if(splitExpressionArray[i + 1].equals("(")) {
					// dodamo k koncnemu nizu
					finalString += "( 0 NOT ( ";
					// indeks prestavimo na term za oklepajem
					i += 2;
					// iscemo par oklepaja, pred katerim stoji NOT
					int bracketCounter = 0;
					while(true) {
						// ce smo prisli do gnezdenega oklepaja, ga dodamo v izhodni niz in 
						// povecamo stevec gnezdenja
						if(splitExpressionArray[i].equals("(")) {
							finalString += splitExpressionArray[i] + " ";
							bracketCounter++;
						// ce smo prisli do zaklepaja
						} else if(splitExpressionArray[i].equals(")")) {
							// ce zaklepaj ni del gnezdenega oklepaja
							if(bracketCounter == 0) {
								break;
							// sicer ga dodamo v koncni niz in zmanjsamo stevec gnezdenja
							} else {
								finalString += ") ";
								bracketCounter--;
							}
						// ostale terme dodajamo v rezultatni niz
						} else {
							finalString += splitExpressionArray[i] + " ";
						}
						// gremo na naslednji term
						i++;
					}
					// dodamo ustrezna zaklepaja
					finalString += ") ) ";
				// ce je za operatorjem NOT samo en term
				} else {
					// sestavimo izraz in povecamo indeks
					finalString += "( 0 NOT " + splitExpressionArray[i + 1] + " ) ";
					i++;
				}
			// ostale terme dodajamo v rezultat
			} else if(splitExpressionArray[i].equals("NOT") && ix > 0) {
				finalString += "NOT ";
				ix--;
			} else {
				finalString += splitExpressionArray[i] + " ";
			}
		}
		// odstranimo redundantni koncni presledek
		finalString = finalString.substring(0, finalString.length() - 1);
		// vrnemo rezultatni niz
		return finalString;
	}
	
	
	public static String formatInfixExpression2(String[] splitExpressionArray, int ix){
		List<String> list = Arrays.asList(splitExpressionArray);
		ArrayList<String> expression = new ArrayList<String>(list);
		// gremo po izrazu, dokler ne pridemo do prvega izraza AND
		int indexAnd = -1;
		Iterator<String> it = expression.iterator();
		while(it.hasNext()){
			indexAnd++;
			if(it.next().equals("AND")){
				if(ix == 0){
					break;
				} else {
					ix--;
				}
			}
		}
		// prvi primer: AND je obkrozen z dvema termoma
		if(!expression.get(indexAnd - 1).equals(")") && !expression.get(indexAnd + 1).equals("(")) {
			expression.add(indexAnd - 1, "(");
			expression.add(indexAnd + 2 + 1, ")");
			
			String result = "";
			it = expression.iterator();
			while(it.hasNext()){
				result += it.next() + " ";
			}
			
			return result;
			
		}
		
		// drugi primer: AND je na levi strani obdan z zaklepajem
		if(expression.get(indexAnd - 1).equals(")") && !expression.get(indexAnd + 1).equals("(")){
			int indexBracket = indexAnd - 1;
			int bracketCounter = -1;
			// poiscemo indeks pripadajocega oklepaja in na to mesto vstavimo oklepaj
			while(true) {
				String element = expression.get(indexBracket);
				// ce smo prisli do gnezdenega oklepaja, ga dodamo v izhodni niz in 
				// povecamo stevec gnezdenja
				if(element.equals(")")) {
					bracketCounter++;
				// ce smo prisli do zaklepaja
				} else if(element.equals("(")) {
					// ce zaklepaj ni del gnezdenega oklepaja
					if(bracketCounter == 0) {
						break;
					// sicer ga dodamo v koncni niz in zmanjsamo stevec gnezdenja
					} else {
						bracketCounter--;
					}
				// ostale terme dodajamo v rezultatni niz
				}
				// gremo na naslednji term
				indexBracket--;
			}
			expression.add(indexBracket, "(");
			
			expression.add(indexAnd + 2 + 1, ")");
			
			String result = "";
			it = expression.iterator();
			while(it.hasNext()){
				result += it.next() + " ";
			}
			
			return result;
		}
		
		// tretji primer: AND je na desni strani obdan z oklepajem
		if(!expression.get(indexAnd - 1).equals(")") && expression.get(indexAnd + 1).equals("(")){
			int indexBracket = indexAnd + 1;
			int bracketCounter = -1;
			// poiscemo indeks pripadajocega oklepaja in na to mesto vstavimo oklepaj
			it = expression.iterator();
			for(int i = 0; i < indexBracket; i++){
				it.next();
			}
			while(it.hasNext()) {
				String element = it.next();
				// ce smo prisli do gnezdenega oklepaja, ga dodamo v izhodni niz in 
				// povecamo stevec gnezdenja
				if(element.equals("(")) {
					bracketCounter++;
				// ce smo prisli do zaklepaja
				} else if(element.equals(")")) {
					// ce zaklepaj ni del gnezdenega oklepaja
					if(bracketCounter == 0) {
						break;
					// sicer ga dodamo v koncni niz in zmanjsamo stevec gnezdenja
					} else {
						bracketCounter--;
					}
				// ostale terme dodajamo v rezultatni niz
				}
				// gremo na naslednji term
				indexBracket++;
			}
			
			expression.add(indexAnd - 1, "(");
			expression.add(indexBracket + 1, ")");
			
			String result = "";
			it = expression.iterator();
			while(it.hasNext()){
				result += it.next() + " ";
			}
			
			return result;
		}
		
		
		// cetrti primer: AND je na obeh straneh obdan z oklepajema
		if(expression.get(indexAnd - 1).equals(")") && expression.get(indexAnd + 1).equals("(")){
			//case 4
			// oklepaj v desno ////////////////////////
			
			int indexBracketRight = indexAnd + 1;
			it = expression.iterator();
			for(int i = 0; i < indexBracketRight; i++){
				it.next();
			}
			int bracketCounter = -1;
			while(it.hasNext()) {
				String element = it.next();
				// ce smo prisli do gnezdenega oklepaja, ga dodamo v izhodni niz in 
				// povecamo stevec gnezdenja
				if(element.equals("(")) {
					bracketCounter++;
				// ce smo prisli do zaklepaja
				} else if(element.equals(")")) {
					// ce zaklepaj ni del gnezdenega oklepaja
					if(bracketCounter == 0) {
						break;
					// sicer ga dodamo v koncni niz in zmanjsamo stevec gnezdenja
					} else {
						bracketCounter--;
					}
				// ostale terme dodajamo v rezultatni niz
				}
				// gremo na naslednji term
				indexBracketRight++;
			}
			////////////////////////////////////////////
			// oklepaj v levo //////////////////////////
			int indexBracketLeft = indexAnd - 1;
			bracketCounter = -1;
			// poiscemo indeks pripadajocega oklepaja in na to mesto vstavimo oklepaj
			while(true) {
				String element = expression.get(indexBracketLeft);
				// ce smo prisli do gnezdenega oklepaja, ga dodamo v izhodni niz in 
				// povecamo stevec gnezdenja
				if(element.equals(")")) {
					bracketCounter++;
				// ce smo prisli do zaklepaja
				} else if(element.equals("(")) {
					// ce zaklepaj ni del gnezdenega oklepaja
					if(bracketCounter == 0) {
						break;
					// sicer ga dodamo v koncni niz in zmanjsamo stevec gnezdenja
					} else {
						bracketCounter--;
					}
				// ostale terme dodajamo v rezultatni niz
				}
				// gremo na naslednji term
				indexBracketLeft--;
			}
			
			expression.add(indexBracketLeft, "(");
			expression.add(indexBracketRight + 1, ")");
			
			String result = "";
			it = expression.iterator();
			while(it.hasNext()){
				result += it.next() + " ";
			}
			
			return result;
		}
		return null;
	}	
}