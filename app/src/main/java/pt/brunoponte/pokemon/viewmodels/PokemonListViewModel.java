package pt.brunoponte.pokemon.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import pt.brunoponte.pokemon.models.PokemonModel;
import pt.brunoponte.pokemon.models.PokemonsWrapper;
import pt.brunoponte.pokemon.network.ApiService;
import pt.brunoponte.pokemon.network.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonListViewModel extends ViewModel {

    private static final String TAG = PokemonListViewModel.class.getSimpleName();
    public static final int PAGE_SIZE = 7;

    private int mNextOffset;

    private MutableLiveData<List<PokemonModel>> mPokemons = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsLastPage = new MutableLiveData<>();

    public void init() {
        mNextOffset = 0;
        mPokemons.setValue(new ArrayList<>());
        mIsLoading.setValue(false);
        mIsLastPage.setValue(false);
    }

    public void fetchMorePokemons() {
        ApiService apiService = RetrofitInstance.getInstance().create(ApiService.class);
        Call<PokemonsWrapper> call = apiService.listPokemons(mNextOffset, PAGE_SIZE);
        call.enqueue(new Callback<PokemonsWrapper>() {
            @Override
            public void onResponse(Call<PokemonsWrapper> call, Response<PokemonsWrapper> response) {
                Log.d(TAG, "fetchMorePokemons() success");
                Log.d(TAG, response.message());

                PokemonsWrapper wrapper = response.body();

                mNextOffset = getNextOffsetFromUrl(wrapper.getNextUrl());  // Set next offset

                List<PokemonModel> pokemons = mPokemons.getValue();
                pokemons.addAll(response.body().getPokemons());
                mPokemons.postValue(pokemons);  // Add new pokemons to list
            }

            @Override
            public void onFailure(Call<PokemonsWrapper> call, Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "fetchMorePokemons() failed");
                Log.d(TAG, t.getMessage());

                List<PokemonModel> pokemons = mPokemons.getValue();
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

    public MutableLiveData<List<PokemonModel>> getPokemons() {
        return mPokemons;
    }

}
