package prereqchecker;

import java.util.*;

/**
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class Eligible
{
    public static void main(String[] args)
    {

        if ( args.length < 3 )
        {
            StdOut.println("Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
            return;
        }

        StdIn.setFile(args[0]);
        String [] nme = StdIn.readAllStrings();
        int a = Integer.parseInt(nme[0]);
        int b = Integer.parseInt(nme[a+1]);
        ArrayList<ArrayList<String>> AdjList = new ArrayList<ArrayList<String>>();
        for(int i= 1; i<a+1; i++)
        {
            ArrayList<String> ad = new ArrayList<String>();
            ad.add(nme[i]);
            AdjList.add(ad);
        }
        for(int i = a+2; i<nme.length; i+=2)
        {
            for(ArrayList<String> li : AdjList)
            {
                if(nme[i].equals(li.get(0)))
                {
                    li.add(nme[i+1]);
                }
            }
        }

        StdIn.setFile(args[1]);
        int c = StdIn.readInt();
        String [] nme1 = StdIn.readAllStrings();
        ArrayList<String> taken = new ArrayList<String>();
        for(String cour : nme1)
        {
            taken.add(cour);
        }
        for(int i = 0; i<AdjList.size(); i++)
        {
            if(taken.contains(AdjList.get(i).get(0)))
            {
                for(String cours : AdjList.get(i))
                {
                    if(!taken.contains(cours))
                    {
                        taken.add(cours);
                        i=0;
                    }
                }
            }
        }
        StdOut.setFile(args[2]);
        for(ArrayList<String> li : AdjList)
        {
            boolean res = true;
            if(taken.contains(li.get(0)))
            {
                res = false;
            }
            else
            {
                for(int i = 1; i<li.size(); i++)
                {
                    if(!taken.contains(li.get(i)))
                    {
                        res = false;
                    }
                }
            }
            if(res)
            {
                StdOut.println(li.get(0));
            }
        }
    }
}