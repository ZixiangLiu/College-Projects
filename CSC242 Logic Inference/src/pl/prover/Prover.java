/* CSC242
 * Project 2
 * 03.05.2018
 * Yukun Chen, Zixiang Liu, Yifei Yang
 * Prover.java
 */

package pl.prover;

import java.util.*;
import pl.core.*;
import java.util.*;

public class Prover {
	
	/**
	 * Return true if the given KB entails the given Sentence.
	 */
	public boolean entails(KB kb, Sentence alpha) {
		ArrayList<Symbol> symbols = new ArrayList(kb.symbols());
		Model model = new Model();
		return TTCheckAll(kb, alpha, symbols, model);
	}

	public boolean TTCheckAll(KB kb, Sentence alpha, List<Symbol> symbols, Model model) {		// helper function for model checking
		if (symbols.isEmpty()) {
			if(model.satisfies(kb)){
				return model.satisfies(alpha);		// if model fits KB, check whether it fits alpha
			}
			else
				return true;						// if model doesn't fit KB, doesn't count in entailment, simply return ture
		}
		else{
			Symbol first = symbols.get(0);
			List<Symbol> rest = symbols.subList(1, symbols.size());
			model.set(first, true);
			boolean result = TTCheckAll(kb, alpha, rest, model);
			model.set(first, false);
			return result && TTCheckAll(kb, alpha, rest, model);	// return the conjunction result of all model
		}
	}
}
