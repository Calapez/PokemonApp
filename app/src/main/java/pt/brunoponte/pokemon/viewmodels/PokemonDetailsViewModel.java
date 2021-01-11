package pt.brunoponte.pokemon.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pt.brunoponte.pokemon.models.pokemon.PokemonModel;
import pt.brunoponte.pokemon.models.pokemon.SimplePokemonModel;
import pt.brunoponte.pokemon.network.ApiService;
import pt.brunoponte.pokemon.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonDetailsViewModel extends ViewModel {

    private static final String TAG = PokemonDetailsViewModel.class.getSimpleName();

    private MutableLiveData<PokemonModel> mPokemon = new MutableLiveData<>();

    public void init(SimplePokemonModel simplePokemon) {
        fetchPokemonDetails(simplePokemon.getName());
    }

    // FIXME: This is just temporarily in ModelView
    private void fetchPokemonDetails(String name) {
        ApiService apiService = RetrofitInstance.getInstance().create(ApiService.class);
        Call<PokemonModel> showPokemonsCall = apiService.showPokemon(name);
        showPokemonsCall.enqueue(new Callback<PokemonModel>() {
            @Override
            public void onResponse(Call<PokemonModel> call, Response<PokemonModel> response) {
                Log.d(TAG, "fetchPokemon() success");
                Log.d(TAG, response.message());

                mPokemon.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<PokemonModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "onFailure() listPokemons");

                mPokemon.setValue(null);
            }
        });
    }

    public LiveData<PokemonModel> getPokemon() {
        return mPokemon;
    }

}
