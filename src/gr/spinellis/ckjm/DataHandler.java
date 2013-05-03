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
import java.util.Iterator;

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
    
    public void countAll()
    {
        // count each package
        for (int i = 0; i < data.size(); i++)
        { 
                ArrayList<MethodNode> currentPackage = data.get(i).getArrayList();
                
                // counting each class
                for (int j = 0; j < currentPackage.size(); j++)
                    ((PathNode)currentPackage.get(j)).count();
                
                data.get(i).count();
        }
    }
    
    @Override
    public String toString()
    {
        String out = "";
        
        Iterator pkgItr = data.iterator();
        
        while (pkgItr.hasNext())
        {
            PathNode currentPackage = (PathNode)pkgItr.next();
            out += "Package: "+currentPackage.getName()+" ["+currentPackage.getCount()+"] \n{\n";
            
            Iterator classItr = currentPackage.getArrayList().iterator();
            
            while (classItr.hasNext())
            {
                PathNode currentClass = (PathNode)classItr.next();
                out += "\tClass: "+currentClass.getName()+" ["+currentClass.getCount()+"] \n\t{\n";
                
                Iterator mtdItr = currentClass.getArrayList().iterator();
                while (mtdItr.hasNext())
                {
                    MethodNode currentMethod = (MethodNode)mtdItr.next();
                    out += "\t\tMethod: "+currentMethod.getName()+"  ["+currentMethod.getCount()+"]\n";
                }
                out += "\t}\n\n";
            }
            out += "}\n\n";
        }
        
        return out;
    }
}