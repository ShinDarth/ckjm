/*
 * $Id: \\dds\\src\\Research\\ckjm.RCS\\src\\gr\\spinellis\\ckjm\\ClassMetricsContainer.java,v 1.9 2005/08/10 16:53:36 dds Exp $
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
import java.util.*;
import java.io.*;


/**
 * A container of class metrics mapping class names to their metrics.
 * This class contains the the metrics for all class's during the filter's
 * operation.  Some metrics need to be updated as the program processes
 * other classes, so the class's metrics will be recovered from this
 * container to be updated.
 *
 * @version $Revision: 1.9 $
 * @author <a href="http://www.spinellis.gr">Diomidis Spinellis</a>
 */
class ClassMetricsContainer
{
    // Shin && Giga works: singletoned
    private static ClassMetricsContainer cmc = new ClassMetricsContainer();
    public static ClassMetricsContainer getCMC()    { return cmc; }
    private ClassMetricsContainer() {}

    /** The map from class names to the corresponding metrics */
    private HashMap<String, ClassMetrics> m = new HashMap<String, ClassMetrics>();
    
    // Shin && Giga works {{{
    private String packageName;
    private boolean ShinAndGigaEnabled = false, showDetails = false, fanIn = false, globalDetails;
    
    public String getPackageName()                          { return packageName; }
    public void setPackageName(String str)                  { packageName = str; }
    
    public boolean detailsEnabled()                         { return showDetails; }
    public boolean globalDetailsEnabled()                   { return globalDetails; }
    public boolean fanIn()                                  { return fanIn; }
    public boolean ShinAndGigaWorks()                       { return ShinAndGigaEnabled; }
    
    public void enableDetails()                             { showDetails = true; }
    public void enableGlobalDetails()                       { globalDetails = true; }
    public void enableFanIn()                               { fanIn = true; }
    public void enableShinAndGiga()                         { ShinAndGigaEnabled = true; }
    
    public CalledClassPath[][] getAllCalledClasses()
    {
        CalledClassPath allCalledClasses[][] = new CalledClassPath[m.size()][];
        Iterator itr = m.values().iterator();
        int i = 0;
        
        while (itr.hasNext())
        {
            allCalledClasses[i] = ((ClassMetrics)(itr.next())).getClassesWhichICall();
            i++;
        }
        
        return allCalledClasses;
    }
    // Shin && Giga works }}}
    
    /** Return a class's metrics */
    public ClassMetrics getMetrics(String name) {
	ClassMetrics cm = m.get(name);
	if (cm == null) {
	    cm = new ClassMetrics();
	    m.put(name, cm);
	}
	return cm;
    }

    /** Print the metrics of all the visited classes. */
    public void printMetrics(CkjmOutputHandler handler) {
	Set<Map.Entry<String, ClassMetrics>> entries = m.entrySet();
	Iterator<Map.Entry<String, ClassMetrics>> i;

	for (i = entries.iterator(); i.hasNext(); ) {
	    Map.Entry<String, ClassMetrics> e = i.next();
	    ClassMetrics cm = e.getValue();
	    if (cm.isVisited() && (MetricsFilter.includeAll() || cm.isPublic()))
		handler.handleClass(e.getKey(), cm);
	}
    }
}
