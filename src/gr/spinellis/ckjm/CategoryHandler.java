/*
 * Shin && Giga works
 * 
 */

package gr.spinellis.ckjm;

import com.amd.aparapi.Device;
import com.amd.aparapi.Kernel;
import com.amd.aparapi.Kernel.EXECUTION_MODE;
import java.util.ArrayList;
import android.util.*;
import com.amd.aparapi.Range;

public class CategoryHandler
{
    private static CategoryHandler ch = new CategoryHandler();
    
    private Category[] categories;
    private int[][] matrix;
    private int[] tot;
    private float[] fragm;
    private float coeff;
    
    // same indexes
    private ArrayList<String> inputClassName;
    private ArrayList<CalledClassPath[]> calledClassesOfInputClass;
    
    ClassMetricsContainer cmc;
            
    private CategoryHandler()
    {
        inputClassName = new ArrayList<String>();
        calledClassesOfInputClass = new ArrayList<CalledClassPath[]>();
        cmc = ClassMetricsContainer.getCMC();
        cfg();
    }
    
    public static CategoryHandler getCategoryHandler() { return ch; }
    
    // add input classes
    public void addInputClass(String name, CalledClassPath[] classesWhichICall)
    {
        boolean added = false;
        
        if  (inputClassName.add(name))
            added = calledClassesOfInputClass.add(classesWhichICall);
                
        if (!added)
        {
            System.out.println("Error adding input classes in CategoryHandler!");
            System.exit(-1);
        }
    }
    
    // configure categories
    private void cfg()
    {
        categories = new Category[9];
        
        /* concern "CONCURRENT" */
        categories[0] = new Category("concurrent");
        categories[0].addClassToCategory("java.concurrent.Executors");
        categories[0].addClassToCategory("java.concurrent.ExecutorService");
        categories[0].addClassToCategory("java.concurrent.Future");
        //categories[0].addClassToCategory("java.concurrent.Thread");
        //categories[0].addClassToCategory("java.concurrent.Runnable");
        
        /* concern "IO" */
        categories[1] = new Category("io");
        categories[1].addClassToCategory("java.io.File");
        categories[1].addClassToCategory("java.io.FileReader");
        categories[1].addClassToCategory("java.io.FileWriter");
        categories[1].addClassToCategory("java.io.FileInputStream");
        categories[1].addClassToCategory("java.io.FileOutputStream");
        categories[1].addClassToCategory("java.io.BufferedReader");
        categories[1].addClassToCategory("java.io.BufferedWriter");
        categories[1].addClassToCategory("java.io.StringWriter");
        categories[1].addClassToCategory("java.io.PrintStream");
        categories[1].addClassToCategory("java.io.PrintWriter");
        categories[1].addClassToCategory("java.io.OutputStreamWriter");
        
        /* concern "STRING" */
        categories[2] = new Category("string");
        categories[2].addClassToCategory("java.lang.Character");
        categories[2].addClassToCategory("java.lang.String");
        categories[2].addClassToCategory("java.lang.StringBuilder");
        categories[2].addClassToCategory("java.lang.StringBuffer");
        
        /* concern "COMPUTE" */
        categories[3] = new Category("compute");
        categories[3].addClassToCategory("java.lang.Byte");
        categories[3].addClassToCategory("java.lang.Short");
        categories[3].addClassToCategory("java.lang.Integer");
        categories[3].addClassToCategory("java.lang.Long");
        categories[3].addClassToCategory("java.lang.Float");
        categories[3].addClassToCategory("java.lang.Double");
        categories[3].addClassToCategory("java.lang.Enum");
        
        /* concern "EXCEPTION" */
        categories[4] = new Category("exception");
        categories[4].addClassToCategory("java.lang.Exception");
        categories[4].addClassToCategory("java.lang.RuntimeException");
        categories[4].addClassToCategory("java.lang.IllegalArgumentException");
        categories[4].addClassToCategory("java.lang.ClassNotFoundException");
        categories[4].addClassToCategory("java.lang.IllegalAccessException");
        categories[4].addClassToCategory("java.lang.InvocationTargetException");
        
        /* concern "REFLECT" */
        categories[5] = new Category("reflect");
        categories[5].addClassToCategory("java.reflect.Class");
        categories[5].addClassToCategory("java.reflect.Constructor");
        categories[5].addClassToCategory("java.reflect.Method");
        categories[5].addClassToCategory("java.reflect.Field");
        categories[5].addClassToCategory("java.reflect.Modifier");
        categories[5].addClassToCategory("java.reflect.Array");
        categories[5].addClassToCategory("java.reflect.ParameteraizedType");
        
        /* concern "ANNOTATION" */
        categories[6] = new Category("annotation");
        categories[6].addClassToCategory("java.annotation.Annotation");
        categories[6].addClassToCategory("java.annotation.RetentionPolicy");
        categories[6].addClassToCategory("java.annotation.Target");
        
        /* concern "CONTAIN" */
        categories[7] = new Category("contain");
        categories[7].addClassToCategory("java.util.List");
        categories[7].addClassToCategory("java.util.LinkedList");
        categories[7].addClassToCategory("java.util.Arrays");
        categories[7].addClassToCategory("java.util.ArrayList");
        categories[7].addClassToCategory("java.util.Map");
        categories[7].addClassToCategory("java.util.HashMap");
        categories[7].addClassToCategory("java.util.Collection");
        categories[7].addClassToCategory("java.util.Collections");
        categories[7].addClassToCategory("java.util.Set");
        categories[7].addClassToCategory("java.util.Vector");
        categories[7].addClassToCategory("java.util.HashTable");
        categories[7].addClassToCategory("java.util.HashSet");
        categories[7].addClassToCategory("java.util.WeakHashMap");
        categories[7].addClassToCategory("java.util.Enumeration");
        
        /* concern "NET" */
        categories[8] = new Category("net");
        categories[8].addClassToCategory("java.net.DatagramPacket");
        categories[8].addClassToCategory("java.net.DatagramSocket");
        categories[8].addClassToCategory("java.net.Socket");
        categories[8].addClassToCategory("java.net.ServerSocket");
        categories[8].addClassToCategory("java.net.MulticastSocket");
        categories[8].addClassToCategory("java.net.InetAddress");
        //categories[8].addClassToCategory("java.net.Remote");
    }
    
