import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

// TODO: komentarji
public class Naloga7 {	
	public static void main(String[] args) throws IOException {
		// Definicija in inicializacija tokov za branje podatkov iz datotek
		FileReader reader = new FileReader(args[0]);
		PrintWriter writer = new PrintWriter(args[1]);
		BufferedReader inStream = new BufferedReader(reader);
		
		// branje surovega infiksnega izraza iz datoteke.
		String infixExpression = inStream.readLine();
		
		// kreiramo novo instanco razreda ExpressionTree, ki bo predstavljala izrazno drevo.
		ExpressionTree tree = new ExpressionTree();
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// markiramo elemente, po katerih zelimo lociti, brez da bi jih izgubili.
		String splitExpression = infixExpression.replace("(", "~(~");
		splitExpression = splitExpression.replace(")", "~)~");
		splitExpression = splitExpression.replace(" ", "~");
		splitExpression = splitExpression.replace("~~", "~");
		
		// pocistimo vodilno locilo in locimo.
		String[] splitExpressionArray = splitExpression.replaceFirst("^~", "").split("~");
		
		// niz, ki bo predstavljal koncni izraz za pretvorbo v postfiksno obliko
		String finalString;
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// pri formatiranju izraza obdelati moramo vse morebitne NOT operaterje v izrazu.
		// prestejemo NOT operatorje.
		int NOTCounter = 0;
		for(int i = 0; i < splitExpressionArray.length; i++) {
			if(splitExpressionArray[i].equals("NOT")) {
				NOTCounter++;
			}
		}
		
		// pri formatiranju moramo obdelati vse morebitne AND operatorje.
		// prestejemo AND operatorje.
		int ANDCounter = 0;
		for(int i = 0; i < splitExpressionArray.length; i++) {
			if(splitExpressionArray[i].equals("AND")) {
				ANDCounter++;
			}
		}
		
		// gremo cez vse NOT operatorje in jih pretvorimo v "psevdo binarne" operatorje
		// ker so NOT operatorji desno asociativni, storimo to od zadnjega NOT operatorja nazaj.
		for(int i = NOTCounter - 1; i >= 0; i--) {
			finalString = ExpressionFormatter.formatInfixExpression(splitExpressionArray, i);
			splitExpressionArray = finalString.split(" ");
		}
		
		// izraze povezane z AND operatorji obdamo z oklepaji
		for(int i = 0; i < ANDCounter; i++) {
			finalString = ExpressionFormatter.formatInfixExpression2(splitExpressionArray, i);
			splitExpressionArray = finalString.split(" ");
		}
		
		
		// preberemo vse operande v seznam parov
		String[] searchArray = splitExpressionArray;
		ArrayList<Node> terms = new ArrayList<Node>();
		
		// gremo cez po termih razclenjen izraz in identificiramo vse unikatne operande
		for(int i = 0; i < searchArray.length; i++) {
			if(!searchArray[i].equals("(") && !searchArray[i].equals(")") && !searchArray[i].equals("NOT") && 
					!searchArray[i].equals("AND") && !searchArray[i].equals("OR") && !searchArray[i].equals("0") &&
						!searchArray[i].equals("TRUE") && !searchArray[i].equals("FALSE") && !terms.contains(new Node(searchArray[i]))) {
				terms.add(new Node(searchArray[i]));
			}
		}
		
		String postfix = ExpressionFormatter.infixToPostfix(splitExpressionArray);
		int[] var;
		Node root = tree.constructTree(postfix.split(" "), terms);
		
		int resCounter = 0;
		
		for(int i = 0; i < terms.size(); i++) {
			terms.get(i).logicalValue = "FALSE";
		}
		
		int numVariations = (int)Math.pow(2.0, (double)terms.size());
		for(int i = 0; i < numVariations; i++){
			String bin = Integer.toBinaryString(i);
			
			var = bin.chars().map(a->a-'0').toArray();
			
			for(int j = var.length - 1; j >=0; j--) {
				//System.out.println("length = " + var.length);
				terms.get(var.length - 1 - j).logicalValue = (var[j] == 1) ? "TRUE" : "FALSE";
			}
			
			String result = tree.evaluate(root);
			if(result.equals("TRUE")) {
				resCounter++;
			}
		}
		
		// v datoteko zapisemo rezultat
		writer.println(resCounter);
		
		// zapremo tokove
		writer.close();
		inStream.close();
		reader.close();
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
	}
}