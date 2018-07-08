package aesher.milan.milan_root.constants;

public class NewUsers {

    private String UID, name, contact, position;

    NewUsers(){};

    public NewUsers(String UID,String name, String contact, String position){
        this.UID = UID;
        this.name = name;
        this.contact = contact;
        this.position = position;
    }


    public String getUID() {
        return UID;
    }
    public String getName() {
        return name;
    }
    public String getContact() {
        return contact;
    }
    public String getPosition() {
        return position;
    }
}
