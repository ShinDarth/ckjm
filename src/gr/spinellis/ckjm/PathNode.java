/*
 * Shin && Giga works
 * 
 *  PathNode contains packages/classes data
 * 
 */

package gr.spinellis.ckjm;

import java.util.ArrayList;

public class PathNode extends MethodNode {
    
    protected ArrayList<MethodNode> nodeList;
    
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
    
    public void count()
    {
        int c = 0;
        
        for(int i = 0; i < nodeList.size(); i++)
            c += nodeList.get(i).getCount();
        
        setCount(c);
    }
}
