import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

// TODO komentarji
public class Naloga8 {	
	public static void main(String[] args) throws IOException {
		FileReader reader = new FileReader(args[0]);
		PrintWriter writer = new PrintWriter(args[1]);
		BufferedReader inStream = new BufferedReader(reader);
		
		String infixExpression = inStream.readLine();
		// kreiramo novo instanco razreda ExpressionTree, ki bo predstavljala izrazno drevo
		ExpressionTree tree = new ExpressionTree();
		
		// "markiramo elemente, po katerih zelimo lociti, brez da bi jih izgubili"
		String splitExpression = infixExpression.replace("(", "~(~");
		splitExpression = splitExpression.replace(")", "~)~");
		splitExpression = splitExpression.replace(" ", "~");
		splitExpression = splitExpression.replace("~~", "~");
		
		// pocistimo vodilno locilo in locimo
		String[] splitExpressionArray = splitExpression.replaceFirst("^~", "").split("~");
		
		// niz, ki bo predstavljal koncni izraz za pretvorbo v postfiksno obliko
		String finalString;
		// obdelati moramo vse morebitne NOT operaterje, ki so lahko v gnezdenih oklepjih
		// prestejemo oklepaje
		int NOTCounter = 0;
		for(int i = 0; i < splitExpressionArray.length; i++) {
			if(splitExpressionArray[i].equals("NOT")) {
				NOTCounter++;
			}
		}
		
		int ANDCounter = 0;
		for(int i = 0; i < splitExpressionArray.length; i++) {
			if(splitExpressionArray[i].equals("AND")) {
				ANDCounter++;
			}
		}
		
		for(int i = NOTCounter - 1; i >= 0; i--) {
			finalString = ExpressionFormatter.formatInfixExpression(splitExpressionArray, i);
			splitExpressionArray = finalString.split(" ");
		}
		
		for(int i = 0; i < ANDCounter; i++) {
			finalString = ExpressionFormatter.formatInfixExpression2(splitExpressionArray, i);
			splitExpressionArray = finalString.split(" ");
		}

		
		// procesiran infiksni izraz pretvorimo v postfiksno obliko
		String postfix = ExpressionFormatter.infixToPostfix(splitExpressionArray);
		// zgradimo drevo na postfiksnem izrazu (razdeljenem v tabelo po termih)
		Node root = tree.constructTree(postfix.split(" "));
		
		// apliciramo transformacijske operacije
		for(int i = 0; i < 1000; i++) {
			tree.transformTraversal(root, "deMorganOR", false);
			tree.transformTraversal(root, "deMorganAND", false);
			tree.transformTraversal(root, "doubleNOT", false);
			tree.transformTraversal(root, "notFALSE", false);
			tree.transformTraversal(root, "notTRUE", false);
		}
		// inicializiramo prazen niz, v katerega bomo hranili rezultat obhoda po drevesu
		String result = "";
		// drevo obhodimo in shranimo vsebino vozlisc
		result = tree.traversal(root, result);
		// odstranimo odvecni znak ','
		result = result.substring(0, result.length() - 1);
		
		// izpisemo rezultat obhoda
		writer.println(result);
		
		// zapremo tokove
		writer.close();
		inStream.close();
		reader.close();
	}
}