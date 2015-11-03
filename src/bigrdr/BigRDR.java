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
public class BigRDR {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ScriptException {
        Tree tree = new Tree();
        Case testCase = new Case();
        Classification result = tree.classify(testCase);
        System.out.println("Result: " + result.getInsertionClassification());
        System.out.println("Classification: " + result.getClassificationNode().getClassificationString());
        
        testCase.put("Apple", 0);
        testCase.put("Banana", 1.0d);
        testCase.put("Cherry", 2.0f);
        testCase.put("Dragonfruit", true);
        testCase.put("Elderflower", "redVSblue");
        testCase.put("Fruit", 'c');
        
        testCase.print();
        
        RuleFormatter.isVariable("abc");
        RuleFormatter.isVariable("$abc");
        RuleFormatter.isVariable("_abc");
        RuleFormatter.isVariable("abc0");
        RuleFormatter.isVariable("$abc$");
        RuleFormatter.isVariable("_abc_");
        RuleFormatter.isVariable(" abc");
        RuleFormatter.isVariable(".abc");
        RuleFormatter.isVariable("-abc");
        RuleFormatter.isVariable("=abc");
        RuleFormatter.isVariable("012");
        RuleFormatter.isVariable("if");
        RuleFormatter.isVariable("case");
        
        System.out.println();
        RuleFormatter.createSubstitutedRule("Apple > 0.1", testCase);
    }
    
}
