import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import bn.core.*;
import bn.parser.*;

// Exact Inference
// EI.java

public class EI {
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException{
		BayesianNetwork network;

		// read file
		String[] File = args[0].split("\\.");
		if(File[1].compareToIgnoreCase("bif")==0){
			BIFParser parser = new BIFParser(new FileInputStream(args[0]));
			network = parser.parseNetwork();
		}
		else {
			XMLBIFParser parser = new XMLBIFParser();
			network = parser.readNetworkFromFile(args[0]);
		}
		int temp = (args.length-2)/2;
		Assignment e = new Assignment();
		//assign evidences to the assignment
		for(int i = 0; i < temp; i++){
			e.set(network.getVariableByName(args[2+2*i]),args[3+2*i]);
		}
		// run the algorithm and print
		System.out.println(enumeration(network.getVariableByName(args[1]),e,network));
	}

	/**
	 * This method is implemented from AIMA Figure 14.9
	 * @param  X  the query RandomVariable
	 * @param  e  evidence Assignment
	 * @param  bn BayesianNetwork
	 * @return    the Distribution of X given e
	 */
	public static Distribution enumeration(RandomVariable x, Assignment e, BayesianNetwork bn){
		Distribution Q = new Distribution(x);

		//compute values for distribution
		for(int i = 0; i < x.getDomain().size(); i++){
			e.put(x,x.getDomain().get(i));
			Q.put(x.getDomain().get(i),enumerationAll(bn.getVariableList(),e,bn));
			e.remove(x);
		}

		//normalize
		Q.normalize();
		return Q;
	}

	public static double enumerationAll(List<RandomVariable> vars, Assignment e, BayesianNetwork bn){
		if(vars.isEmpty()) return 1.0;  //the base case
		RandomVariable Y = vars.get(0);	//the first variable
		//Y is not hidden variable
		if(e.variableSet().contains(Y)){
			double x1 = bn.getProb(Y,e);
			return x1*enumerationAll(vars.subList(1,vars.size()),e,bn);
		}

		//Y is hidden variable
		else {
			double value = 0;
			for (int i = 0; i < Y.getDomain().size(); i++){
				Assignment s = e.copy();
				s.put(Y, Y.getDomain().get(i));
				value = value + bn.getProb(Y, s)*enumerationAll(vars.subList(1,vars.size()), s, bn);
			}
			return value;
		}
	}

}
