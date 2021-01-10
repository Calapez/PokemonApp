package pt.brunoponte.pokemon.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PokemonModel implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    private String photoUrl;

    public PokemonModel(String name, String url)  {
        this.name = name;
        this.url = url;
        this.photoUrl = "";
    }

    public PokemonModel(String name, String url, String photoUrl)  {
        this.name = name;
        this.url = url;
        this.photoUrl = photoUrl;
    }

    public PokemonModel(Parcel in) {
        name = in.readString();
        url = in.readString();
        photoUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        dest.writeString(photoUrl);
    }

    public static final Creator<PokemonModel> CREATOR = new Creator<PokemonModel>() {
        @Override
        public PokemonModel createFromParcel(Parcel in) {
            return new PokemonModel(in);
        }

        @Override
        public PokemonModel[] newArray(int size) {
            return new PokemonModel[size];
        }
    };

    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


}
