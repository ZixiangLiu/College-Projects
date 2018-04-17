/* CSC242
 * Project 2
 * 03.05.2018
 * Yukun Chen, Zixiang Liu, Yifei Yang
 */

import pl.core.Biconditional;
import pl.core.Disjunction;
import pl.core.KB;
import pl.core.Negation;
import pl.core.Symbol;
import pl.prover.Prover;
import pl.resolution.*;

public class WumpusWorldKB extends KB {

	public WumpusWorldKB() {
		super();
		Symbol p11 = intern("P1,1");
		Symbol p12 = intern("P1,2");
		Symbol p21 = intern("P2,1");
		Symbol p22 = intern("P2,2");
		Symbol p31 = intern("P3,1");
		Symbol b11 = intern("B1,1");
		Symbol b21 = intern("B2,1");

		add(new Negation(p11));
		add(new Biconditional(b11, new Disjunction(p12, p21)));
		add(new Biconditional(b21, new Disjunction(p12, new Disjunction(p22, p31))));
		add(new Negation(b11));
		add(b21);
	}

	public static void main(String[] argv) {
		WumpusWorldKB a = new WumpusWorldKB();
		System.out.println("Given: ");
		a.dump();

		Prover pv = new Prover();
		System.out.println("\nUsing TT-entails.\nWe have: ");
		long startTime = System.nanoTime();
		for (int i=0; i < a.symbols().size(); i++){
			System.out.println(a.getSymbol(i) + " " + pv.entails(a, a.getSymbol(i)));
			System.out.println(new Negation(a.getSymbol(i)) + " " + pv.entails(a, new Negation(a.getSymbol(i))));
		}
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		String strDouble = String.format("%.2f", (double)duration/1000000);
		System.out.println("Runtime of TT-entails: " + strDouble + "ms");

		PLResolution plr = new PLResolution();
		System.out.println("\nUsing Resolution.\nWe have: ");
		startTime = System.nanoTime();
		for (int i=0; i < a.symbols().size(); i++){
			System.out.println(a.getSymbol(i) + " " + plr.resolution(a, a.getSymbol(i)));
			System.out.println(new Negation(a.getSymbol(i)) + " " + plr.resolution(a, new Negation(a.getSymbol(i))));
		}
		endTime = System.nanoTime();
		duration = (endTime - startTime);
		strDouble = String.format("%.2f", (double)duration/1000000);
		System.out.println("Runtime of Resolution: " + strDouble + "ms");
	}

}
