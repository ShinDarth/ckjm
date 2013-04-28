/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.spinellis.ckjm;

/**
 *
 * @author Gabriele Gelardi
 */
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
