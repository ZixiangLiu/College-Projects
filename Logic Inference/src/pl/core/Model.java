/* CSC242
 * Project 2
 * 03.05.2018
 * Yukun Chen, Zixiang Liu, Yifei Yang
 * Model.java
 */

package pl.core;

import java.util.*;

/**
 * A Model is an assignment of boolean values (true or false) to
 * PropositionalSymbols.
 */

public class Model {

	private HashMap<Symbol, Boolean> symbols;

	public Model() {
		this.symbols = new HashMap<Symbol, Boolean>();
	}

	/**
	 * Set the value assigned to the given PropositionSymbol in this
	 * Model to the given boolean VALUE.
	 */
	public void set(Symbol sym, boolean value) {
		symbols.put(sym, value);
	}

	/**
	 * Returns the boolean value associated with the given PropositionalSymbol
	 * in this Model.
	 */
	public boolean get(Symbol sym) {
		return symbols.get(sym);
	}
	
	/**
	 * Return true if this Model satisfies (makes true) the given KB.
	 */
	public boolean satisfies(KB kb) {
		ArrayList<Sentence> sentences = new ArrayList(kb.sentences());

		for (int j=0; j<sentences.size(); j++){
			if (!sentences.get(j).isSatisfiedBy(this)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Return true if this Model satisfies (makes true) the given Sentence.
	 */
	public boolean satisfies(Sentence sentence) {
		return sentence.isSatisfiedBy(this);
	}
	
	/**s
	 * Print the assignments in this Model to System.out.
	 */
	public void dump() {
		for (Symbol name: symbols.keySet()){
            String key =name.toString();
            String value = symbols.get(name).toString();  
            System.out.println(key + " = " + value);  
		}
	}

}
