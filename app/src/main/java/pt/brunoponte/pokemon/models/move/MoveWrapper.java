package pt.brunoponte.pokemon.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoveWrapper {

    @SerializedName("move")
    @Expose
    private MoveModel move;

    public MoveModel getMove() {
        return move;
    }

}
