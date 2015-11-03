/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import javax.script.ScriptException;

/**
 *
 * @author Abignold
 */
public class Test {
    public static void main(String[] args) throws ScriptException {
        Case testCase = createTestCase();
        testRuleFormatterIsVariableMethod();
        testRuleEvaluator(testCase);
        testTreeCreation(testCase);
    }

    private static void testTreeCreation(Case testCase) throws ScriptException {
        System.out.println("Testing tree creation");
        Tree tree = new Tree();
        System.out.println("-----TREE-TEST-----");
        Classification result = tree.classify(testCase);
        System.out.println("Classification: " + result.getClassificationNode().getClassificationString());
        System.out.println("Result: " + result.getInsertionClassification());
        System.out.println("-------------------");
        System.out.println("");
    }

    private static void testRuleEvaluator(Case testCase) throws ScriptException {
        System.out.println("Testing rule evaluator");
        String[][] rules = {
            {"1 + 1 == 2", "true"},
            {"5 == 2", "false"},
            {"Apple > 0.1 || Gogurt == \"yellow black\"", "true"}
        };
        
        System.out.println("-----RULE-TEST-----");
        System.out.println("PASS\tRES\tRULE");
        for(String[] test : rules){
            Node n = new Node(test[0], "Test Node");
            boolean result = n.testRule(testCase);
            System.out.println((result==Boolean.valueOf(test[1])) + "\t" + Boolean.toString(result) + "\t" + test[0]);
        }
        System.out.println("-------------------");
        System.out.println("");
    }

    private static void testRuleFormatterIsVariableMethod() {
        System.out.println("Testing classifier of valid variable names.");
        String[][] variables = {
            {"abc","true"},{"$abc","true"},
            {"_abc","true"},{"abc0","true"},
            {"$abc$","true"},{"_abc_","true"},
            {" abc","false"},{".abc","false"},
            {"-abc","false"},{"=abc","false"},
            {"012","false"},{"if","false"},
            {"case","false"},{"","false"},
        };
        System.out.println("-----VAR--TEST-----");
        System.out.println("PASS\tRES\tVAR_NAME");
        for(String[] test : variables){
            boolean result = RuleFormatter.isVariable(test[0]);
            System.out.println((result==Boolean.valueOf(test[1])) + "\t" + Boolean.toString(result) + "\t" +  test[0]);
        }
        System.out.println("-------------------");
        System.out.println("");
    }

    private static Case createTestCase() {
        System.out.print("Creating new case... ");
        Case testCase = new Case();
        testCase.put("Apple", 0);
        testCase.put("Banana", 1.0d);
        testCase.put("Cherry", 2.0f);
        testCase.put("Dragonfruit", true);
        testCase.put("Elderflower", "redVSblue");
        testCase.put("Fruit", 'c');
        testCase.put("Gogurt", "yellow black");
        System.out.println("Done.");
        System.out.println("-----TEST-CASE-----");
        testCase.print();
        System.out.println("-------------------");
        System.out.println();
        return testCase;
    }
}
