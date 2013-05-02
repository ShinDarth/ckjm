/*
 * Shin && Giga works
 * 
 * MethodNode contains methods data
 * 
 */

package gr.spinellis.ckjm;

public class MethodNode {
    
    public String name;
    public int count;
    
    public MethodNode(String name) {
        this.name = name;
        count = 0;
    }
    
    public String getName()             { return name; }
    public void setName(String name)    { this.name = name; }
    public int getCount()               { return count; }
    public void setCount(int count)     { this.count = count; }
}
