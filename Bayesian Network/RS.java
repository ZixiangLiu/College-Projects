import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import bn.core.*;
import bn.parser.*;

// Rejection Sampling
// RS.java

public class RS {
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
		System.out.println(rejectionSampling(network.getVariableByName(args[2]),e,network, Integer.parseInt(args[0])));
	}

	/**
	 * This method is implemented from AIMA Figure 14.14
	 * @param  X  the query RandomVariable
	 * @param  e  evidence Assignment
	 * @param  bn BayesianNetwork
	 * @param  N  the number of iteration
	 * @return    the Distribution of X given e
	 */
	public static Distribution rejectionSampling(RandomVariable X, Assignment e, BayesianNetwork bn, int n){
		Distribution N = new Distribution(X);
		// initialize W to all zeros
		for (int i = 0; i < X.getDomain().size(); i++){
			N.put(X.getDomain().get(i), 0);
		}

		// iterate N times
		for(int j = 0; j < n; j++){
			// generate random assignment
			// the method is implemented in BayesianNetwork.java
			Assignment s = bn.randomEvent();
			// consistent method is also in BayesianNetwork.java
			if (bn.consistent(s, e)){
				Object xval = s.get(X);
				N.put(xval, N.get(xval)+1);
			}
		}

		//normalize
		N.normalize();
		return N;
	}
}
