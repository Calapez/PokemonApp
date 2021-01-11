package pt.brunoponte.pokemon.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SimplePokemonModel implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    private String photoUrl;

    public SimplePokemonModel(Parcel in) {
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

    public static final Creator<SimplePokemonModel> CREATOR = new Creator<SimplePokemonModel>() {
        @Override
        public SimplePokemonModel createFromParcel(Parcel in) {
            return new SimplePokemonModel(in);
        }

        @Override
        public SimplePokemonModel[] newArray(int size) {
            return new SimplePokemonModel[size];
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

    public void setPhotoUrl(String url) {
        photoUrl = url;
    }

}
