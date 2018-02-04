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
        testDifferenceList();
        testTreeCreation();
    }

    private static void testDifferenceList() {
        System.out.println("Testing difference list creation");
        CaseList caseList = getCaseList();
        Case case1 = caseList.get(0);
        Case case2 = caseList.get(1);
//        case1.getDifferenceList(case2);
        DifferenceList differenceList = case1.getDifferenceList(case2);

        String[][] variables = {
            {"AppleInt", "true"},
            {"BananaDouble", "false"},
            {"CherryFloat", "true"},
            {"DragonfruitBoolean", "false"},
            {"ElderflowerString", "true"},
            {"FruitChar", "false"},
            {"GogurtStringSpace", "false"},
            {"CaseOneUnique", "true"},
            {"CaseTwoUnique", "true"}
        };
        System.out.println("-----DIFF-TEST-----");
        System.out.println("PASS\tRES\tVAR_NAME");
        for (String[] test : variables) {
            boolean result = differenceList.isDifferent(test[0]);
            System.out.println((result == Boolean.valueOf(test[1])) + "\t" + Boolean.toString(result) + "\t" + test[0]);
        }

        System.out.println("-------------------");
        System.out.println("");
    }

    private static CaseList getCaseList() {
        CaseList caseList = new CaseList();

        Case testCase = new Case();
        testCase.put("AppleInt", 1);
        testCase.put("BananaDouble", 1.0d);
        testCase.put("CherryFloat", 2.0f);
        testCase.put("DragonfruitBoolean", true);
        testCase.put("ElderflowerString", "redVSblue");
        testCase.put("FruitChar", 'c');
        testCase.put("GogurtStringSpace", "yellow black");
        testCase.put("CaseOneUnique", "nop_nop_nop_nop");
        caseList.add(testCase);

        testCase = new Case();
        testCase.put("AppleInt", "1");
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

    private static void testTreeCreation() throws ScriptException {
        System.out.println("Testing tree creation");

        // Test Case 1
        Case c1 = new Case();
        c1.put("A", true);
        c1.put("B", true);
        c1.put("C", true);

        // Test Case 2
        Case c2 = new Case();
        c2.put("A", true);
        c2.put("B", false);
        c2.put("C", true);

        // Test Case 3
        Case c3 = new Case();
        c3.put("A", false);
        c3.put("B", false);
        c3.put("C", false);

        System.out.println("-----TREE-TEST-----");
        
        // Step 1
        RDRTree tree = new RDRTree();
        if (!tree.toString().equals("true (Root Node)\n")) {
            System.out.println("Failed. Step 1.");
        } else {
            System.out.println("Passed. Step 1.");
        }

        // Step 2
        Classification classify1 = tree.classify(c1);
        if (!classify1.toString().equals("Root Node")) {
            System.out.println("Failed. Step 2.");
        } else {
            System.out.println("Passed. Step 2.");
        }

        // Step 3
        Node n1 = new Node();
        n1.setCornerstoneCase(c1);
        n1.setClassificationString("1st Rule Added");
        n1.setRule("A == true");
        tree.insertNode(n1);
        if (!tree.toString().equals("true (Root Node)\n|   A == true (1st Rule Added)\n|   NONE\n")) {
            System.out.println("Failed. Step 3.");
        }  else {
            System.out.println("Passed. Step 3.");
        }
        
        // Step 4
        Classification classify2 = tree.classify(c1);
        if (!classify2.toString().equals("1st Rule Added")) {
            System.out.println("Failed. Step 4.");
        } else {
            System.out.println("Passed. Step 4.");
        }

        // Step 5
        Classification classify3 = tree.classify(c2);
        if (!classify3.toString().equals("1st Rule Added")) {
            System.out.println("Failed. Step 5.");
        } else {
            System.out.println("Passed. Step 5.");
        }
        
        // Step 6
        Node n2 = new Node();
        n2.setCornerstoneCase(c2);
        n2.setClassificationString("2nd Rule Added");
        n2.setRule("B == false");
        tree.insertNode(n2);
        if (!tree.toString().equals("true (Root Node)\n|   A == true (1st Rule Added)\n|   |   B == false (2nd Rule Added)\n|   |   NONE\n|   NONE\n")) {
            System.out.println("Failed. Step 6.");
        } else {
            System.out.println("Passed. Step 6.");
        }
        
        // Step 7
        Classification classify4 = tree.classify(c2);
        if (!classify4.toString().equals("2nd Rule Added")) {
            System.out.println("Failed. Step 7.");
        }  else {
            System.out.println("Passed. Step 7.");
        }

        // Step 8
        Classification classify5 = tree.classify(c3);
        if (!classify5.toString().equals("Root Node")) {
            System.out.println("Failed. Step 8.");
        }  else {
            System.out.println("Passed. Step 8.");
        }

        // Step 9
        Node n3 = new Node();
        n3.setCornerstoneCase(c3);
        n3.setClassificationString("3rd Rule Added");
        n3.setRule("A != true");
        tree.insertNode(n3);
        if (!tree.toString().equals("true (Root Node)\n|   A == true (1st Rule Added)\n|   |   B == false (2nd Rule Added)\n|   |   A != true (3rd Rule Added)\n|   NONE\n")) {
            System.out.println("Failed. Step 9.");
        } else {
            System.out.println("Passed. Step 9.");
        }
        
        // Step 10
        Classification classify6 = tree.classify(c3);
        if (!classify6.toString().equals("3rd Rule Added")) {
            System.out.println("Failed. Step 10.");
        } else {
            System.out.println("Passed. Step 10.");
        }
        
        // Step 10
        Classification classify7 = tree.classify(c1);
        if (!classify7.toString().equals("1st Rule Added")) {
            System.out.println("Failed. Step 11.");
        } else {
            System.out.println("Passed. Step 11.");
        }
        
        // Step 10
        Classification classify8 = tree.classify(c2);
        if (!classify8.toString().equals("2nd Rule Added")) {
            System.out.println("Failed. Step 12.");
        } else {
            System.out.println("Passed. Step 12.");
        }
        
        System.out.println("-------------------");
        System.out.println("");

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
        for (String[] test : rules) {
            Node n = new Node(test[0], "Test Node");
            boolean result = n.testRule(testCase);
            System.out.println((result == Boolean.valueOf(test[1])) + "\t" + Boolean.toString(result) + "\t" + test[0]);
        }
        System.out.println("-------------------");
        System.out.println("");
    }

    private static void testRuleFormatterIsVariableMethod() {
        System.out.println("Testing classifier of valid variable names.");
        String[][] variables = {
            {"abc", "true"}, {"$abc", "true"},
            {"_abc", "true"}, {"abc0", "true"},
            {"$abc$", "true"}, {"_abc_", "true"},
            {" abc", "false"}, {".abc", "false"},
            {"-abc", "false"}, {"=abc", "false"},
            {"012", "false"}, {"if", "false"},
            {"case", "false"}, {"", "false"},};
        System.out.println("-----VAR--TEST-----");
        System.out.println("PASS\tRES\tVAR_NAME");
        for (String[] test : variables) {
            boolean result = RuleFormatter.isVariable(test[0]);
            System.out.println((result == Boolean.valueOf(test[1])) + "\t" + Boolean.toString(result) + "\t" + test[0]);
        }
        System.out.println("-------------------");
        System.out.println("");
    }

    private static Case createTestCase() {
        System.out.println("Testing case creation");
        Case testCase = new Case();
        testCase.put("AppleInt", 0);
        testCase.put("BananaDouble", 1.0d);
        testCase.put("CherryFloat", 2.0f);
        testCase.put("DragonfruitBoolean", true);
        testCase.put("ElderflowerString", "redVSblue");
        testCase.put("FruitChar", 'c');
        testCase.put("GogurtStringSpace", "yellow black");
        System.out.println("-----TEST-CASE-----");
        testCase.print();
        System.out.println("-------------------");
        System.out.println();
        return testCase;
    }
}
