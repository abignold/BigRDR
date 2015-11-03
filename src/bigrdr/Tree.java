/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import javax.script.ScriptException;

/**
 *
 * @author Adam
 */
public class Tree {
    private Node rootNode = null;

    public Tree() {
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
}
