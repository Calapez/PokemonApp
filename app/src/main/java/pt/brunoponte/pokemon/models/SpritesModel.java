package pt.brunoponte.pokemon.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Built from the following format:
 * {
 *      "front_default": string_1,
 *      "back_default": string_2
 * }
 */

public class SpritesModel {

    @SerializedName("front_default")
    @Expose
    private String frontUrl;

    @SerializedName("back_default")
    @Expose
    private String backUrl;

    public String getFrontUrl() {
        return frontUrl;
    }

    public String getBackUrl() {
        return backUrl;
    }

}
