package pt.brunoponte.pokemon.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
