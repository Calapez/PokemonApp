package pt.brunoponte.pokemon.network;

import java.util.List;

import pt.brunoponte.pokemon.models.PokemonModel;
import pt.brunoponte.pokemon.models.PokemonsWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("pokemon")
    Call<PokemonsWrapper> listPokemons(@Query("offset") int offset, @Query("limit") int pageSize);

    @GET("pokemon/{name}")
    Call<List<PokemonModel>> showPokemon(@Path("name") String name);

}

