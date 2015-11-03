/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Adam
 */
public class Case extends HashMap<String, Object>{
    
    public void print(){
        for(Map.Entry<String, Object> me : this.entrySet()){
            System.out.println(me.getKey() + "\t" + RuleFormatter.formatVariable(me.getValue()) + "\t" + me.getValue().getClass().getSimpleName());
        }
    }
}
