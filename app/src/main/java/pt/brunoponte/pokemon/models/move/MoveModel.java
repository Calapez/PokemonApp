package pt.brunoponte.pokemon.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoveModel {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

}
