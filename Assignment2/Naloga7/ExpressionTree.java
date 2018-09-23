import java.util.ArrayList;	
import java.util.Stack;
import java.util.Iterator;

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
	String evaluate(Node node) {
		// ce vozlisce ima sinove (je operator)
	     if(node.left != null || node.right != null){
	    	 String val = null;
	         String operator = node.value; 
	    	 
	         String leftVal = evaluate(node.left);
	         if(operator.equals("AND") && leftVal.equals("FALSE")) {
	        	 return "FALSE";
	         } else if(operator.equals("OR") && leftVal.equals("TRUE")) {
	        	 return "TRUE";
	         }
	         String rightVal = evaluate(node.right);

	          
	          // find operation symbol for node and use it as
	          if(operator.equals("NOT")){
	               if(rightVal.equals("TRUE") || leftVal.equals("TRUE")){
	                    val = "FALSE";
	               } else {
	                    val = "TRUE";
	               }
	          } else if(operator.equals("AND")){
	               if(leftVal.equals("TRUE") && rightVal.equals("TRUE")){
	                    val = "TRUE";
	               } else {
	                    val = "FALSE";
	               }
	          } else if(operator.equals("OR")){
	               if(leftVal.equals("FALSE") && rightVal.equals("FALSE")){
	                    val = "FALSE";
	               } else {
	                    val = "TRUE";
	               }
	          }
	          return val;
	     } else {
	          return node.logicalValue;
	     }
	}

    // constructTree: metoda, ki vrne koren drevesa, zgrajenega na podanem postfiksnem izrazu
    Node constructTree(String postfix[], ArrayList<Node> terms) {
		
    	// Kreiramo novo instanco sklada, ki hrani vozlisca drevesa
    	Stack<Node> st = new Stack<Node>();
    	
    	// definiramo tri instance razreda Node
    	Node t, t1, t2;
    	
    	// kreiramo instanco iteratorja za traverzijo seznama unikatnih termov
    	Iterator<Node> it;
    	
        // gremo cez vsak term izraza v postfiksni obliki
        for(int i = 0; i < postfix.length; i++) {
 
        	// Ce je term operand, ga damo na sklad.
        	if(!isOperator(postfix[i])) {
        		String val = postfix[i];
        		// indeks operanda, ki ga primerjamo z termom v izrazu
        		int index = -1;
        		// gremo cez vse elemente seznama unikatnih termov
        		it = terms.iterator();
        		if(postfix[i].equals("0")) {
        			t = new Node("0");
        			st.push(t);
        		} else if(postfix[i].equals("TRUE") || postfix[i].equals("FALSE")) {
        			t = new Node(postfix[i], postfix[i]);
        			st.push(t);
        		} else {
	        		while(it.hasNext()) {
	        			// shranimo vrednost naslednjega terma v seznamu
	        			val = it.next().value;
	        			index++;
	        			// ce smo nasli pravi term
	        			if(val.equals(postfix[i])) {
	        				// inicializiramo bodoci list drevesa s kazalcem na ta unikaten term
	        				t = terms.get(index);
	        				// vozlisce damo na sklad
	        				st.push(t);
	        				break;
	        			}
	        		}
        		}
        	// ce je term operator
            } else {
            	// inicializiramo vozlisce z operatorjem
            	t = new Node(postfix[i]);
            	// S sklada pridobimo dva operanda.
            	t1 = st.pop();
            	t2 = st.pop();
            	
            	// Naredimo ju sinova vozlisca, ki predstavlja trenutni operator
            	t.right = t1;
           		t.left = t2;
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
    
    /*public static void getLeaves(Node node, ArrayList<Node> leaves)	{
    	getLeaves(node.left, leaves);
        if(node.left == null && node.right == null) {
        	leaves.add(node)
        }
        getLeaves(node.right, leaves);
    	
    }*/
}