package pt.brunoponte.pokemon.models.pokemon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Built from the following format:
 * {
 *      "results": [SimplePokemonModel, SimplePokemonModel, SimplePokemonModel]"
 * }
 */

public class SimplePokemonsWrapper {

    @SerializedName("results")
    @Expose
    private List<SimplePokemonModel> pokemons;

    public List<SimplePokemonModel> getPokemons() {
        return pokemons;
    }

}
