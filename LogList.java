import java.util.ArrayList;

/**
 * LogList Class to store logs and prints them upon adding 
 */
public class LogList {
    private ArrayList<Log> list; 

    public LogList() {
        this.list = new ArrayList<Log>();
    }

    /**
     * Adds specified log to list and prints it 
     * @param l Log to be added 
     */
    public void add(Log l) {
        this.list.add(l); 
        System.out.println(l.toString()); 
    }

    /**
     * Returns the log specified by the index
     * @param i index of the log 
     */
    public Log get(int i) {
        return this.list.get(i); 
    }

    /**
     * Returns the size of the list 
     */
    public int size() {
        return this.list.size(); 
    }

    /**
     * Saves the logs into a txt file 
     */
    // public void saveLogs() {

    // }
}