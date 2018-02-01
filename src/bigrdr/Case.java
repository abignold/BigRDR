/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Adam
 */
public class Case extends HashMap<String, Object>{
    
    public void print(){
        this.entrySet().stream().forEach((me) -> {
            System.out.println(me.getKey() + "\t" + RuleFormatter.formatVariable(me.getValue()) + "\t" + me.getValue().getClass().getSimpleName());
        });
    }
    
    public Object put(String key, String valueString, String type) {
        Object value = changeType(valueString, type);
        return super.put(key, value);
    }
    
    public Object[][] getDifferenceList(Case otherCase){
        String[] sharedKeys = getSharedKeys(otherCase);
        String[] uniqueKeys = getUniqueKeys(otherCase);

        Object[][] differenceList = new Object[sharedKeys.length + uniqueKeys.length][4];
        // fill array with empty strings to avoid null pointer exceptions later
        for(Object[] objectArray : differenceList){
            Arrays.fill(objectArray, "");
        }
        
        int index = 0;
        for(String key : sharedKeys){
            differenceList[index][0] = key;
            differenceList[index][1] = this.get(key);
            differenceList[index][2] = otherCase.get(key);
            differenceList[index][3] = this.get(key) != otherCase.get(key);
            index++;
        }
        
        for(String key : uniqueKeys){
            differenceList[index][0] = key;
            differenceList[index][1] = this.get(key);
            differenceList[index][2] = otherCase.get(key);
            differenceList[index][3] = "difference";
            index++;
        }
        
        for(Object[] objectArray : differenceList){
            System.out.println(Arrays.toString(objectArray));
        }
        
        return null;
    }
    
    public String[] getSharedKeys(Case otherCase){
        ArrayList<String> sharedKeys = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>(this.keySet());
        
        for(Map.Entry<String, Object> me : otherCase.entrySet()){
            String key = me.getKey();
            if(keys.contains(key.toString())){
                sharedKeys.add(key);
            }
        }
        
        return sharedKeys.toArray(new String[sharedKeys.size()]);
    }
    
    public String[] getUniqueKeys(Case otherCase){
        ArrayList<String> notSharedKeys = new ArrayList<>();
        
        for(Map.Entry<String, Object> me : otherCase.entrySet()){
            if(!this.keySet().contains(me.getKey().toString())){
                notSharedKeys.add(me.getKey());
            }
        }
        
        for(Map.Entry<String, Object> me : this.entrySet()){
            if(!otherCase.keySet().contains(me.getKey())){
                notSharedKeys.add(me.getKey());
            }
        }
        
        return notSharedKeys.toArray(new String[notSharedKeys.size()]);
    }
    
    public boolean isDifference(String key, Case otherCase){
        if(!this.containsKey(key) && !otherCase.containsKey(key)){
            return true;
        }
        
        return false;
    }
    
    private static Object changeType(String value, String type){
        switch(type.toLowerCase()){
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
