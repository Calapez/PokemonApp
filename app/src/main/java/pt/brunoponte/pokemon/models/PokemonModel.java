package pt.brunoponte.pokemon.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
