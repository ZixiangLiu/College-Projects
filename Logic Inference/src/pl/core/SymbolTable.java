package pl.core;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A SymbolTable maps names (Strings) to Symbols.
 */
public class SymbolTable {
	
	protected HashMap<String,Symbol> symbols = new HashMap<>();
	
	/**
	 * Returns the PropositionSymbol with the given NAME, creating one
	 * and adding it to this SymbolTable if necessary.
	 */
	public Symbol intern(String name) {
		Symbol sym = symbols.get(name);
		if (sym == null) {
			sym = new Symbol(name);
			symbols.put(name, sym);
		}
		return sym;
	}
	
	/**
	 * Return the number of Symbols stored in this SymbolTable.
	 */
	public int size() {
		return symbols.size();
	}

	/**
	 * Return a Collection containing the Symbols stored in this
	 * SymbolTable.
	 */
	public ArrayList<Symbol> symbols() {
		ArrayList<Symbol> temp = new ArrayList<Symbol>(symbols.values());
		return temp;
	}
	
}


