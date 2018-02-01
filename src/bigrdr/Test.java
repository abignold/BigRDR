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
        testDifferenceList();
        
    }

    
    private static void testDifferenceList(){
        System.out.println("Testing difference list creation");
        CaseList caseList = getCaseList();
        Case case1 = caseList.get(0);
        Case case2 = caseList.get(1);
        System.out.println("-----DIFF-TEST-----");
//        case1.getDifferenceList(case2);
        System.out.println(case1.isDifference("AppleInt", case2));

        System.out.println("-------------------");
        System.out.println("");
    }
    
    private static CaseList getCaseList(){
        CaseList caseList = new CaseList();
        
        Case testCase = new Case();
        testCase.put("AppleInt", 0);
        testCase.put("BananaDouble", 1.0d);
        testCase.put("CherryFloat", 2.0f);
        testCase.put("DragonfruitBoolean", true);
        testCase.put("ElderflowerString", "redVSblue");
        testCase.put("FruitChar", 'c');
        testCase.put("GogurtStringSpace", "yellow black");
        testCase.put("CaseOneUnique", "nop_nop_nop_nop");
        caseList.add(testCase);
        
        testCase = new Case();
        testCase.put("AppleInt", 1);
        testCase.put("BananaDouble", 1.0d);
        testCase.put("CherryFloat", 3.0f);
        testCase.put("DragonfruitBoolean", true);
        testCase.put("ElderflowerString", "blueVSred");
        testCase.put("FruitChar", 'c');
        testCase.put("GogurtStringSpace", "yellow black");
        testCase.put("CaseTwoUnique", "yep_yep_yep_yep");
        caseList.add(testCase);
        
        return caseList;
    }
    
    
    
    
    
    
    
    
    
    private static void testTreeCreation(Case testCase) throws ScriptException {
        
    }

    private static void testRuleEvaluator(Case testCase) throws ScriptException {
        System.out.println("Testing rule evaluator");
        String[][] rules = {
            {"1 + 1 == 2", "true"},
            {"5 == 2", "false"},
            {"AppleInt > 0.1 || GogurtStringSpace == \"yellow black\"", "true"}
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
        testCase.put("AppleInt", 0);
        testCase.put("BananaDouble", 1.0d);
        testCase.put("CherryFloat", 2.0f);
        testCase.put("DragonfruitBoolean", true);
        testCase.put("ElderflowerString", "redVSblue");
        testCase.put("FruitChar", 'c');
        testCase.put("GogurtStringSpace", "yellow black");
        System.out.println("Done.");
        System.out.println("-----TEST-CASE-----");
        testCase.print();
        System.out.println("-------------------");
        System.out.println();
        return testCase;
    }
}
