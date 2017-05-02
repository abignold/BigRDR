/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import java.util.HashMap;
import java.util.Map;
import javax.script.ScriptException;

/**
 *
 * @author Adam
 */
public class RDRTree {
    private Node rootNode = null;

    public RDRTree() {
        this.rootNode = new Node("true", "Root Node");
    }
    
    public Classification classify(Case testCase) throws ScriptException{
        Node childNode = this.rootNode;
        Node lastTrueNode = this.rootNode;
        Node parentNode = this.rootNode;
        boolean ruleResult = false;
        
        while(childNode != null){
            parentNode = childNode;
            ruleResult = parentNode.testRule(testCase);
            if(ruleResult){
                lastTrueNode = parentNode;
                childNode = parentNode.getTrueNode();
            } else {
                childNode = parentNode.getFalseNode();
            }
        }
        
        return new Classification(lastTrueNode, parentNode, ruleResult);
    }
    
    public Classification insertNode(Node node) throws ScriptException{
        Classification result = this.classify(node.getCornerstoneCase());
        this.insertNode(node, result);
        return result;
    }
    
    public Classification insertNode(Node node, Classification result){
        if(result.getInsertionClassification()){
            result.getInsertionNode().setTrueNode(node);
        } else {
            result.getInsertionNode().setFalseNode(node);
        }
        return result;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }
    
    @Override
    public String toString() {
        return this.rootNode.recusiveNodeString(0);
    }
    
    public HashMap<String, Double> evaluate(CaseList caseList) throws ScriptException {
        return evaluate(caseList, 1);
    }
    
    // Print settings, 0 = No printing, 1 = Stats, 2 = stats + incorrect, 3 = stats + incorrect + correct
    public HashMap<String, Double> evaluate(CaseList caseList, int printSettings) throws ScriptException {
        long startTime = System.currentTimeMillis();
        double correct = 0;
        for (Case c : caseList) {
            Classification cl = this.classify(c);
            if (c.get("classification").equals(cl.getClassificationNode().getClassificationString())) {
                correct++;
                if(printSettings >= 3) System.out.println("  CORRECT: " + c.get("classification") + " - " + cl.getClassificationNode().getClassificationString());
            } else {
                if(printSettings >= 2) System.out.println("INCORRECT: " + c.get("classification") + " - " + cl.getClassificationNode().getClassificationString());
            }
        }
        long endTime = System.currentTimeMillis();;
        
        HashMap<String, Double> stats = new HashMap<>();
        stats.put("size", caseList.size()*1d);
        stats.put("correct", correct);
        stats.put("incorrect", caseList.size()-correct);
        stats.put("accuracy", (correct / caseList.size() * 100.0));
        stats.put("treetime", (endTime - startTime)*1d);
        stats.put("casetime", stats.get("treetime")/stats.get("size"));
        
        
        if(printSettings >= 1){
            for(Map.Entry<String, Double> me : stats.entrySet()){
                System.out.println(me.getKey().toUpperCase() + ": " + me.getValue());
            }
        }
        
        return stats;
    }
}
