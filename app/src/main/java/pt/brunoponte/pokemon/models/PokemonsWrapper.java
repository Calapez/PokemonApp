package pt.brunoponte.pokemon.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PokemonsWrapper {

    @SerializedName("results")
    @Expose
    private List<PokemonModel> pokemons;
    @SerializedName("next")
    @Expose
    private String nextUrl;

    public List<PokemonModel> getPokemons() {
        return pokemons;
    }

    public String getNextUrl() {
        return nextUrl;
    }

}
