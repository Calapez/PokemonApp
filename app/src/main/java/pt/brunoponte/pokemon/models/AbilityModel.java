package pt.brunoponte.pokemon.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbilityModel {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

}
