/*
 * Shin && Giga works
 */

package gr.spinellis.ckjm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class CalledClass
{
    private String className;
    private HashMap<String, Integer> myMethods;
    
    public CalledClass(String name)
    {
        className = name;
        myMethods = new HashMap<String, Integer>();
    }
    
    public void addMethod(String str)
    {
        if (!myMethods.containsKey(str))
            myMethods.put(str, 1);
        else
        {
            Integer count = myMethods.get(str);
            count ++;
            
            myMethods.remove(str);
            myMethods.put(str, count);
        }
    }
    
    public String getClassName() { return className; }

    @Override
    public String toString()
    {
        String str = "Class name: "+className+"\n";
        
        str+="Methods: "+myMethods.toString()+"\n";
        
        return str;
    }
}
