/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *
 * @author Adam
 */
public class Node {

    private Node trueNode = null;
    private Node falseNode = null;
    private String rule = null;
    private String classificationString = null;
    private Case cornerstoneCase = null;

    public Node() {
        this.rule = "true";
        this.classificationString = "NO CLASS SET";
    }

    public Node(String rule, String classificationString) {
        this.rule = rule;
        this.classificationString = classificationString;
    }

    public boolean testRule(Case testCase) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        String substitutedRule = RuleFormatter.createSubstitutedRule(rule, testCase);
        Object result = engine.eval(substitutedRule);
        return (Boolean) result;
    }

    public Node getTrueNode() {
        return trueNode;
    }

    public void setTrueNode(Node trueNode) {
        this.trueNode = trueNode;
    }

    public Node getFalseNode() {
        return falseNode;
    }

    public void setFalseNode(Node falseNode) {
        this.falseNode = falseNode;
    }

    public void addNode(Node n, boolean sideToAddOn) {
        if (sideToAddOn) {
            this.setTrueNode(n);
        } else {
            this.setFalseNode(n);
        }
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getClassificationString() {
        return classificationString;
    }

    public void setClassificationString(String classificationString) {
        this.classificationString = classificationString;
    }

    public Case getCornerstoneCase() {
        return cornerstoneCase;
    }

    public void setCornerstoneCase(Case cornerstoneCase) {
        this.cornerstoneCase = cornerstoneCase;
    }

    public String recusiveNodeString(int indentation) {
        String s = "";
        for (int i = 0; i < indentation; i++) {
            s += "|   ";
        }
        s += this.rule + " (" + this.classificationString + ")\n";
        
        if (this.trueNode == null && this.falseNode == null) return s;
        if (this.trueNode != null) {
            s += this.trueNode.recusiveNodeString(indentation + 1);
        } else {
            for (int i = 0; i < indentation+1; i++) {
                s += "|   ";
            }
            s += "NONE\n";
        }
        if (this.falseNode != null) {
            s += this.falseNode.recusiveNodeString(indentation + 1);
        } else {
            for (int i = 0; i < indentation+1; i++) {
                s += "|   ";
            }
            s += "NONE\n";
        }
        return s;
    }

}
