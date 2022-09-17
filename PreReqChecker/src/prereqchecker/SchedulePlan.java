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
 * SchedulePlanInputFile name is passed through the command line as args[1]
 * Read from SchedulePlanInputFile with the format:
 * 1. One line containing a course ID
 * 2. c (int): number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * SchedulePlanOutputFile name is passed through the command line as args[2]
 * Output to SchedulePlanOutputFile with the format:
 * 1. One line containing an int c, the number of semesters required to take the course
 * 2. c lines, each with space separated course ID's
 */
public class SchedulePlan
{
    
    
    public static int[] getlayer(LList[] courselist, int startind)
    {
        boolean[] boo = new boolean[courselist.length];
        int[] length = new int[courselist.length];
        Queue<Integer> s = new LinkedList<Integer>();
        s.add(startind);
        boo[startind]=true;
        while(!s.isEmpty())
        {
            int upper = s.remove();
            LList temp = courselist[upper].next;
            while(temp!=null)
            {
                int x=-1;
                for(int i=0;i<courselist.length;i++)
                {
                    if(courselist[i].getData().equals(temp.getData())) x=i;
                }

                s.add(x);
                
                boo[x]=true;
                length[x]=length[upper]+1;
                temp=temp.next;
            }
        }

        return length;
    }
    
    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println("Execute: java -cp bin prereqchecker.SchedulePlan <adjacency list INput file> <schedule plan INput file> <schedule plan OUTput file>");
            return;
        }

        StdIn.setFile(args[0]);

        int a = StdIn.readInt();
        StdIn.readLine();
        LList[] courselist = new LList[a];

        for(int i=0;i<a;i++)
        {
            courselist[i] = new LList(StdIn.readLine());
        }

        int b = StdIn.readInt();
        StdIn.readLine();
        for(int i=0;i<b;i++)
        {
            String hit = StdIn.readString();
            String pr = StdIn.readString();
            StdIn.readLine();
            for(int j=0;j<a;j++)
            {
                if(courselist[j].getData().equals(hit))
                {
                    LList append = courselist[j].next;
                    courselist[j].next = new LList(pr);
                    courselist[j].next.next=append;
                    break;
                }
            }
        }

        StdIn.setFile(args[1]);
        
        String goal = StdIn.readLine();
        int e = StdIn.readInt();
        StdIn.readLine();

        boolean[] taken = new boolean[a];

        for(int i=0;i<e;i++)
        {
            
            Stack<Integer> stack = new Stack<Integer>();
            int index = -1;
            String s = StdIn.readLine();
            for(int j=0;j<courselist.length;j++)
            {
                if(courselist[j].getData().equals(s)) index=j;
            }


            stack.push(index);
            while(!stack.isEmpty())
            {
                int v = stack.pop();
                if(taken[v]) continue;
                taken[v]=true;
                LList temp = courselist[v];
                temp=temp.next;

                while(temp!=null)
                {
                    int n = -1;
                    
                    for(int j=0;j<courselist.length;j++)
                    {
                        if(courselist[j].getData().equals(temp.getData())) n=j;
                    }

                    if(!taken[n])
                    {
                        stack.push(n);
                    }
                    temp=temp.next;
                }
            }
        }

        boolean[] pre = new boolean[a];

        int nn = -1;
        
        for(int i=0;i<courselist.length;i++)
        {
            if(courselist[i].getData().equals(goal)) nn=i;
        }

        Stack<Integer> s = new Stack<Integer>();
        s.push(nn);
        while(!s.isEmpty())
        {
            int v = s.pop();
            if(pre[v]) continue;
            pre[v]=true;
            LList temp = courselist[v].next;
            while(temp!=null)
            {
                int n=-1;
                for(int i=0;i<courselist.length;i++)
                {
                    if(courselist[i].getData().equals(temp.getData())) n=i;
                }
                
                if(!pre[n]) s.push(n);
                temp=temp.next;
            }
        }

        boolean[] ftr = new boolean[a];

        int x=-1;
        for(int i=0;i<courselist.length;i++)
        {
            if(courselist[i].getData().equals(goal)) x=i;
        }

        int[] len = getlayer(courselist,x);

        for(int i=0;i<a;i++)
        {
            if(pre[i])
            {
                if(!taken[i]) ftr[i]=true;
            }
        }
        int u=0;
        int ctr=0;
        while(u<ftr.length)
        {
            if(ftr[u]==false) ctr++;
            u++;
        }
        int max=-1;
        if(ctr>=0) max = -2;
        for(int i=0;i<a;i++)
        {
            if(len[i]>max)
            {
                if(ftr[i]) max=len[i];
                else continue;
            }
        }
        StdOut.setFile(args[2]);
        StdOut.println(max);
        for(int i=max;i>0;i--)
        {
            for(int j=0;j<a;j++)
            {
                if(len[j]==i && ftr[j]) StdOut.print(courselist[j].getData()+" ");
            }
            StdOut.println();
        }

    }

}
