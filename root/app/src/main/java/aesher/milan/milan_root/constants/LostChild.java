package aesher.milan.milan_root.constants;

public class LostChild {

    private String firstName,lastName,age, height, weight,id1, id2, description , contact, pictureUrl, complexion, incharge;

    LostChild(String first_name, String last_name){}

    public LostChild(String firstName,String lastName,String age,String height, String weight,
                     String id1,String id2, String description, String contact, String pictureUrl, String complexion, String incharge){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.id1 = id1;
        this.id2 = id2;
        this.description = description;
        this.contact =contact;
        this.pictureUrl = pictureUrl;
        this.complexion = complexion;
        this.incharge = incharge;
    }

    public String getIncharge() {
        return incharge;
    }

    public String getName() {
        return firstName+" "+lastName;
    }

    public String getComplexion() {
        return complexion;
    }

    public String getAge() {
        return age;
    }

    public String getHeight() {
        return height;
    }

    public String getId1() {
        return id1;
    }

    public String getId2() {
        return id2;
    }

    public String getWeight() {
        return weight;
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


