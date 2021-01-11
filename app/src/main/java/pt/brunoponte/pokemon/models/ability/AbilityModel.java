package pt.brunoponte.pokemon.models.ability;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Built from the following format:
 * {
 *      "name": "overgrow"
 * }
 */

public class AbilityModel {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

}
