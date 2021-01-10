package pt.brunoponte.pokemon.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbilityWrapper {

    @SerializedName("ability")
    @Expose
    private AbilityModel ability;

    public AbilityModel getMove() {
        return ability;
    }

}