    public void process()
    {
        matrix = new int[inputClassName.size()][categories.length];
        tot = new int[categories.length];
        
        int maxNameLength = getMaxNameLength();
        
        for (int i = 0; i < maxNameLength; i++)
            System.out.print(" ");
        
        System.out.println("\t\t\t\t conc\tio\tstr\tcomp\texcpt\trefl\tannot\tcont\tnet");
        
        for (int inputClassIdx = 0; inputClassIdx < inputClassName.size(); inputClassIdx++)
        {
          System.out.print("Processing \""+inputClassName.get(inputClassIdx)+"\"...");
            CalledClassPath[] classesWhichICall = calledClassesOfInputClass.get(inputClassIdx);
            
            for (int calledClassIdx = 0; calledClassIdx < classesWhichICall.length; calledClassIdx++)
            {
                for (int catIdx = 0; catIdx < categories.length; catIdx++)
                {
                    if (categories[catIdx].check(classesWhichICall[calledClassIdx].getClassName()))
                    {
                        matrix[inputClassIdx][catIdx]+= classesWhichICall[calledClassIdx].getCalledMethodsCount();
                        tot[catIdx] += classesWhichICall[calledClassIdx].getCalledMethodsCount();
                        break; // assuming each class belongs to one (and only one) category
                    }
                }
            }
            
            // print categories datas
            int spaces = maxNameLength - inputClassName.get(inputClassIdx).length();
            for (int i = 0; i < spaces; i++)
                System.out.print(" ");
            
            System.out.print("\t\t  ");
            
            for (int k = 0; k < matrix[inputClassIdx].length; k++)
                System.out.print(matrix[inputClassIdx][k]+"\t");
           
            System.out.println();
        }
        
        System.out.println();
        
        for (int i = 0; i < maxNameLength/6; i++)
            System.out.print("\t");
            
        System.out.print("\tTOTAL:    ");
        
        for (int i = 0; i < categories.length; i++)
            System.out.print(tot[i]+"\t");
        
        System.out.println("\n");
        
        fragm = new float[inputClassName.size()];
        float K = categories.length;
        
        coeff = 1 / (((float)Math.sqrt(K)) - 1);  
    
        // Serial CPU
        serialFragm();
        
        // OpenCL
        /*
        float fragm2[] = parallelFragm();
        
        // check values
        for (int i = 0; i < fragm.length; i++)
            if (!Float.isNaN(fragm[i]) && Math.abs(fragm[i] - fragm2[i]) > 0.0001)
                System.out.println("Wrong values: "+fragm[i]+" vs "+fragm2[i]);
        // */
        
        System.out.println("\t\tClasses with fragm:\n");
        
        // descending sort
        float tmpFragm;
        String tmpName;
        int max;
        
	for (int index = 0; index < fragm.length-1; index++)
	{
		max = index;
                
		for (int i = index+1; i < fragm.length; i++)
			if (fragm[i] > fragm[max])
				max = i;
                
                // swap name and value
		tmpFragm = fragm[index];
                tmpName = inputClassName.get(index);
                
                inputClassName.set(index, inputClassName.get(max));
                fragm[index] = fragm[max];
                
                inputClassName.set(max, tmpName);
                fragm[max] = tmpFragm;
	}
        
        // print fragm results
        for (int inputClassIdx = 0; inputClassIdx < fragm.length; inputClassIdx++)
        {
            if (!Float.isNaN(fragm[inputClassIdx]) && fragm[inputClassIdx] != 0)
            {
                System.out.print("fragm("+inputClassName.get(inputClassIdx)+"):");

                // output format
                int space = maxNameLength - inputClassName.get(inputClassIdx).length();
                for (int i = 0; i < space; i++)
                    System.out.print(" ");
                
                System.out.println("\t"+fragm[inputClassIdx]+"\t{CBO: "+cmc.getMetrics(inputClassName.get(inputClassIdx)).getCbo()+"}\n");
            }
        }
    }
    
