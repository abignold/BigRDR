/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Adam
 */
public class Case extends HashMap<String, Object> {

    public void print() {
        this.entrySet().stream().forEach((me) -> {
            System.out.println(me.getKey() + "\t" + RuleFormatter.formatVariable(me.getValue()) + "\t" + me.getValue().getClass().getSimpleName());
        });
    }

    public Object put(String key, String valueString, String type) {
        Object value = changeType(valueString, type);
        return super.put(key, value);
    }

    // returns difference list, {key, case1, case2, isDifferent}
    public DifferenceList getDifferenceList(Case otherCase) {
        return new DifferenceList(this, otherCase);
    }

    

    private static Object changeType(String value, String type) {
        switch (type.toLowerCase()) {
            case "int":
            case "integer":
                return Integer.valueOf(value);
            case "double":
            case "real":
                return Double.valueOf(value);
            case "float":
                return Float.valueOf(value);
            case "string":
            case "text":
                return value;
            case "hidden":
                return null;
            default:
                return value;
        }
    }

}
