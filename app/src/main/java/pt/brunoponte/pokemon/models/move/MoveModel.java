package pt.brunoponte.pokemon.models.move;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Built from the following format:
 * {
 *      "name": "razor-wind"
 * }
 */

public class MoveModel {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

}