    public void serialFragm()
    {
        float itc, itc_sqr;
        for (int inputClassIdx = 0; inputClassIdx < fragm.length; inputClassIdx++)
        {
            itc = itc_sqr = 0;

            for (int k = 0; k < categories.length; k++)
            {
                itc += matrix[inputClassIdx][k];
                itc_sqr += Math.pow(matrix[inputClassIdx][k], 2);
            }
            
            fragm[inputClassIdx] = coeff * (itc/((float)Math.sqrt(itc_sqr)) - 1);
        }
    }
    
    public float[] parallelFragm()
    {
        final int n_$costant$ = fragm.length;
        final int[] matrix2_$costant$ = new int[matrix.length*matrix[0].length];
        final float coeff2_$costant$ = coeff;
        final float fragm2[] = new float[matrix.length];
        final int colLen_$costant$ = matrix[0].length;
        
        for (int i = 0; i < matrix.length; i++)
            for (int j = 0; j < matrix[i].length; j++)
                matrix2_$costant$[i*matrix[0].length+j] = matrix[i][j];

        Kernel kernel = new Kernel()     
        {
            @Override public void run()
            {
                int inputClassIdx = getGlobalId();
                
                if (inputClassIdx >= n_$costant$)
                    return;
                
                int itc = 0, itc_sqr = 0;

                for (int k = 0; k < colLen_$costant$; k++)
                {
                    int curr = matrix2_$costant$[inputClassIdx*colLen_$costant$+k];
                  
                    itc += curr;
                    itc_sqr += (curr*curr);
                }
               
                fragm2[inputClassIdx] = coeff2_$costant$ * ((itc/FloatMath.sqrt(itc_sqr)) - 1);
            }
        };
        
        kernel.setExecutionMode(EXECUTION_MODE.CPU);
        kernel.execute(Range.create(n_$costant$));
        
        kernel.dispose();
        return fragm2;
    }
    
    public int getMaxNameLength()
    {
        int max = 0;
        
        for (int i = 0; i < inputClassName.size(); i++)
            if (max < inputClassName.get(i).length())
                max = inputClassName.get(i).length();
        
        return max;
    }
}
