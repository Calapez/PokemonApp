package pt.brunoponte.pokemon.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import pt.brunoponte.pokemon.models.pokemon.PokemonModel;
import pt.brunoponte.pokemon.models.pokemon.SimplePokemonsWrapper;
import pt.brunoponte.pokemon.models.pokemon.SimplePokemonModel;
import pt.brunoponte.pokemon.network.ApiService;
import pt.brunoponte.pokemon.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonRepository {

    private static String TAG = PokemonRepository.class.getSimpleName();
    private static PokemonRepository instance;

    private MutableLiveData<List<SimplePokemonModel>> mSimplePokemons;
    private MutableLiveData<Boolean> mIsFetching;

    public static PokemonRepository getInstance() {
        if (instance == null) {
            instance = new PokemonRepository();
        }
        return instance;
    }

    public void init() {
        mSimplePokemons = new MutableLiveData<>();
        mSimplePokemons.setValue(new ArrayList<>());

        mIsFetching = new MutableLiveData<>();
        mIsFetching.setValue(false);
    }

    public MutableLiveData<List<SimplePokemonModel>> getSimplePokemons() {
        return mSimplePokemons;
    }

    public MutableLiveData<Boolean> getIsFetching() {
        return mIsFetching;
    }

    // Fetch pokemons from a given offset and with a given pageSize
    public void fetchMorePokemons(int offset, int pageSize) {
        mIsFetching.setValue(true);

        List<SimplePokemonModel> tempPokemons = mSimplePokemons.getValue();

        // Fetch pokemons
        ApiService apiService = RetrofitInstance.getInstance().create(ApiService.class);
        Call<SimplePokemonsWrapper> listPokemonsCall = apiService.listPokemons(offset, pageSize);
        listPokemonsCall.enqueue(new Callback<SimplePokemonsWrapper>() {
            @Override
            public void onResponse(@NonNull Call<SimplePokemonsWrapper> call,
                                   @NonNull Response<SimplePokemonsWrapper> response)
            {
                Log.d(TAG, "fetchMorePokemons() success");

                SimplePokemonsWrapper wrapper = response.body();

                if (wrapper == null || wrapper.getPokemons() == null) {
                    return;
                }

                final int[] photosCounter = {0};
                for (SimplePokemonModel pokemon : wrapper.getPokemons()) {
                    tempPokemons.add(pokemon);

                    // Must fetch pokemon details to get its photo url
                    ApiService apiService2 = RetrofitInstance.getInstance().create(ApiService.class);
                    Call<PokemonModel> showPokemonCall = apiService2.showPokemon(pokemon.getName());
                    showPokemonCall.enqueue(new Callback<PokemonModel>() {
                        @Override
                        public void onResponse(@NonNull Call<PokemonModel> call,
                                               @NonNull Response<PokemonModel> response)
                        {
                            photosCounter[0]++;
                            pokemon.setPhotoUrl(response.body().getSprites().getFrontUrl());

                            // Last photo, finish list
                            if (photosCounter[0] == wrapper.getPokemons().size()) {
                                mIsFetching.setValue(false);
                                mSimplePokemons.postValue(tempPokemons);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<PokemonModel> call,
                                              @NonNull Throwable t)
                        {
                            t.printStackTrace();
                            Log.d(TAG, "onFailure() showPokemonCall");
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<SimplePokemonsWrapper> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "onFailure() listPokemons");

                List<SimplePokemonModel> noPokemons = mSimplePokemons.getValue();
                noPokemons.clear();
                mSimplePokemons.postValue(noPokemons);
            }
        });
    }

}
