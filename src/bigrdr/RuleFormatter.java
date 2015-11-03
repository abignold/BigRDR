/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Collator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        // Convert all string ("*") to hashes, this is done to avoid splitting strings withs spaces. 
        HashMap<String, String> strLookup = new HashMap<>();
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(rule);
        while (m.find()) {
            strLookup.put(getMD5(m.group(1)), m.group(1));
            rule = rule.replaceAll(m.group(1), getMD5(m.group(1)));
        }
        
        // Find all variables and replace them with the values.
        String[] ruleSplit = rule.split(" ");
        rule = " " + rule + " ";
        for(String var : ruleSplit){
            if(isVariable(var)){
                String val = formatVariable(testCase.get(var));
                rule = rule.replaceFirst(" " + var + " ", " " + val + " ");
            }
        }
        rule = rule.substring(1).substring(0, rule.length()-2);

        // Replace the hashes with their strings
        for(Map.Entry<String, String> me : strLookup.entrySet()){
            rule = rule.replaceAll(me.getKey(), me.getValue());
        }
        
        return rule;
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
        if(variableName.length() == 0){
            return false;
        }

        // Check first character, should be a-z,A-Z, $, or _
        if (!variableName.substring(0, 1).matches("^[A-Za-z$_]*$")) {
            return false;
        }

        if(!variableName.substring(1).matches("^[A-Za-z0-9$_]*$")) {
            return false;
        }
        
        if(isJavaKeyword(variableName)){
            return false;
        }

        return true;
    }
    
    public static boolean isJavaKeyword(String keyword) {
        return (Arrays.binarySearch(reservedWords, keyword, englishCollator) >= 0);
    }
    
    public static boolean quickSyntaxCheck(String rule){
        if(rule.contains("\n")){
            return false;
        }
        
        if(countCharacter(rule, "\"") % 2 == 1){
            return false;
        }
        
        return true;
    }
    
    public static int countCharacter(String rule, String str){
        int count = 0;
        int index = rule.indexOf(str);
        while(index != -1){
            count++;
            index = rule.indexOf(str);
        }
        return count;
    }
    
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
