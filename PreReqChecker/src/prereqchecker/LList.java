package prereqchecker;

public class LList {
    public LList next;
    private String data;

    public LList() {

    }

    public LList(String d) {
        data = d;
    }
    
    public LList(LList n, String d) {
        data = d;
        next = n;
    }

    public LList getNext()
    {
        return next;
    }

    public String getData()
    {
        return data;
    }
}