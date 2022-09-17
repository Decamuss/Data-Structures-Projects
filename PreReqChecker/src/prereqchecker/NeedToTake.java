package prereqchecker;

import java.util.*;

/**
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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class NeedToTake {
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>");
            return;
        }


        StdIn.setFile(args[0]);
        String [] nme = StdIn.readAllStrings();
        int a = Integer.parseInt(nme[0]);
        int b = Integer.parseInt(nme[a+1]);
        ArrayList<ArrayList<String>> adjList = new ArrayList<ArrayList<String>>();
    
        for(int i= 1; i<a+1; i++)
        {
            ArrayList<String> adj = new ArrayList<String>();
            adj.add(nme[i]);
            adjList.add(adj);
        }
        for(int i = a+2; i<nme.length; i+=2)
        {
            for(ArrayList<String> li : adjList)
            {
                if(nme[i].equals(li.get(0)))
                {
                    li.add(nme[i+1]);
                }
            }
        }
   
        StdIn.setFile(args[1]);
        String futr = StdIn.readString();
        int d = StdIn.readInt();
        String [] ceee = StdIn.readAllStrings();
        ArrayList<String> coursesTaken = new ArrayList<String>();
        for(String course : ceee){
            coursesTaken.add(course);
        }
    
        for(int i = 0; i<adjList.size(); i++)
        {
            if(coursesTaken.contains(adjList.get(i).get(0)))
            {
                for(String courses : adjList.get(i))
                {
                    if(!coursesTaken.contains(courses))
                    {
                        coursesTaken.add(courses);
                        i=0;
                    }
                }
            }
        }
        ArrayList<String> prereqs = new ArrayList<String>();
        for(ArrayList<String> cr : adjList){
            if(cr.get(0).equals(futr)){
                for(String c : cr){
                    prereqs.add(c);
                }
            }
        }
     
        prereqs.remove(0);
     
        for(int i = 0; i<adjList.size(); i++){
            if(prereqs.contains(adjList.get(i).get(0))){
                for(String courses : adjList.get(i)){
                    if(!prereqs.contains(courses)){
                        prereqs.add(courses);
                        i=0;
                    }
                }
            }
        }

        StdOut.setFile(args[2]);

        for(String pre : prereqs){
            if(!coursesTaken.contains(pre)){
                StdOut.println(pre);
            }
        }
    }
}