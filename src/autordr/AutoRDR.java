package autordr;

import bigrdr.CaseList;
import bigrdr.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author abign
 * 
 * Automatically creates an RDR Tree from a RDR case list
 */
public class AutoRDR {

    private Classifier wekaClassifier;
    private RDRTree rdrTree;
    private Evaluation wekaEvaluation;
    
    public static void main(String[] args) throws ScriptException {
        String[][] matrix = {
            {"sepallength", "sepalwidth", "petallength", "petalwidth", "classification"},
            {"real", "real", "real", "real", "string"},
            {"5.1", "3.8", "1.9", "0.4", "Iris-setosa"},
            {"7.0", "3.2", "4.7", "1.4", "Iris-versicolor"},
            {"5.9", "3.0", "5.1", "1.8", "Iris-virginica"}
        };
        CaseList caseList = CaseList.createFromArray(matrix);
//        CaseList caseList = CaseList.createFromFile("iris.csv");
        RDRTree rdr = AutoRDR.createRDR(caseList);
        System.out.println(rdr);
        rdr.evaluate(caseList);
    }
    
    public static RDRTree createRDR(CaseList caseList){
        return new AutoRDR(caseList).getRDRTree();
    }
    
    public AutoRDR(CaseList caseList) {
        Instances data = caseListToInstances(caseList);
        this.wekaClassifier = createClassifier(data);
        this.wekaEvaluation = evaluateWekaClassifier(this.wekaClassifier, data);
        if(this.wekaEvaluation.pctCorrect() != 100){
            System.out.println("WARNING: WEKA Classifier does not accurately classifier all cases. The resulting RDR tree may be incorrect.");
        }
        this.rdrTree = createRDRFromWekaClassifier(this.wekaClassifier);
    }

    public Classifier getWekaClassifier() {
        return wekaClassifier;
    }

    public RDRTree getRDRTree() {
        return rdrTree;
    }

    public Evaluation getWekaEvaluation() {
        return wekaEvaluation;
    }
    
    
    
    

    // Attempts to find and set the class index in the weka data
    private void findAndSetClassIndex(Instances data){
        String[] keywords = {"class", "classification", "Class", "Classification"};
        for(String key : keywords){
            try{
                data.setClass(data.attribute(key));
            } catch (NullPointerException ex){
                continue;
            }
            return;
        }
        data.setClass(data.attribute(data.numAttributes()-1));
        System.out.println("Could not find classification index. Automatically set to last index. (" + data.classAttribute().name() + ")");
        
    }
    
    // Converts an RDRCase to a WEKA instance
    private Instance caseToInstance(Case rdrCase, CaseList cList) {
        Instance instance = new DenseInstance(rdrCase.size());
        return instance;
    }

    
    // Converts a WEKA instance to an RDRCase
    private Case instanceToCase(Instance instance) {
        Case rdrCase = new Case();
        for (int i = 0; i < instance.numAttributes(); i++) {
            String value = instance.toString(i);
            String[] meta = instance.attribute(i).toString().split(" ");
            rdrCase.put(meta[1], value, meta[2]);
        }
        return rdrCase;
    }

    // Turns an RDR CaseList to a WEKA Instances list
    private Instances caseListToInstances(CaseList cList) {
        FastVector fv = createWekaFeatureVector(cList);
        Instances instances = new Instances("autogenInstances", fv, cList.size());

        for (Case c : cList) {
            Instance instance = new DenseInstance(c.size());
            int i = 0;
            for (Map.Entry<String, Object> me : c.entrySet()) {
                switch (me.getValue().getClass().getSimpleName().toLowerCase()) {
                    case "string":
                        instance.setValue((Attribute) fv.elementAt(i), (String) me.getValue());
                        break;
                    case "integer":
                        instance.setValue((Attribute) fv.elementAt(i), (Integer) me.getValue());
                        break;
                    case "double":
                        instance.setValue((Attribute) fv.elementAt(i), (Double) me.getValue());
                        break;
                    case "float":
                        instance.setValue((Attribute) fv.elementAt(i), (Float) me.getValue());
                        break;
                    default:
                        instance.setValue((Attribute) fv.elementAt(i), (String) me.getValue());
                        break;
                }
                i++;
                instances.add(instance);

            }
        }
        findAndSetClassIndex(instances);
        return instances;
    }

    // Creates a WEKA Feature vector from an RDR CaseList
    private FastVector createWekaFeatureVector(CaseList cList) {
        Case templateCase = cList.get(0);
        FastVector fv = new FastVector(templateCase.size());
        templateCase.entrySet().stream().map((me) -> createAttribute(me.getKey(), me.getValue(), cList)).forEach((attr) -> {
            fv.addElement(attr);
        });
        return fv;
    }

