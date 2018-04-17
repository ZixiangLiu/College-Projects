import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import bn.core.*;
import bn.parser.*;

// Likelihood Weighting
// LW.java

public class LW {
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException{
		BayesianNetwork network;

		// read file
		String[] File = args[1].split("\\.");
		if(File[1].compareToIgnoreCase("bif")==0){
			BIFParser parser = new BIFParser(new FileInputStream(args[1]));
			network = parser.parseNetwork();
		}
		else {
			XMLBIFParser parser = new XMLBIFParser();
			network = parser.readNetworkFromFile(args[1]);
		}
		int temp = (args.length-3)/2;
		Assignment e = new Assignment();
		//assign evidences to the assignment
		for(int i = 0; i < temp; i++){
			e.set(network.getVariableByName(args[3+2*i]),args[4+2*i]);
		}
		// run the algorithm and print
		System.out.println(likelihoodWeighting(network.getVariableByName(args[2]),e,network, Integer.parseInt(args[0])));
	}

	/**
	 * This method is implemented from AIMA Figure 14.15
	 * @param  X  the query RandomVariable
	 * @param  e  evidence Assignment
	 * @param  bn BayesianNetwork
	 * @param  N  the number of iteration
	 * @return    the Distribution of X given e
	 */
	public static Distribution likelihoodWeighting(RandomVariable X, Assignment e, BayesianNetwork bn, int N){
		Distribution W = new Distribution(X);
		// initialize W to all zeros
		for (int i = 0; i < X.getDomain().size(); i++){
			W.put(X.getDomain().get(i), 0);
		}

		// iterate N times
		for(int j = 0; j < N; j++){
			/** in this implementation
			 * the weightedSample does not return
			 * because java does not support returning two elements
			 * so instead we implement it by W reference entered into the method
			 */
			weightedSample(bn, e, X, W);
		}

		//normalize
		W.normalize();
		return W;
	}

	public static void weightedSample(BayesianNetwork bn, Assignment e, RandomVariable X, Distribution W){
		double weight = 1;
		Assignment s = e.copy();

		for (RandomVariable xi: bn.getVariableListTopologicallySorted()){
			if (e.containsKey(xi)){
				weight = weight * bn.getProb(xi, s);
			}else {
				s.put(xi, bn.randomAssign(xi, s));
			}
		}

		Object xval = s.get(X);
		W.put(xval, W.get(xval)+weight);
	}

}
