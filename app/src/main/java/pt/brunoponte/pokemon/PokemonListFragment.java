package pt.brunoponte.pokemon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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

    private static PokemonListFragment sInstance;

    private MainActivity mActivity;
    private PokemonListViewModel mPokemonListViewModel;

    /* Progress Bar*/
    private ProgressBar mProgressBar;

    /* Pokemon list */
    private RecyclerView mListPokemons;
    private LinearLayoutManager mLayoutManager;
    private PokemonAdapter mAdapter;

    static PokemonListFragment getInstance() {
        if (sInstance == null) {
            sInstance = new PokemonListFragment();
        }

        return sInstance;
    }

    public PokemonListFragment() {}

    /* Scroll Listener for handling pagination with Recycler View */
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (mPokemonListViewModel.getIsFetching() == null
                    || mPokemonListViewModel.getIsFetching().getValue() == null) {
                return;
            }

            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            // Scrolled to the bottom, fetch more pokemons
            if (!mPokemonListViewModel.getIsFetching().getValue()
                    && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PokemonListViewModel.PAGE_SIZE)
            {
                    // Load new page of pokemons
                    mPokemonListViewModel.fetchMorePokemons();
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

        /* Progress bar */
        mProgressBar = (ProgressBar) viewRoot.findViewById(R.id.progressBar);

        /* Recycler View */
        // Set adapter value
        mAdapter = new PokemonAdapter(mActivity,
                mPokemonListViewModel.getSimplePokemons().getValue());
        // Set layout manager
        mLayoutManager = new LinearLayoutManager(mActivity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Set recycler view
        mListPokemons = (RecyclerView) viewRoot.findViewById(R.id.recyclerPokemon);
        mListPokemons.setLayoutManager(mLayoutManager);
        mListPokemons.setAdapter(mAdapter);
        mListPokemons.addOnScrollListener(recyclerViewOnScrollListener); // Pagination

        setObservers();

        mPokemonListViewModel.fetchMorePokemons();  // Fetch pokemons right away

        return viewRoot;
    }

    private void setObservers() {
        mPokemonListViewModel.getSimplePokemons().observe(this, simplePokemons ->
                mAdapter.notifyDataSetChanged()
        );

        mPokemonListViewModel.getIsFetching().observe(this, isLoading ->
                mProgressBar.setVisibility( isLoading ? View.VISIBLE : View.INVISIBLE )
        );
    }
}
