/*
 * Shin && Giga works
 */

package gr.spinellis.ckjm;

public class SimpleNode {
    
    public String name;
    public int count;
    
    public SimpleNode(String name) {
        this.name = name;
        count = 0;
    }
    public String getName() { return name;}
    public void setName(String name) { this.name = name; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
