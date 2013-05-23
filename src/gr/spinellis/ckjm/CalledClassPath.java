/*
 * Shin && Giga works
 * 
 * CalledClassPath contains datas in the format package+class
 * 
 */

package gr.spinellis.ckjm;

import java.util.HashMap;
import java.util.Iterator;

public class CalledClassPath
{
    private String className;
    private HashMap<String, Integer> myMethods;
    
    public CalledClassPath(String name)
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
    
    public HashMap<String, Integer> getMethods()    { return myMethods; }
    public String getClassName()                    { return className; }
    
    public int getCalledMethodsCount()
    {
        int count = 0;
        Iterator itr = myMethods.values().iterator();
        
        while (itr.hasNext())
            count += (int)itr.next();
        
        return count;
    }

    @Override
    public String toString()
    {
        String str = "Class name: "+className+"\t--> called methods amount: "+getCalledMethodsCount()+"\n";
        
        str+="Methods: "+myMethods.toString()+"\n";
        
        return str;
    }
}
