package pt.brunoponte.pokemon.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

public class PokemonListViewModel extends ViewModel {

    private static final String TAG = PokemonListViewModel.class.getSimpleName();
    public static final int PAGE_SIZE = 7;

    private int mNextOffset;

    private MutableLiveData<List<SimplePokemonModel>> mPokemons = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsLastPage = new MutableLiveData<>();

    public void init() {
        mNextOffset = 0;
        mPokemons.setValue(new ArrayList<>());
        mIsLoading.setValue(false);
        mIsLastPage.setValue(false);
    }

    public void fetchMorePokemons() {
        mIsLoading.setValue(true);

        List<SimplePokemonModel> tempPokemons = mPokemons.getValue();

        ApiService apiService = RetrofitInstance.getInstance().create(ApiService.class);
        Call<PokemonsWrapper> listPokemonsCall = apiService.listPokemons(mNextOffset, PAGE_SIZE);
        listPokemonsCall.enqueue(new Callback<PokemonsWrapper>() {
            @Override
            public void onResponse(Call<PokemonsWrapper> call, Response<PokemonsWrapper> response) {
                Log.d(TAG, "fetchMorePokemons() success");
                Log.d(TAG, response.message());

                PokemonsWrapper wrapper = response.body();

                if (wrapper.getPokemons().size() < PAGE_SIZE)
                    mIsLastPage.setValue(true);

                mNextOffset = getNextOffsetFromUrl(wrapper.getNextUrl());  // Set next offset

                final int[] photosCounter = {0};
                for (SimplePokemonModel pokemon : wrapper.getPokemons()) {
                    tempPokemons.add(pokemon);

                    /* Async request */
                    Log.d(TAG, "showPokemon("+pokemon.getName()+")");
                    ApiService apiService2 = RetrofitInstance.getInstance().create(ApiService.class);
                    Call<PokemonModel> showPokemonCall = apiService2.showPokemon(pokemon.getName());
                    showPokemonCall.enqueue(new Callback<PokemonModel>() {
                        @Override
                        public void onResponse(Call<PokemonModel> call, Response<PokemonModel> response) {
                            photosCounter[0]++;
                            pokemon.setPhotoUrl(response.body().getSprites().getFrontUrl());

                            // Last photo, finish list
                            if (photosCounter[0] == wrapper.getPokemons().size()) {
                                mIsLoading.setValue(false);
                                mPokemons.postValue(tempPokemons);
                            }
                        }

                        @Override
                        public void onFailure(Call<PokemonModel> call, Throwable t) {
                            t.printStackTrace();
                            Log.d(TAG, t.getMessage());
                            Log.d(TAG, "onFailure() showPokemonCall");
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<PokemonsWrapper> call, Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "onFailure() listPokemons");
                Log.d(TAG, t.getMessage());

                List<SimplePokemonModel> pokemons = mPokemons.getValue();
                pokemons.clear();
                mPokemons.postValue(pokemons);
            }
        });
    }

    private int getNextOffsetFromUrl(String url) {
        return Integer.parseInt(
                url.substring(url.indexOf("=") + 1, url.indexOf("&"))
        );
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return mIsLoading;
    }

    public MutableLiveData<Boolean> getIsLastPage() {
        return mIsLastPage;
    }

    public MutableLiveData<List<SimplePokemonModel>> getPokemons() {
        return mPokemons;
    }

}
