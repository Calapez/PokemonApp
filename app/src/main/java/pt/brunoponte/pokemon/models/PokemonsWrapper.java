package pt.brunoponte.pokemon.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ContactsWrapper implements Parcelable {

    //@SerializedName("name")
    private String name;
    //@SerializedName("url")
    private String url;
    private String photoUrl;

    public ContactsWrapper(String name, String url)  {
        this.name = name;
        this.url = url;
        this.photoUrl = "";
    }

    public ContactsWrapper(String name, String url, String photoUrl)  {
        this.name = name;
        this.url = url;
        this.photoUrl = photoUrl;
    }

    public ContactsWrapper(Parcel in) {
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

    public static final Creator<ContactsWrapper> CREATOR = new Creator<ContactsWrapper>() {
        @Override
        public ContactsWrapper createFromParcel(Parcel in) {
            return new ContactsWrapper(in);
        }

        @Override
        public ContactsWrapper[] newArray(int size) {
            return new ContactsWrapper[size];
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
