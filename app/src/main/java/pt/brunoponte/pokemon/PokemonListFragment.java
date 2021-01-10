package pt.brunoponte.pokemon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pt.brunoponte.pokemon.adapters.PokemonAdapter;
import pt.brunoponte.pokemon.viewmodels.PokemonListViewModel;

public class PokemonListFragment extends Fragment {

    private static final String TAG = PokemonListFragment.class.getSimpleName();

    private static PokemonListFragment singleton;

    private MainActivity mActivity;
    private PokemonListViewModel mPokemonListViewModel;

    /* Pokemon list */
    private RecyclerView listPokemons;
    private LinearLayoutManager mLayoutManager;
    private PokemonAdapter mAdapter;

    public static PokemonListFragment getInstance() {
        if (singleton == null) {
            singleton = new PokemonListFragment();
        }

        return singleton;
    }

    private PokemonListFragment() {}

    /* Scroll Listener for handling pagination with Recycler View */
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!mPokemonListViewModel.getIsLoading().getValue() && !mPokemonListViewModel.getIsLastPage().getValue()) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PokemonListViewModel.PAGE_SIZE) {
                    // Load new page of pokemons
                    mPokemonListViewModel.fetchMorePokemons();
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        mPokemonListViewModel = ViewModelProviders.of(this).get(PokemonListViewModel.class);
        mPokemonListViewModel.init();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = (View) inflater.inflate(R.layout.pokemon_list_fragment, container, false);

        /* Recycler View */
        // Set adapter value
        mAdapter = new PokemonAdapter(mActivity, mPokemonListViewModel.getPokemons().getValue());
        // Set layout manager
        mLayoutManager = new LinearLayoutManager(mActivity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Set recycler view
        listPokemons = (RecyclerView) viewRoot.findViewById(R.id.recyclerPokemon);
        listPokemons.setLayoutManager(mLayoutManager);
        listPokemons.setAdapter(mAdapter);
        listPokemons.addOnScrollListener(recyclerViewOnScrollListener); // Pagination

        mPokemonListViewModel.getPokemons().observe(this, pokemons ->
                //mAdapter.notifyDataSetChanged()//;
                mAdapter.setPokemonsList(pokemons)
        );

        mPokemonListViewModel.fetchMorePokemons();

        return viewRoot;
    }
}
