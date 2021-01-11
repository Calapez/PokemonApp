package pt.brunoponte.pokemon.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import pt.brunoponte.pokemon.models.PokemonModel;
import pt.brunoponte.pokemon.models.PokemonsWrapper;
import pt.brunoponte.pokemon.models.SimplePokemonModel;
import pt.brunoponte.pokemon.network.ApiService;
import pt.brunoponte.pokemon.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonRepository {

    private static String TAG = PokemonRepository.class.getSimpleName();
    private static PokemonRepository instance;

    private MutableLiveData<List<SimplePokemonModel>> mSimplePokemons;
    private MutableLiveData<List<PokemonModel>> mPokemons;
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

        mPokemons = new MutableLiveData<>();
        mPokemons.setValue(new ArrayList<>());

        mIsFetching = new MutableLiveData<>();
        mIsFetching.setValue(false);
    }

    public MutableLiveData<List<SimplePokemonModel>> getSimplePokemons() {
        return mSimplePokemons;
    }

    public MutableLiveData<List<PokemonModel>> getPokemons() {
        return mPokemons;
    }

    public MutableLiveData<Boolean> getIsFetching() {
        return mIsFetching;
    }

    public void fetchMorePokemons(int offset, int pageSize) {
        mIsFetching.setValue(true);

        List<SimplePokemonModel> tempPokemons = mSimplePokemons.getValue();

        ApiService apiService = RetrofitInstance.getInstance().create(ApiService.class);
        Call<PokemonsWrapper> listPokemonsCall = apiService.listPokemons(offset, pageSize);
        listPokemonsCall.enqueue(new Callback<PokemonsWrapper>() {
            @Override
            public void onResponse(@NonNull Call<PokemonsWrapper> call,
                                   @NonNull Response<PokemonsWrapper> response)
            {
                Log.d(TAG, "fetchMorePokemons() success");

                PokemonsWrapper wrapper = response.body();

                if (wrapper == null || wrapper.getPokemons() == null) {
                    return;
                }

                final int[] photosCounter = {0};
                for (SimplePokemonModel pokemon : wrapper.getPokemons()) {
                    tempPokemons.add(pokemon);

                    /* Async request */
                    Log.d(TAG, "showPokemon("+pokemon.getName()+")");
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
            public void onFailure(@NonNull Call<PokemonsWrapper> call, @NonNull Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "onFailure() listPokemons");

                List<SimplePokemonModel> noPokemons = mSimplePokemons.getValue();
                noPokemons.clear();
                mSimplePokemons.postValue(noPokemons);
            }
        });
    }

}
