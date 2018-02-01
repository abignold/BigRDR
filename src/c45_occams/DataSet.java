package c45_occams;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedHashMap;

// Stores a set of instances
public class DataSet {

	// Ordered list of instances
	public ArrayList<Instance> instances;
	public ArrayList<Attribute> attributes;
	
	public DataSet() {
		instances = new ArrayList<Instance>();
		attributes = new ArrayList<Attribute>();
	}
	
	// Add instance to data set (add all attributes before doing this)
	public void addInstance(String line) {			
		ArrayList<String> features = new ArrayList<String>(Arrays.asList(line.split(",")));
		String label = features.remove(features.size() - 1);
		LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
		for (int i = 0; i < features.size(); i++)
			values.put(attributes.get(i).name, features.get(i));
		instances.add(new Instance(values, label));
	}
	
	// Add attribute to data set
	public void addAttribute(String line) {
            if(!line.trim().toLowerCase().startsWith("@attribute")){ // if line does not start with @attribute then return
                return;
            }
            
		// Break line into attributes and values
		ArrayList<String> splitLine = new ArrayList<String>(Arrays.asList(line.split(" "))); // 0 = @ attribute, 1 = label, 2 = type of attribute or attibute labels.
                splitLine.remove(0); // remove the @attribute, not needed anymore
                String name = splitLine.remove(0);
                String[] valueSplit = splitLine.remove(0).replaceAll("\\{", "").replaceAll("}", "").split(",");
		ArrayList<String> values = new ArrayList<>();
		for (String value : valueSplit)
			values.add(value.trim().replaceAll("'", "").replaceAll("}", ""));
		// Update list
		attributes.add(new Attribute(values, name));
	}
        
//        public void addAttribute(String line) {
//		// Break line into attributes and values
//		ArrayList<String> splitLine = new ArrayList<String>(Arrays.asList(line.split(" ")));
//		splitLine.remove("@attribute");
//		splitLine.remove("{");
//		ArrayList<String> values = new ArrayList<String>();
//		for (String value : splitLine)
//			values.add(value.replaceAll("'", "").replaceAll(",", "").replaceAll("}", ""));
//		// Update list
//		String name = values.remove(0);
//		attributes.add(new Attribute(values, name));
//	}
	
	// Developer tool: print data set
	public void print() {
		for (Attribute attribute : attributes)
			attribute.print();
		for (Instance instance : instances)
			instance.print();
	}
	
}
