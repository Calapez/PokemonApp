package pt.brunoponte.pokemon.network;

import pt.brunoponte.pokemon.models.pokemon.PokemonModel;
import pt.brunoponte.pokemon.models.pokemon.SimplePokemonsWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("pokemon")
    Call<SimplePokemonsWrapper> listPokemons(@Query("offset") int offset, @Query("limit") int pageSize);

    @GET("pokemon/{name}")
    Call<PokemonModel> showPokemon(@Path("name") String name);

}

