import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Naloga6 {	
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
		
		// procesiran infiksni izraz pretvorimo v postfiksno obliko (Shunting Rail Algorithm).
		String postfix = ExpressionFormatter.infixToPostfix(splitExpressionArray);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// zgradimo drevo na postfiksnem izrazu (razdeljenem v tabelo po termih)
		Node root = tree.constructTree(postfix.split(" "));
		
		// inicializiramo prazen niz, v katerega bomo hranili rezultat obhoda po drevesu.
		String result = "";
		
		// drevo obhodimo in shranimo vsebino vozlisc.
		result = tree.traversal(root, result);
		
		// odstranimo odvecni repni znak ','.
		result = result.substring(0, result.length() - 1);
		
		// rezultat obhoda zapisemo v datoteko.
		writer.println(result);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// izracunamo visino drevesa
		int height = tree.findHeight(root);
		// visino drevesa zapisemo v datoteko
		writer.println(height);
		
		// zapremo tokove
		writer.close();
		inStream.close();
		reader.close();
		
		/////////////////////////////////////////////////////////////////////////////////////////////////////////
	}
}