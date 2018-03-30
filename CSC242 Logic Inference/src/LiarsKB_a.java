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

public class LiarsKB_a extends KB {

	public LiarsKB_a() {
		super();
		Symbol amy = intern("Amy is truth-teller");
		Symbol bob = intern("Bob is truth-teller");
		Symbol cal = intern("Cal is truth-teller");

		add(new Implication(amy, new Conjunction(cal, amy)));
		add(new Implication(new Negation(amy), new Negation(new Conjunction(cal, amy))));
		add(new Implication(bob, new Negation(cal)));
		add(new Implication(new Negation(bob), cal));
		add(new Implication(cal, new Disjunction(bob, new Negation(amy))));
		add(new Implication(new Negation(cal), new Negation(new Disjunction(bob, new Negation(amy)))));

	}

	public static void main(String[] argv) {
		LiarsKB_a a = new LiarsKB_a();
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
