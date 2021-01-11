package pt.brunoponte.pokemon.models.ability;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Built from the following format:
 * {
 *      "ability": AbilityModel"
 * }
 */

public class AbilityWrapper {

    @SerializedName("ability")
    @Expose
    private AbilityModel ability;

    public AbilityModel getAbility() {
        return ability;
    }

}
