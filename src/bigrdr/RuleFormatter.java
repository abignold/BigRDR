/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;

/**
 *
 * @author Adam
 */
public class RuleFormatter {

    static final Collator englishCollator = Collator.getInstance(Locale.ENGLISH);
    static final String reservedWords[] = {
        "abstract", "assert", "boolean", "break", "byte", "case",
        "catch", "char", "class", "const", "continue",
        "default", "do", "double", "else", "extends",
        "false", "final", "finally", "float", "for",
        "goto", "if", "implements", "import", "instanceof",
        "int", "interface", "long", "native", "new",
        "null", "package", "private", "protected", "public",
        "return", "short", "static", "strictfp", "super",
        "switch", "synchronized", "this", "throw", "throws",
        "transient", "true", "try", "void", "volatile",
        "while"
    };

    public static String createSubstitutedRule(String rule, Case testCase) {
        System.out.println(rule);
        String[] ruleSplit = rule.split(" ");
        for(String var : ruleSplit){
            if(isVariable(var)){
                var = formatVariable(testCase.get(var));
            }
        }
        String substitutedRule = String.join(" ", ruleSplit);
        System.out.println(substitutedRule);
        System.out.println("--------------");
        return substitutedRule;
    }

    public static String formatVariable(Object variable) {
        switch (variable.getClass().getSimpleName()) {
            case "String":
                return "\"" + variable.toString() + "\"";
            case "Character":
                return "'" + variable.toString() + "'";
            default:
                return variable.toString();
        }
    }

    public static boolean isVariable(String variableName) {
        System.out.print(variableName + ": ");
        // Check first character, should be a-z,A-Z, $, or _
        if (!variableName.substring(0, 1).matches("^[A-Za-z$_]*$")) {
            System.out.println("False");
            return false;
        }
        
        int $$a = 7;
        
        if(!variableName.substring(1).matches("^[A-Za-z0-9$_]*$")) {
            System.out.println("False");
            return false;
        }
        
        if(isJavaKeyword(variableName)){
            System.out.println("False");
            return false;
        }
        
        System.out.println("True");
        return true;
    }
    
    public static boolean isJavaKeyword(String keyword) {
        return (Arrays.binarySearch(reservedWords, keyword, englishCollator) >= 0);
    }
}
