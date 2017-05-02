/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import autordr.AutoRDR;
import bigrdr.Case;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author abign
 */
public class CaseList extends ArrayList<Case> {

    public static void main(String[] args) {
        String[][] matrix = {
            {"sepallength", "sepalwidth", "petallength", "petalwidth", "classification"},
            {"real", "real", "real", "real", "string"},
            {"5.1", "3.8", "1.9", "0.4", "Iris-setosa"},
            {"7.0", "3.2", "4.7", "1.4", "Iris-versicolor"},
            {"5.9", "3.0", "5.1", "1.8", "Iris-virginica"}
        };
        CaseList c = CaseList.createFromFile("iris.csv");
        int x = 0;
        x ++;
    }

    public static CaseList createFromFile(String filename) {
        return processFile(filename);
    }

    public static CaseList createFromArray(String[][] matrix) {
        CaseList cList = new CaseList();
        LinkedHashMap<String, String> typeDefinitions = processHeader(matrix[0], matrix[1]);
        for (int i = 2; i < matrix.length; i++) {
            cList.add(createCase(matrix[i], typeDefinitions));
        }
        return cList;
    }

    private static CaseList processFile(String filename) {
        CaseList cList = new CaseList();
        LinkedHashMap<String, String> typeDefinitions = new LinkedHashMap<>();
        File file = new File(filename);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int lineNum = 0;
            String delim = "NOTSET";
            while ((line = br.readLine()) != null) {
                lineNum++;
                if (delim.equals("NOTSET")) {
                    delim = findDelim(line);
                }
                String[] csvLine = csvLineToString(line, delim);
                if (csvLine.length == 0) {
                    continue;
                }

                if (lineNum == 1) {
                    String[] nameLine = csvLine;
                    String[] typeLine = csvLineToString(br.readLine(), delim);
                    typeDefinitions = processHeader(nameLine, typeLine);
                } else {
                    cList.add(createCase(csvLine, typeDefinitions));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Error reading file.");
            System.exit(-1);
        }
        return cList;
    }

    private static String[] csvLineToString(String line, String delim) {

        if (line.isEmpty()) {
            return new String[0];
        }
        String[] csvLine = line.replaceAll(" ", "").split(delim);
        return csvLine;
    }

    private static LinkedHashMap<String, String> processHeader(String[] nameLine, String[] typeLine) {
        LinkedHashMap<String, String> typeDefinitions = new LinkedHashMap<>();
        if (nameLine.length != typeLine.length) {
            System.err.println("Number of elements in name and type lines do not match.");
        } else {
            for (int i = 0; i < nameLine.length; i++) {
                typeDefinitions.put(nameLine[i], typeLine[i]);
            }
        }
        return typeDefinitions;
    }

    private static String findDelim(String line) {
        int comma = line.length() - line.replace(",", "").length();
        int tab = line.length() - line.replace("\t", "").length();
        int semi = line.length() - line.replace(";", "").length();
        if (comma >= tab && comma >= semi) {
            return ",";
        }
        if (semi >= tab && semi >= comma) {
            return ";";
        }
        if (tab >= comma && tab >= semi) {
            return "\t";
        }
        return ",";
    }

    private static Case createCase(String[] csvLine, LinkedHashMap<String, String> typeDefinitions) {
        Case rdrCase = new Case();
        String[] keys = typeDefinitions.keySet().toArray(new String[0]);
        String[] values = typeDefinitions.values().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            rdrCase.put(keys[i], csvLine[i], values[i]);
        }
        return rdrCase;
    }

}
