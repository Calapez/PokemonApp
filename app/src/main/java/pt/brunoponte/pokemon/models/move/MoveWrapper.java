package pt.brunoponte.pokemon.models.move;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Built from the following format:
 * {
 *      "move": {MoveModel}"
 * }
 */

public class MoveWrapper {

    @SerializedName("move")
    @Expose
    private MoveModel move;

    public MoveModel getMove() {
        return move;
    }

}
