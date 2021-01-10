package pt.brunoponte.pokemon.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import pt.brunoponte.pokemon.models.PokemonModel;
import pt.brunoponte.pokemon.models.SimplePokemonModel;
import pt.brunoponte.pokemon.network.ApiService;
import pt.brunoponte.pokemon.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonDetailsViewModel extends ViewModel {

    private static final String TAG = PokemonDetailsViewModel.class.getSimpleName();

    private MutableLiveData<PokemonModel> mPokemon = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();

    public void init(SimplePokemonModel simplePokemon) {
        mIsLoading.setValue(false);
        fetchPokemon(simplePokemon.getName());
    }

    public void fetchPokemon(String name) {
        mIsLoading.setValue(true);

        ApiService apiService = RetrofitInstance.getInstance().create(ApiService.class);
        Call<PokemonModel> showPokemonsCall = apiService.showPokemon(name);
        showPokemonsCall.enqueue(new Callback<PokemonModel>() {
            @Override
            public void onResponse(Call<PokemonModel> call, Response<PokemonModel> response) {
                Log.d(TAG, "fetchPokemon() success");
                Log.d(TAG, response.message());

                mPokemon.setValue(response.body());
                mIsLoading.setValue(false);

            }

            @Override
            public void onFailure(Call<PokemonModel> call, Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "onFailure() listPokemons");
                Log.d(TAG, t.getMessage());

                mPokemon.setValue(null);
                mIsLoading.setValue(false);
            }
        });
    }

    public MutableLiveData<PokemonModel> getPokemon() {
        return mPokemon;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }



}
