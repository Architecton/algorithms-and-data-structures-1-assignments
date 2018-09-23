import java.util.Stack;

// razred, ki predstavlja izrazno drevo
class ExpressionTree {
 
    // isOperator: pomozna metoda, ki preveri, ce je podani niz operator
	boolean isOperator(String s) {
		if(s.equals("NOT") || s.equals("AND") || s.equals("OR")) {
			return true;
		}
		return false;
	}
	
    // traversal: metoda za obhod po drevesu, ki vrne vrednosti vozlisc strnjene v niz
    String traversal(Node t, String result) {
    	// ce vozlisce t obstaja
    	if(t != null) {
    		// ignoriramo redundantna vozlisca, ki so posledica psevdobinarnih NOT izrazov
    		result += t.value;
    		result += ',';
        	// rekurzivni klic za levega sina
        	result = traversal(t.left, result);
        	// rekurzivni klic za desnega sina
        	result = traversal(t.right, result);
    	}
    	return result;
    }
    
    // findHeight: metoda, ki vrne visino tega izraznega drevesa
    public int findHeight(Node node) {
        if (node == null) {
            return 0;
        } else {
        	return 1 + Math.max(findHeight(node.left), findHeight(node.right));
        }
    }

    // constructTree: metoda, ki vrne koren drevesa, zgrajenega na podanem postfiksnem izrazu
    Node constructTree(String postfix[]) {
		// Kreiramo novo instanco sklada, ki hrani vozlisca drevesa
    	Stack<Node> st = new Stack<Node>();
    	// definiramo tri instance razreda Node
    	Node t, t1, t2;
    	
        // gremo cez vsak term izraza v postfiksni obliki
        for(int i = 0; i < postfix.length; i++) {
 
        	// Ce je term operand, ga damo na sklad.
        	if (!isOperator(postfix[i])) {
        		t = new Node(postfix[i]);
        		st.push(t);
        	// ce je term operator
            } else {
            	// inicializiramo vozlisce z operatorjem
            	t = new Node(postfix[i]);
            	// S sklada pridobimo dva operanda.
            	t1 = st.pop();
            	t2 = st.pop();
            	
                //  Ce nista redundantna operanda psevdobinarnega izraza NOT, ju naredimo
            	// sinova vozlisca, ki predstavlja trenutni operator
            	if(!t1.value.equals("0")) {
            		t.right = t1;
            	}
            	if(!t2.value.equals("0")) {
            		t.left = t2;
            	}
            	
                // ta podizraz damo na sklad
            	st.push(t);
            }
        }
        
        // na vrhu sklada imamo sedaj koren izraznega drevesa
        t = st.peek();
        // odstranimo vrh sklada
        st.pop();
        // vrnemo koren izraznega drevesa
        return t;
    }
}