package pt.brunoponte.pokemon;

public class Pokemon {

    private String name;
    private String endpoint;
    private String photoUrl;

    public Pokemon(String name, String endpoint) {
        this.name = name;
        this.endpoint = endpoint;
        this.photoUrl = "";
    }

    public String getName() {
        return name;
    }
    public String getEndpoint() {
        return endpoint;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
