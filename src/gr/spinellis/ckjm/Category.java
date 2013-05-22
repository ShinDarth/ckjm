/*
 * Shin && Giga works
 * 
 */

package gr.spinellis.ckjm;

import java.util.ArrayList;

public class Category
{
    String name;        // name of this category
    ArrayList classes;  // members of this category        
    
    public Category(String name)
    {
        this.name = name;
        classes = new ArrayList();
    }
    
    // check if that calledClass belongs to this category
    public boolean check(String calledClass)
    {
        for (int i = 0; i < classes.size(); i++)
            if (calledClass.startsWith(((String)classes.get(i))))
                return true;
        
        return false;
    }
    
    // fill category with its classes
    public void addClassToCategory(String path) { classes.add(path); }
}