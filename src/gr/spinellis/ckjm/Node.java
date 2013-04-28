/*
 * Shin && Giga works
 */

package gr.spinellis.ckjm;

import java.util.ArrayList;
import java.util.Iterator;

public class Node extends SimpleNode {
    
    ArrayList<SimpleNode> nodeList;
    
    public Node(String name) {
        super(name);
        nodeList = new ArrayList();
    }
    public ArrayList getArrayList() { return nodeList; }
    
    public void addNode(SimpleNode node) {
        nodeList.add(node);
    }
    public SimpleNode getNode(String n) {
        for(int i = 0; i < nodeList.size(); i++) {
            if(nodeList.get(i) != null && nodeList.get(i).getName().equals(n))
                return nodeList.get(i);
        }
        return null;
    }
}
