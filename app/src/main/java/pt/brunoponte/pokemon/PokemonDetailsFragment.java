package pt.brunoponte.pokemon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pt.brunoponte.pokemon.models.AbilityModel;
import pt.brunoponte.pokemon.models.AbilityWrapper;
import pt.brunoponte.pokemon.models.MoveModel;
import pt.brunoponte.pokemon.models.PokemonModel;
import pt.brunoponte.pokemon.models.SimplePokemonModel;
import pt.brunoponte.pokemon.util.GeneralMethods;
import pt.brunoponte.pokemon.viewmodels.PokemonDetailsViewModel;
import pt.brunoponte.pokemon.viewmodels.PokemonListViewModel;

public class PokemonDetailsFragment extends Fragment {

    private static final String TAG = PokemonDetailsFragment.class.getSimpleName();

    private static PokemonDetailsFragment instance;
    private PokemonDetailsViewModel mPokemonDetailsViewModel;

    private MainActivity mActivity;

    private ImageView imgFront;
    private ImageView imgBack;
    private TextView textName;

    /* Description list */
    private ListView listDescriptions;
    private ArrayAdapter<String> mAdapter;

    public static PokemonDetailsFragment getInstance() {
        if (instance == null) {
            instance = new PokemonDetailsFragment();
        }

        return instance;
    }

    private PokemonDetailsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        mPokemonDetailsViewModel = ViewModelProviders.of(this).get(PokemonDetailsViewModel.class);

        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }

        mPokemonDetailsViewModel.init(bundle.getParcelable("pokemonObject"));

        // Handle back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                mActivity.openFragPokemonList();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = (View) inflater.inflate(R.layout.pokemon_details_fragment, container, false);

        imgFront = (ImageView) viewRoot.findViewById(R.id.imgPhotoFront);
        imgBack = (ImageView) viewRoot.findViewById(R.id.imgPhotoBack);
        textName = (TextView) viewRoot.findViewById(R.id.textName);
        listDescriptions = (ListView) viewRoot.findViewById(R.id.listDescriptions);

        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPokemonDetailsViewModel.getPokemon().observe(this, pokemon -> {
            textName.setText(GeneralMethods.capitalizeFirstLetter(pokemon.getName()));

            Picasso.get()
                    .load(pokemon.getSprites().getFrontUrl())
                    .resize(300, 300)
                    .into(imgFront);

            Picasso.get()
                    .load(pokemon.getSprites().getBackUrl())
                    .resize(300, 300)
                    .into(imgBack);

            mAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1,
                    getDescriptionsFromPokemon(pokemon));
            listDescriptions.setAdapter(mAdapter);
        });
    }

    private List<String> getDescriptionsFromPokemon(PokemonModel pokemon) {
        List<String> descriptions = new ArrayList<>();

        descriptions.add("Weight = " + pokemon.getWeight());

        for(int i = 0; i < pokemon.getAbilitiesWrapper().size(); i++) {
            AbilityModel ability = pokemon.getAbilitiesWrapper().get(i).getAbility();
            descriptions.add("Ability #" + i + " - " + ability.getName());
        }

        for(int i = 0; i < pokemon.getAbilitiesWrapper().size(); i++) {
            AbilityModel ability = pokemon.getAbilitiesWrapper().get(i).getAbility();
            descriptions.add(
                    String.format(Locale.US, "Ability #%d - %s",
                            i+1, ability.getName())
            );
        }

        for(int i = 0; i < pokemon.getMovesWrapper().size(); i++) {
            MoveModel move = pokemon.getMovesWrapper().get(i).getMove();
            descriptions.add(
                    String.format(Locale.US, "Move #%d - %s",
                            i+1, move.getName())
            );
        }

        return descriptions;
    }
}
