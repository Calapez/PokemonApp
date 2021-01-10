package pt.brunoponte.pokemon.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Pokemon implements Parcelable {

    private String name;
    private String endpoint;
    private String photoUrl;

    public Pokemon(String name, String endpoint)  {
        this.name = name;
        this.endpoint = endpoint;
        this.photoUrl = "";
    }

    public Pokemon(String name, String endpoint, String photoUrl)  {
        this.name = name;
        this.endpoint = endpoint;
        this.photoUrl = photoUrl;
    }

    public Pokemon(Parcel in) {
        name = in.readString();
        endpoint = in.readString();
        photoUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(endpoint);
        dest.writeString(photoUrl);
    }

    public static final Creator<Pokemon> CREATOR = new Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };

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
