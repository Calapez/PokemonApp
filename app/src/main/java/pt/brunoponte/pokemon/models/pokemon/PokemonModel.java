package pt.brunoponte.pokemon.models.pokemon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import pt.brunoponte.pokemon.models.SpritesModel;
import pt.brunoponte.pokemon.models.ability.AbilityWrapper;
import pt.brunoponte.pokemon.models.move.MoveWrapper;

/**
 * Built from the following format:
 * {
 *      "name": string
 *      "abilities": [AbilityWraper, AbilityWraper]",
 *      "moves": [MoveWraper, MoveWraper],
 *      "sprites": SpritesModel
 *      "weight": int
 * }
 */

public class PokemonModel {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("weight")
    @Expose
    private int weight;

    @SerializedName("moves")
    @Expose
    private List<MoveWrapper> moveWrappers;

    @SerializedName("abilities")
    @Expose
    private List<AbilityWrapper> abilityWrappers;

    @SerializedName("sprites")
    private SpritesModel sprites;

    public String getName() {
        return name;
    }
    public int getWeight() {
        return weight;
    }
    public List<MoveWrapper> getMovesWrapper() {
        return moveWrappers;
    }
    public List<AbilityWrapper> getAbilitiesWrapper() {
        return abilityWrappers;
    }
    public SpritesModel getSprites() {
        return sprites;
    }

}
