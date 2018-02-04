/*
 * To change this.getCaseOne() license header, choose License Headers in Project Properties.
 * To change this.getCaseOne() template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author abign
 */
public class DifferenceList{

    private HashMap<String, Object[]> differenceListStore = new HashMap<>();
    private Case case1;
    private Case case2;
    private int hash1;
    private int hash2;
    
    
    public DifferenceList(Case case1, Case case2){
        this.case1 = case1;
        this.case2 = case2;
        this.update();
    }
    
    private void update(){
        this.hash1 = case1.hashCode();
        this.hash2 = case2.hashCode();
        
        String[] sharedKeys = getSharedKeys();
        String[] uniqueKeys = getUniqueKeys();
        String[] allKeys = Arrays.copyOf(sharedKeys, sharedKeys.length + uniqueKeys.length);
        System.arraycopy(uniqueKeys, 0, allKeys, sharedKeys.length, uniqueKeys.length);
        
        for (String key : allKeys) {
            Object[] entry = new Object[4];
            entry[0] = key;
            entry[1] = this.getCaseOne().get(key);
            entry[2] = this.getCaseTwo().get(key);
            entry[3] = this.isDifferent(key);
            this.differenceListStore.put(key, entry);
        }
    }
    
    public Object[][] getListAsObjectArray(){
        this.checkForChanges();
        return this.differenceListStore.values().toArray(new Object[this.differenceListStore.size()][4]);
    }
    
    public Object[] getKeyAsObjectArray(String key){
        this.checkForChanges();
        return this.differenceListStore.get(key);
    }
    
    public String[] getSharedKeys() {
        
        ArrayList<String> sharedKeys = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>(this.getCaseOne().keySet());

        for (Map.Entry<String, Object> me : this.getCaseTwo().entrySet()) {
            String key = me.getKey();
            if (keys.contains(key.toString())) {
                sharedKeys.add(key);
            }
        }

        return sharedKeys.toArray(new String[sharedKeys.size()]);
    }

    public String[] getUniqueKeys() {
        ArrayList<String> notSharedKeys = new ArrayList<>();

        for (Map.Entry<String, Object> me : this.getCaseTwo().entrySet()) {
            if (!this.getCaseOne().keySet().contains(me.getKey().toString())) {
                notSharedKeys.add(me.getKey());
            }
        }

        for (Map.Entry<String, Object> me : this.getCaseOne().entrySet()) {
            if (!this.getCaseTwo().keySet().contains(me.getKey())) {
                notSharedKeys.add(me.getKey());
            }
        }

        return notSharedKeys.toArray(new String[notSharedKeys.size()]);
    }
    
    // false if both don't have keys.
    // true if only one has key
    // true if both have keys but values dont match
    public boolean isDifferent(String key) {
        if (!this.getCaseOne().containsKey(key) && !this.getCaseTwo().containsKey(key)) {
            return false;
        }
        
        if(!this.getCaseOne().containsKey(key) || !this.getCaseTwo().containsKey(key)){
            return true;
        }

        return (!this.getCaseOne().get(key).equals(this.getCaseTwo().get(key)));
    }

    public Case getCaseOne() {
        return case1;
    }

    public Case getCaseTwo() {
        return case2;
    }
    
    public void checkForChanges(){
        if(this.getCaseTwo().hashCode() != this.hash1 || this.getCaseTwo().hashCode() != this.hash2){
            update();
        } 
    }
}
