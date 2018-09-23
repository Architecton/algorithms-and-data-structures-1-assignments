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
    
    void transformTraversal(Node t, String action, boolean change){
    	if(t == null) {
    		return;
    	}
    	if(change || !isOperator(t.value)) {
    		return;
    	}
    	if(t != null){
    		switch(action){
    			case "deMorganOR":
    				if(t.value.equals("NOT")){
    					change = ExpressionTree.deMorganOR(t);
    				}
    				transformTraversal(t.left, "deMorganOR", change);
					transformTraversal(t.right, "deMorganOR", change);
    				break;
    			case "deMorganAND":
    				if(t.value.equals("NOT")){
    					ExpressionTree.deMorganAND(t);
    				}
    				transformTraversal(t.left, "deMorganAND", change);
					transformTraversal(t.right, "deMorganAND", change);
    				break;
    			case "doubleNOT":
    				if(!t.value.equals("NOT")){
    					change = ExpressionTree.doubleNot(t);
    				}
    				transformTraversal(t.left, "doubleNOT", change);
					transformTraversal(t.right, "doubleNOT", change);
    				break;
    			case "notTRUE":
    				if(t.value.equals("NOT")){
    					ExpressionTree.notTRUE(t);
    				}
    				transformTraversal(t.left, "notTRUE", change);
					transformTraversal(t.right, "notTRUE", change);
    				break;
    			case "notFALSE":
    				if(t.value.equals("NOT")){
    					ExpressionTree.notFALSE(t);
    				}
    				transformTraversal(t.left, "notFALSE", change);
					transformTraversal(t.right, "notFALSE", change);
					break;
    		}
    	}
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
    
    public static boolean deMorganOR(Node node) {
    	// ce vozlisce ima sinove (je operator)
    	if(node != null && node.right != null){
    		if(node.value.equals("NOT") && node.right.value.equals("OR")){
    			Node t1, t2;
    			node.value = "AND";
    			t1 = new Node("NOT");
    			t2 = new Node("NOT");
    			t1.left = node.right.left;
    			t2.left = node.right.right;
    			node.left = t1;
    			node.right = t2;
    			return true;
    		}
    	} else if(node != null && node.left != null) {
    		if(node.value.equals("NOT") && node.left.value.equals("OR")){
    			Node t1, t2;
    			node.value = "AND";
    			t1 = new Node("NOT");
    			t2 = new Node("NOT");
    			t1.left = node.left.left;
    			t2.left = node.left.right;
    			node.left = t1;
    			node.right = t2;
    			return true;
    		}
    	}
    	return false;
    }
    
    public static boolean deMorganAND(Node node) {
    	// ce vozlisce ima sinove (je operator)
    	if(node != null && node.right != null){
    		if(node.value.equals("NOT") && node.right.value.equals("AND")){
    			Node t1, t2;
    			node.value = "OR";
    			t1 = new Node("NOT");
    			t2 = new Node("NOT");
    			t1.left = node.right.left;
    			t2.left = node.right.right;
    			node.left = t1;
    			node.right = t2;
    			
    			return true;
    		}
    	} else if(node != null && node.left != null) {
    		if(node.value.equals("NOT") && node.left.value.equals("AND")){
    			Node t1, t2;
    			node.value = "OR";
    			t1 = new Node("NOT");
    			t2 = new Node("NOT");
    			t1.left = node.left.left;
    			t2.left = node.left.right;
    			node.left = t1;
    			node.right = t2;
    			
    			return true;
    		}
    	}
    	return false;
    }
    
    public static boolean doubleNot(Node node){
    	boolean changed1 = false;
    	boolean changed2 = false;
    	if(node != null && node.left.value.equals("NOT") || node.right.value.equals("NOT")){
    		// 1/2. varianta
    		if(node.left.value.equals("NOT") && node.left.left != null && node.left.left.value.equals("NOT")){
    			if(node.left.left.left != null) {
    				node.left = node.left.left.left;
    			} else {
    				node.left = node.left.left.right;
    			}    			
    			changed1 = true;
    		// 3/4. varianta
    		} else if(node.left.value.equals("NOT") && node.left.right != null && node.left.right.value.equals("NOT")) {
    			if(node.left.right.left != null) {
    				node.left = node.left.right.left;
    			} else {
    				node.left = node.left.right.right;
    			}
    			changed1 = true;
    		}
    		
    		// 5/6. varianta
    		if(node.right.value.equals("NOT") && node.right.right != null && node.right.right.value.equals("NOT")){
    			
    			if(node.right.right.right != null) {
    				node.right = node.right.right.right;
    			} else {
    				node.right = node.right.right.left;
    			}
    			changed2 = true;
    		
    		// 7/8. varianta
    		} else if(node.right.value.equals("NOT") && node.right.left != null && node.right.left.value.equals("NOT")) {
    			if(node.right.left.left != null) {
    				node.right = node.right.left.left;
    			} else {
    				node.right = node.right.left.right;
    			}
    			changed2 = true;
    		}
    		return changed1 || changed2;
    	}
    	return false;
    }
    
    public static boolean notTRUE(Node node){
    	if(node == null) {
    		return false;
    	}
    	if(node.value.equals("NOT") && node.right != null && node.right.value.equals("TRUE")){
    		node.value = "FALSE";
    		node.left = node.right = null;
    		return true;
    	} else if(node.value.equals("NOT") && node.left != null && node.left.value.equals("TRUE")) {
    		node.value = "FALSE";
    		node.left = node.right = null;
    		return true;
    	}
    	return false;
    }
    
    public static boolean notFALSE(Node node){
    	if(node != null && node.value.equals("NOT") && node.right != null && node.right.value.equals("FALSE")){
    		node.value = "TRUE";
    		node.left = node.right = null;
    		return true;
    	} else if(node != null && node.value.equals("NOT") && node.left != null && node.left.value.equals("FALSE")) {
    		node.value = "TRUE";
    		node.left = node.right = null;
    		return true;
    	}
    	return false;
    }
}