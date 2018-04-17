/* CSC242
 * Project 2
 * 03.05.2018
 * Yukun Chen, Zixiang Liu, Yifei Yang
 */

import pl.core.Disjunction;
import pl.core.Conjunction;
import pl.core.Implication;
import pl.core.KB;
import pl.core.Negation;
import pl.core.Symbol;
import pl.prover.Prover;
import pl.resolution.*;

public class HornClausesKB extends KB {

	public HornClausesKB() {
		super();
		Symbol mythical = intern("mythical");
		Symbol mortal = intern("mortal");
		Symbol mammal = intern("mammal");
		Symbol magical = intern("magical");
		Symbol horned = intern("horned");

		add(new Implication(mythical, new Negation(mortal)));
		add(new Implication(new Negation(mythical), new Conjunction(mortal, mammal)));
		add(new Implication(new Disjunction(new Negation(mortal), mammal), horned));
		add(new Implication(horned, magical));

	}

	public static void main(String[] argv) {
		HornClausesKB a = new HornClausesKB();
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
