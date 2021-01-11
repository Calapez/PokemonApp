package pt.brunoponte.pokemon.models.pokemon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Built from the following format:
 * {
 *      "name": "wartortle",
 *      "url": "https://pokeapi.co/api/v2/pokemon/8/"
 * }
 */

public class PokemonsWrapper {

    @SerializedName("results")
    @Expose
    private List<SimplePokemonModel> pokemons;
    @SerializedName("next")
    @Expose
    private String nextUrl;

    public List<SimplePokemonModel> getPokemons() {
        return pokemons;
    }

    public String getNextUrl() {
        return nextUrl;
    }

}
