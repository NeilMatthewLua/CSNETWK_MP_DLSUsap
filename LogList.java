import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException; 
/**
 * LogList Class to store logs and prints them upon adding 
 */
public class LogList {
    private ArrayList<Log> list; 
    private boolean accepting; 

    public LogList() {
        this.accepting = true; 
        this.list = new ArrayList<Log>();
    }

    /**
     * Adds specified log to list and prints it 
     * @param l Log to be added 
     */
    public void add(Log l) {
        if (accepting) {
            this.list.add(l); 
            System.out.println(l.toString()); 
        }   
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
     * Sets the accepting boolean to specified parameter 
     */
    public void setAccepting(boolean value) {
        this.accepting = value; 
    }

    /**
     * Saves the logs into a txt file 
     */
    public void saveLogs() {
        try {
            FileWriter writer = new FileWriter("logs.txt", false);
            for (Log c : this.list) {
                writer.write(c.toString()); 
                writer.write(System.getProperty("line.separator")); 
            }
            writer.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred in saving the file.");
            e.printStackTrace();
          }
    }
}