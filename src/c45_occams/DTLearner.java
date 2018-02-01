package c45_occams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Main class for decision tree learner
public class DTLearner {
	
	// Extracts data set from ARFF file
	@SuppressWarnings("resource")
	private static DataSet read(String filename) {
		DataSet data = new DataSet();
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filename));
		} catch (FileNotFoundException exception) {
			System.out.println("Error: could not find file");
			System.exit(1);
		}
		String line;
		scanner.nextLine(); // Skip first line, assumes first line is @releation
		while (scanner.hasNextLine()) {
			line = scanner.nextLine();
			if (line.equals("@data"))
				break;
			data.addAttribute(line);
		}
		while (scanner.hasNextLine())
			data.addInstance(scanner.nextLine());
                if(data.attributes.get(0).values.size() == 0){
                    System.err.println("Reading file failed.");
                    System.exit(0);
                }
                
		return data;
	}
	
//	public static void main(String[] args) {
//		DataSet trainingSet = read(args[0]);
//		DataSet testingSet = read(args[1]);
//		int stopValue = Integer.parseInt(args[2]);
//		
//		DecisionTree ID3 = new DecisionTree(stopValue);
//		ID3.train(trainingSet);
//		ID3.test(testingSet.instances);
//	}
        
        public static void main(String[] args) {
		DataSet trainingSet = read("binexportbinary.arff");
		DataSet testingSet = read("binexportbinary.arff");
		int minInstancesPerNode = Integer.parseInt("1");
		
		DecisionTree ID3 = new DecisionTree(minInstancesPerNode);
		ID3.train(trainingSet);
		ID3.test(testingSet.instances);
	}
	
}
