/*
 * Shin && Giga works
 * 
 * DataHandler handles all called packages (stored in PathNode objects) which
 * contains all called classes (also stored in other PathNode objects).
 * Each PathNode which represents a called class will contain an array of
 * called methods (each one stored in MethodNode objects).
 * 
 */

package gr.spinellis.ckjm;

import java.util.ArrayList;

public class DataHandler {

    ArrayList<PathNode> data;

    public DataHandler() {
        data = new ArrayList();
    }

    public void addPackage(String p) {
        PathNode pack = new PathNode(p);
        data.add(pack);
    }

    public PathNode getPackage(String p) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(p)) 
                return data.get(i);    
        }
        return null;
    }
    
    public PathNode getClassOfPackage(String p, String c) {
        PathNode pack = getPackage(p);
        return (PathNode)pack.getNode(c);
    }

    public boolean addClassToPackage(String p, String c) {
        PathNode pack = getPackage(p);
        PathNode cl = new PathNode(c);
        
        if(pack != null) {
            pack.addNode(cl);
            return true;
        }
        return false;
    }

    public boolean addMethodToClass(String p, String c, String m) {
       PathNode cl = getClassOfPackage(p,c);
       MethodNode method = new MethodNode(m);
       
       if(cl !=  null) {
           cl.addNode(method);
           return true;
       }
       return false;
    }
    
    public MethodNode getMethodOfClass(String p, String c, String m) {
        PathNode cl = getClassOfPackage(p,c);
        return cl.getNode(m);
    }
}
