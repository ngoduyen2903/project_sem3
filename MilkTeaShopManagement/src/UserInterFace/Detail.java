package UserInterFace;

public class Detail {

    private String user;
    private String name;

    public Detail() {
        user = "Milk Tea Shop Management";
        name = "DLT-Milk Tea";
    }

    public Detail(String user, String name) {
        this.user = user;
        this.name = name;
    }

    public Detail(Detail detail) {
        this.user = detail.user;
        this.name = detail.name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
