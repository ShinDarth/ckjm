/*
 * $Id: \\dds\\src\\Research\\ckjm.RCS\\src\\gr\\spinellis\\ckjm\\MetricsFilter.java,v 1.9 2005/08/10 16:53:36 dds Exp $
 *
 * (C) Copyright 2005 Diomidis Spinellis
 *
 * Permission to use, copy, and distribute this software and its
 * documentation for any purpose and without fee is hereby granted,
 * provided that the above copyright notice appear in all copies and that
 * both that copyright notice and this permission notice appear in
 * supporting documentation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 * MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package gr.spinellis.ckjm;

import org.apache.bcel.classfile.*;
import java.io.*;
import java.util.*;

/**
 * Convert a list of classes into their metrics.
 * Process standard input lines or command line arguments
 * containing a class file name or a jar file name,
 * followed by a space and a class file name.
 * Display on the standard output the name of each class, followed by its
 * six Chidamber Kemerer metrics:
 * WMC, DIT, NOC, CBO, RFC, LCOM
 *
 * @see ClassMetrics
 * @version $Revision: 1.9 $
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
public class MetricsFilter {
    /** True if the measurements should include calls to the Java JDK into account */
    private static boolean includeJdk = false;

    /** True if the reports should only include public classes */
    private static boolean onlyPublic = false;

    /** Return true if the measurements should include calls to the Java JDK into account */
    public static boolean isJdkIncluded() { return includeJdk; }
    /** Return true if the measurements should include all classes */
    public static boolean includeAll() { return !onlyPublic; }

    /**
     * Load and parse the specified class.
     * The class specification can be either a class file name, or
     * a jarfile, followed by space, followed by a class file name.
     */
    static void processClass(ClassMetricsContainer cm, String clspec) {
	int spc;
	JavaClass jc = null;

	if ((spc = clspec.indexOf(' ')) != -1) {
	    String jar = clspec.substring(0, spc);
	    clspec = clspec.substring(spc + 1);
	    try {
		jc = new ClassParser(jar, clspec).parse();
	    } catch (Exception e) {
		System.err.println("Error loading " + clspec + " from " + jar + ": " + e);
	    }
	} else {
	    try {
                
		jc = new ClassParser(clspec).parse();
	    } catch (Exception e) {
		System.err.println("Error loading " + clspec + ": " + e);
	    }
	}
	if (jc != null) {
	    ClassVisitor visitor = new ClassVisitor(jc, cm);
	    visitor.start();
	    visitor.end();
	}
    }

    /** The filter's main body.
     * Process command line arguments and the standard input.
     */
    public static void main(String[] argv) {
	int argp = 0;
        ClassMetricsContainer cm = ClassMetricsContainer.getCMC();
        
        // Shin && Giga works
        if (argv.length > 0 && argv[argp].startsWith("-c"))
        {
            cm.enableShinAndGiga();
            
            // check extra params
            int i = 2;
            while (i < argv[argp].length())
            {
                if (argv[argp].charAt(i) == 'D')
                    cm.enableDetails();
                
                if (argv[argp].charAt(i) == 'G')
                    cm.enableGlobalDetails();
                
                if (argv[argp].charAt(i) == 'F')
                    cm.enableFanIn();
                
                i++;
            }
            
            argp++;
            includeJdk = true;
            onlyPublic = false;
            
            if (cm.fanIn())
            {
                String packageName = "";
                System.out.print("Insert a package name to calc fan-in: ");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                try
                {
                    packageName = in.readLine();
                    in.close();
                }
                catch(Exception e)
                {
                    System.err.println("Error reading line: " + e);
                        System.exit(1);
                }
                cm.setPackageName(packageName);
            }
        }
        else // standard ckjm
        {
            if (argv.length > argp && argv[argp].equals("-s")) {
                includeJdk = true;
                argp++;
            }
            if (argv.length > argp && argv[argp].equals("-p")) {
                onlyPublic = true;
                argp++;
            }

            if (argv.length == argp) {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                try {
                    String s;
                    while ((s = in.readLine()) != null)
                        processClass(cm, s);
                } catch (Exception e) {
                    System.err.println("Error reading line: " + e);
                    System.exit(1);
                }
            }
        }

	for (int i = argp; i < argv.length; i++)
	    processClass(cm, argv[i]);
        
        // Shin && Giga works
        if (cm.globalDetailsEnabled())
        {
            // Organizing all packages/classes/methods called by each visited class
           CalledClassPath allCalledClasses[][] = cm.getAllCalledClasses();
           DataHandler dataHandler = DataHandler.getDataHandler();
           
           for (int i = 0; i < allCalledClasses.length; i++)
           {
               for (int j = 0; j < allCalledClasses[i].length; j++)
               {
                   String fullName = allCalledClasses[i][j].getClassName(), packageName, className;
                   
                   int x = fullName.length() - 1;
                   while(fullName.charAt(x) != '.') x--;
                   
                   packageName = fullName.substring(0, x);
                   className = fullName.substring(x+1, fullName.length());
                   
                   PathNode currentPackage = dataHandler.getPackage(packageName);
                   PathNode currentClass;
                   
                   // check if package and class are in dataHandler, add them if not
                   if (currentPackage != null) // package is already in dataHandler
                   {
                       currentClass = (PathNode)currentPackage.getNode(className);
                       
                       if (currentClass == null) // class is already in dataHandler
                       {
                           dataHandler.addClassToPackage(packageName, className);
                           
                           currentClass = dataHandler.getClassOfPackage(packageName, className);
                       }
                   }
                   else // package is not in dataHandler yet
                   {
                       dataHandler.addPackage(packageName);
                       dataHandler.addClassToPackage(packageName, className);
                       
                       currentPackage = dataHandler.getPackage(packageName);
                       currentClass = dataHandler.getClassOfPackage(packageName, className);
                   }
                   
                   // now both package and class should be in dataHandler
                   if (currentPackage == null || currentClass == null) // test check
                   {
                       System.out.println("Error! dataHandler is not fine!");
                       System.exit(-1);
                   }
                   
                   // add methods to class
                   HashMap<String, Integer> currentMethods =  allCalledClasses[i][j].getMethods();
                   Iterator methodsItr = currentMethods.keySet().iterator();
                   
                   while (methodsItr.hasNext())
                   {
                       String methodName = (String)methodsItr.next();
                       int methodCount = currentMethods.get(methodName);
                       
                       MethodNode currentMethod= dataHandler.getMethodOfClass(packageName, className, methodName);
                       
                       if (currentMethod == null) // method is not in dataHandler yet
                       {
                           // add a new method
                           dataHandler.addMethodToClass(packageName, className, methodName);
                           
                           // now currentMethod should no longer be null
                           currentMethod = dataHandler.getMethodOfClass(packageName, className, methodName);
                       }
                       
                       // update countck
                       currentMethod.incrCount(methodCount);
                   }
               }
           }
           
           // update count of all nodes
           dataHandler.countAll();
           
           // print everything
           System.out.println("\n\n********* All packages called by all analyzed .class files *********");
           
           System.out.println(dataHandler.toString());
           
           System.out.println("\n******************************************************************\n");
        }
        
        if (cm.ShinAndGigaWorks())
            CategoryHandler.getCategoryHandler().process();
        else
        {
            CkjmOutputHandler handler = new PrintPlainResults(System.out);
            cm.printMetrics(handler);
        }
    }
}
