package marsmadoka98.gmail.whatsapp;

public class messages {

    private  String from,message,type; //should be the same as those in db

    public messages(){

    }
    public messages(String from, String messages, String type) {
        this.from = from;
        this.message = messages;
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

    public void setMessage(String messages) {
        this.message = messages;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
