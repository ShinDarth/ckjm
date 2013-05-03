/*
 * Shin && Giga works
 * 
 * MethodNode contains methods data
 * 
 */

package gr.spinellis.ckjm;

public class MethodNode {
    
    protected String name;
    protected int count;
    
    public MethodNode(String name) {
        this.name = name;
        count = 0;
    }
    
    public String getName()             { return name; }
    public int getCount()               { return count; }
    public void setCount(int count)     { this.count = count; }
    public void incrCount(int count)    { this.count += count; }
}
