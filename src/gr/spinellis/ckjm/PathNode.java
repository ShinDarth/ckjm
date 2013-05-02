/*
 * Shin && Giga works
 * 
 *  PathNode contains packages/classes data
 * 
 */

package gr.spinellis.ckjm;

import java.util.ArrayList;
import java.util.Iterator;

public class PathNode extends MethodNode {
    
    ArrayList<MethodNode> nodeList;
    
    public PathNode(String name) {
        super(name);
        nodeList = new ArrayList();
    }
    
    public ArrayList getArrayList() { return nodeList; }
    
    public void addNode(MethodNode node) {
        nodeList.add(node);
    }
    
    public MethodNode getNode(String n) {
        for(int i = 0; i < nodeList.size(); i++) {
            if(nodeList.get(i) != null && nodeList.get(i).getName().equals(n))
                return nodeList.get(i);
        }
        return null;
    }
}