    // Creates a WEKA attribute from RDR Case attributes
    private Attribute createAttribute(String key, Object value, CaseList cList) {
        switch (value.getClass().getSimpleName().toLowerCase()) {
            case "integer":
            case "double":
            case "float":
                return new Attribute(key);
            case "string":
                return createNominalAttribute(key, cList);
            default:
                System.err.println("Tried to create '" + key + "' with unknown type '" + value.getClass().getSimpleName().toLowerCase() + "'");
                return new Attribute(key);
        }
    }

    // Creates a nominal WEKA attribute from an RDR CaseList
    private Attribute createNominalAttribute(String key, CaseList cList) {
        ArrayList<String> values = new ArrayList<>();
        cList.stream().filter((c) -> (!values.contains((String) c.get(key)))).forEach((c) -> {
            values.add((String) c.get(key));
        });
        FastVector fv = new FastVector(values.size());
        values.stream().forEach((value) -> {
            fv.addElement(value);
        });
        return new Attribute(key, fv);
    }

    // Loads a WEKA ARFF file from a file
    private Instances loadArffDataFromFile(String filename) throws IOException {
        Instances data;
        try (BufferedReader reader = new BufferedReader(
                new FileReader(filename))) {
            data = new Instances(reader);
            try {
                data.setClass(data.attribute("class"));
            } catch (NullPointerException ex) {
                data.setClass(data.attribute(data.numAttributes() - 1));
            }
        }
        return data;
    }

    // Creates a WEKA classifier with no data
    private Classifier createClassifier() {
        return createClassifier(null);
    }

    // Creates a WEKA classifier (J48) using instances data
    private Classifier createClassifier(Instances data) {
        J48 j = new J48();
        j.setBinarySplits(true);
        j.setMinNumObj(0);
        j.setUnpruned(true);
        j.setConfidenceFactor(1);
        if (data != null) {
            try {
                j.buildClassifier(data);
            } catch (Exception ex) {
                Logger.getLogger(AutoRDR.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return j;
    }
    
    // Evaluates a WEKA classifier, can be used to get classifier accuracy
    private Evaluation evaluateWekaClassifier(Classifier classifier, Instances data){
        try {
            Evaluation eval = new Evaluation(data);
            eval.evaluateModel(classifier, data);
            
            for (int i = 0; i < data.numInstances(); i++) {
                eval.evaluateModelOnce(classifier, data.instance(i));
            }
            return eval;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("There was an error evaluating the weka classifier when automatically creating an RDR tree.");
        }
        return null;
    }

    // Removes unneeded string data from the weka tostring fuction. preprocessing for converting a weka classifier to RDR
    private String wekaSummaryToTreeString(String weka) {
        String treeString = weka;
        treeString = treeString.substring(treeString.indexOf("------------------") + 20, treeString.indexOf("Number of Leaves  :") - 2);
        return treeString;
    }

    
    private RDRTree createRDRFromWekaClassifier(Classifier classifier) {
        String weka = wekaSummaryToTreeString(classifier.toString());
        String[] split = weka.split("\n");
        ArrayList<String> rules = new ArrayList();
        Collections.addAll(rules, split);
        RDRTree dt = createTreeFromRules(rules);
        return dt;
    }

    private RDRTree createTreeFromRules(ArrayList<String> rules) {
        RDRTree dt = new RDRTree();
        populateTree(dt, rules);
        return dt;
    }

    private void populateTree(RDRTree dt, ArrayList<String> rules){
        dt.getRootNode().setTrueNode(addNextRule(rules));
    }
    
    private Node addNextRule(ArrayList<String> rules){
        if(rules.isEmpty()) return null;
        
        String line = rules.remove(0);
        String[] rule = parseRuleString(line);
        boolean terminationNode = rule.length == 2;
        
        Node child = new Node(rule[0], "");
        
        if(terminationNode){
            child.setTrueNode(new Node("true", rule[1]));
        } else {
            child.setTrueNode(addNextRule(rules));
        }
        
        line = rules.remove(0);
        rule = parseRuleString(line);
        terminationNode = rule.length == 2;
        
        if(terminationNode){
            child.setFalseNode(new Node("true", rule[1]));
        } else {
            child.setFalseNode(addNextRule(rules));
        }
        
        return child;
    }

    private String[] parseRuleString(String raw) {
        raw = raw.replaceAll("\\|   ", "");
        String[] rule = raw.split(":");
        if (rule.length == 2) {
            rule[1] = rule[1].substring(1, rule[1].lastIndexOf(" "));
        }
        return rule;
    }

    private int getIndentation(String rule) {
        return rule.length() - rule.replace("|", "").length();
    }

}
