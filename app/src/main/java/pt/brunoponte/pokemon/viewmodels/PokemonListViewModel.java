package pt.brunoponte.pokemon.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import pt.brunoponte.pokemon.models.pokemon.SimplePokemonModel;
import pt.brunoponte.pokemon.repositories.PokemonRepository;

public class PokemonListViewModel extends ViewModel {

    private static final String TAG = PokemonListViewModel.class.getSimpleName();
    public static final int PAGE_SIZE = 7;

    private PokemonRepository mRepository;
    private MutableLiveData<List<SimplePokemonModel>> mSimplePokemons;
    private MutableLiveData<Boolean> mIsFetching;

    public void init() {
        mRepository = PokemonRepository.getInstance();
        mRepository.init();
        mSimplePokemons = mRepository.getSimplePokemons();
        mIsFetching = mRepository.getIsFetching();
    }

    public void fetchMorePokemons() {
        if (mRepository.getSimplePokemons().getValue() == null) {
            return;
        }

        // Offset is simply the size of the pokemons
        int offset = mRepository.getSimplePokemons().getValue().size();

        mRepository.fetchMorePokemons(offset, PAGE_SIZE);
    }

    public LiveData<Boolean> getIsFetching() {
        return mIsFetching;
    }

    public LiveData<List<SimplePokemonModel>> getSimplePokemons() {
        return mSimplePokemons;
    }

}
