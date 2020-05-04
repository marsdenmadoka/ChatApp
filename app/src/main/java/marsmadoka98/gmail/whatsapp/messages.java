package marsmadoka98.gmail.whatsapp;

public class messages {

    private  String from,message,type; //should be the same as those in db

    public messages(){

    }

    public messages(String from, String message, String type) {
        this.from = from;
        this.message = message;
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
