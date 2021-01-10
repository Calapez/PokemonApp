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

import pt.brunoponte.pokemon.models.PokemonModel;
import pt.brunoponte.pokemon.models.SimplePokemonModel;
import pt.brunoponte.pokemon.util.GeneralMethods;

public class PokemonDetailsFragment extends Fragment {

    private static final String TAG = PokemonDetailsFragment.class.getSimpleName();

    private static PokemonDetailsFragment singleton;

    private MainActivity mActivity;

    private ImageView imgPhoto;
    private TextView textName;

    private SimplePokemonModel mPokemon;

    /* Description list */
    private ListView listDescriptions;
    private ArrayAdapter<String> mAdapter;
    private List<String> mDescriptionDataset = new ArrayList<>();

    public static PokemonDetailsFragment getInstance() {
        if (singleton == null) {
            singleton = new PokemonDetailsFragment();
        }

        return singleton;
    }

    private PokemonDetailsFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPokemon = bundle.getParcelable("pokemonObject");
        }

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

        imgPhoto = (ImageView) viewRoot.findViewById(R.id.imgPhoto);
        textName = (TextView) viewRoot.findViewById(R.id.textName);

        // List View and Adapter
        mAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_list_item_1, mDescriptionDataset);
        listDescriptions = (ListView) viewRoot.findViewById(R.id.listDescriptions);
        listDescriptions.setAdapter(mAdapter);

        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textName.setText(GeneralMethods.capitalizeFirstLetter(mPokemon.getName()));
        Picasso.get()
                .load(mPokemon.getPhotoUrl())
                .resize(300, 300)
                .into(imgPhoto);

        loadPokemon();
    }

    private void loadPokemon() {
        TaskLoadPokemon task = new TaskLoadPokemon();
        task.execute();
    }

    class TaskLoadPokemon extends AsyncTask<Void, Void, Void> {

        private int resultCode = -1;
        private String resultBody = null;

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.setConnectTimeout(3000, TimeUnit.MILLISECONDS);
            httpClient.setReadTimeout(3000, TimeUnit.MILLISECONDS);
            httpClient.setWriteTimeout(3000, TimeUnit.MILLISECONDS);

            Request request = new Request.Builder()
                    .url(mPokemon.getUrl())
                    .addHeader("Content-type", "application/json")
                    .build();

            try {
                Response response = httpClient.newCall(request).execute();
                resultCode = response.code();
                resultBody = response.body().string();
            } catch (IOException e) {
                resultCode = -1;
                resultBody = null;
                e.printStackTrace();
            }

            Log.d(TAG, String.format("Response - code: %d, body: %s", resultCode, resultBody));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String message = "";
            if (resultCode == 200) { // Request succeeded
                try {
                    JSONObject root = new JSONObject(resultBody);

                    // Loop through all held stats
                    JSONArray jsonStats = root.getJSONArray("stats");
                    for (int i = 0; i < jsonStats.length(); i++) {
                        String statName = jsonStats.getJSONObject(i)
                                .getJSONObject("stat").getString("name");
                        int statValue = jsonStats.getJSONObject(i)
                                .getInt("base_stat");

                        mDescriptionDataset.add(
                                String.format(Locale.US, "%s = %d",
                                        statName, statValue)
                        );
                    }

                    // Loop through all abilities
                    JSONArray jsonAbilities = root.getJSONArray("abilities");
                    for (int i = 0; i < jsonAbilities.length(); i++) {
                        String abilityName = jsonAbilities.getJSONObject(i)
                                .getJSONObject("ability").getString("name");
                        mDescriptionDataset.add(
                                String.format(Locale.US, "Ability #%d - %s",
                                        i+1, abilityName)
                        );
                    }

                    // Loop through all forms
                    JSONArray jsonForms = root.getJSONArray("forms");
                    for (int i = 0; i < jsonForms.length(); i++) {
                        String formName = jsonForms.getJSONObject(i).getString("name");
                        mDescriptionDataset.add(
                                String.format(Locale.US, "Form #%d - %s",
                                        i+1, formName)
                        );
                    }

                    // Loop through all held items
                    JSONArray jsonHeldItems = root.getJSONArray("held_items");
                    for (int i = 0; i < jsonHeldItems.length(); i++) {
                        String itemName = jsonHeldItems.getJSONObject(i)
                                .getJSONObject("item").getString("name");
                        mDescriptionDataset.add(
                                String.format(Locale.US, "Item #%d - %s",
                                        i+1, itemName)
                        );
                    }

                    // Loop through all moves
                    JSONArray jsonMoves = root.getJSONArray("moves");
                    for (int i = 0; i < jsonMoves.length(); i++) {
                        String moveName = jsonMoves.getJSONObject(i)
                                .getJSONObject("move").getString("name");
                        mDescriptionDataset.add(
                                String.format(Locale.US, "Move #%d - %s",
                                        i+1, moveName)
                        );
                    }

                    mAdapter.notifyDataSetChanged();  // Refresh adapter

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Failed to parse JSON");
                }
            } else {
                if (resultCode == 400) {
                    message = "Error: Bad request";
                } else if (resultCode == 500) {
                    message = "Server Error";
                } else {
                    message = "Unexpected Error";
                }
            }

            if (!message.isEmpty())
                Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
        }
    }


}
