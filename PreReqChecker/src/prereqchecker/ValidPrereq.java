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
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 * 
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */
public class ValidPrereq {
    public static void main(String[] args)
    {

        if ( args.length < 3 )
        {
            StdOut.println("Execute: java -cp bin prereqchecker.ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
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
    String fr = StdIn.readString();
    String se = StdIn.readString();
    boolean va = false;
    boolean vb = false;
    boolean v = false;
    for(ArrayList<String> li : adjList)
    {
        if(li.get(0).contains(fr))
        {
            va = true;
        }
        if(li.get(0).contains(se))
        {
            vb = true;
        }
    }
    if(va&&vb)
    {
        v = true;
    }
    v = valid(se, fr, adjList, v);
    StdOut.setFile(args[2]);
    if(v)
    {
        StdOut.println("YES");
    }
    else
    {
        StdOut.println("NO");
    }
}

private static boolean valid(String a, String b, ArrayList<ArrayList<String>> adjList, boolean re){
Stack <String> sss = new Stack<String>();
sss.push(a);
while(!sss.isEmpty())
{
    for(ArrayList<String> li : adjList)
    {
        if(li.get(0).equals(sss.peek()))
        {
            sss.pop();
            if(li.contains(b))
            {
                re = false;
                return re;
            }
            else
            {
                for(int i = 1; i<li.size(); i++)
                {
                    sss.push(li.get(i));
                }
            }
            break;
        }
    }
}
return re;



    }
}
