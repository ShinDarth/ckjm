/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.spinellis.ckjm;

import java.util.ArrayList;

/**
 *
 * @author Gabriele Gelardi
 */
public class DataHandler {

    ArrayList<Node> data;

    public DataHandler(String signature) {
        data = new ArrayList();

    }

    public void addPackage(String p) {
        Node pack = new Node(p);
        data.add(pack);
    }

    public Node getPackage(String p) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(p)) 
                return data.get(i);    
        }
        return null;
    }
    public Node getClassOfPackage(String p, String c) {
        Node pack = getPackage(p);
        return (Node)pack.getNode(c);
    }

    public boolean addClasstoPackage(String p, String c) {
        Node pack = getPackage(p);
        Node cl = new Node(c);
        
        if(pack != null) {
            pack.addNode(cl);
            return true;
        }
        return false;
    }

    public boolean addMethodtoClass(String p, String c, String m) {
       Node cl = getClassOfPackage(p,c);
       SimpleNode method = new SimpleNode(m);
       
       if(cl !=  null) {
           cl.addNode(method);
           return true;
       }
       return false;
    }
    public SimpleNode getMethodOfClass(String p, String c, String m) {
        Node cl = getClassOfPackage(p,c);
        return cl.getNode(m);
    }
}
