package marsmadoka98.gmail.whatsapp;


//in order to use the new firebase recycle adpater this class is needed
//NOTE THIS FILE IS LINKED TO THE FINDFRIENDS ACTIVITYt
public class Contacts {
    public  String name;
    public  String status;
    public  String image;

    public Contacts(){ //tis empty constructor MUST be there

    }
    public Contacts(String name, String status, String image) {
        this.name = name;
        this.status = status;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
