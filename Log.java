import java.sql.Timestamp;

/*
    Log Class 
    Object which contains the logged activities of the clients 
    @attribute source Source/sender of the activity 
    @attribute dest Recipient of the sent file/message
    @attribute activityType Type of the activity (LOGIN, LOGOUT, FILE, MESSAGE)
    @attribute isSender True if the source is also the actor of the action
    @attribute timeSent Timestamp of when the activity was done (YY:MM:DD HH:MM:SS)
*/
public class Log {
    private String source;
    private String dest; 
    private String activityType; 
    private String message;
    private boolean isSender; 
    private Timestamp timeSent; 

    /* 
        Constructor for type LOGIN, LOGOUT, DISCONNECT, 
        @param source Client that performed the log 
        @param activityType Type of the activity that was logged
    */
    public Log(String source, String activityType) {
        this.source = source; 
        this.activityType = activityType; 
        this.timeSent = new Timestamp(System.currentTimeMillis());
    }

    /*
        Constructor for type FAILRECEIVEMSG, FAILSENDMSG, FAILRECEIVEFILE, FAILSENDFILE
    */
    public Log(String source, String activityType, boolean isSender) {
        this(source, activityType);
        this.isSender = isSender; 
    }

    /* 
        Constructor for type FILE
        @param source Client that performed the log 
        @param activityType Type of the activity that was logged
        @param dest Destination client of the activity 
        @param isSender True if the source is also the sender of the file; False otherwise
    */
    public Log(String source, String activityType, String dest, boolean isSender) {
        this(source, activityType);
        this.dest = dest; 
        this.isSender = isSender; 
    }

    /* 
        Constructor for type MESSAGE
        @param source Client that performed the log 
        @param activityType Type of the activity that was logged
        @param dest Destination client of the activity 
        @param message Message that was sent by the source 
        @param isSender True if the source is also the sender of the file; False otherwise
    */
    public Log(String source, String activityType, String dest, boolean isSender, String message) {
        this(source, activityType, dest, isSender);
        this.message = message; 
    }

    /*
        Returns a string version of the log based on the activity type 
    */
    @Override 
    public String toString() {
        if (this.activityType.equals("LOGIN")) {
            return "(" + this.timeSent + ") " + this.source + ": User logged in"; 
        }
        else if (this.activityType.equals("LOGOUT")) {
            return "(" + this.timeSent + ") " + this.source + ": User logged out / disconnected"; 
        }
        else if (this.activityType.equals("DISCONNECT")) {
            return "(" + this.timeSent + ") " + this.source + ": User disconnected";
        }
        else if (this.activityType.equals("FAILSENDMSG")) {
            return "(" + this.timeSent + ") " + this.source + ": Failed to send message.";
        }
        else if (this.activityType.equals("FAILRECEIVEMSG")) {
            return "(" + this.timeSent + ") " + this.source + ": Failed to receive message.";
        }
        else if (this.activityType.equals("FAILSENDFILE")) {
            return "(" + this.timeSent + ") " + this.source + ": Failed to send file.";
        }
        else if (this.activityType.equals("FAILRECEIVEFILE")) {
            return "(" + this.timeSent + ") " + this.source + ": Failed to receive file.";
        }
        else if (this.activityType.equals("FILE")) {
            if (this.isSender)
                return "(" + this.timeSent + ") " + this.source + ": Sent a file to " + this.dest;  
            else 
                return "(" + this.timeSent + ") " + this.source + ": Received a file from " + this.dest;
        }
        else if (activityType.equals("MESSAGE")) {
            if (this.isSender) 
                return "(" + this.timeSent + ") " + this.source + ": Sent a message to" + this.dest + " " + this.message ;
            else 
                return "(" + this.timeSent + ") " + this.source + ": Received a message from" + this.dest + " " + this.message ;
        }
        else {
            return "Unknown activity type recorded." + this.activityType; 
        }
    }
}