/* CSC242
 * Project 2
 * 03.05.2018
 * Yukun Chen, Zixiang Liu, Yifei Yang
 * Solver.java
 */

package pl.prover;

import pl.core.KB;
import pl.core.Model;
import java.lang.Math;
import java.util.Collection;

public interface Solver {
	
	/**
	 * If the given KB is satisfiable, return a satisfying Model.
	 */
	public Model solve(KB kb) {
		int size = kb.symbols().length();
		Collection<symbol> symbols = kb.symbols();			// generate all model symbol and sentences
		Collection<Sentence> sentences = kb.sentences();

		for (int i=0; i<Math.pow(2, size); i++) {		// use binary to get Truth table
			Model model = new Model();
			String binary = Integer.toBinaryString(i);
			for (int j=0; j<size; j++) {
				boolean temp;
				if (j<binary.length())
					temp = binary[j] == '1'? true, false;
				else
					temp = false
				model.set(symbols.get(j), temp);
			}
			if (model.satisfies(kb))
				return model;				// if the generated model satisfies KB, return the model
		}
	}
	return null;			// if no model satisfies KB, return null
}
