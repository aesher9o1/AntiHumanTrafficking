package aesher.milan.milan_root.constants;

public class LostChild {

    private String name, description , contact, pictureUrl;

    LostChild(){}

    public LostChild(String name, String description, String contact, String pictureUrl){
        this.name = name;
        this.description = description;
        this.contact =contact;
        this.pictureUrl = pictureUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getContact() {
        return contact;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }
}


