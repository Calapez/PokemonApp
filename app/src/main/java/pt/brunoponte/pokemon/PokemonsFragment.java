package pt.brunoponte.pokemon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PokemonsFragment extends Fragment {

    private static final String TAG = "PokemonsFragment";
    private static final int POKEMONS_SIZE = 20;

    private MainActivity mActivity;

    private RecyclerView mRecyclerView;
    private PokemonAdapter mAdapter;
    private List<Pokemon> mPokemonsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainActivity) getActivity();
        mPokemonsList = new ArrayList<>(POKEMONS_SIZE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = (View) inflater.inflate(R.layout.pokemons_fragment, container, false);

        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        // Setup and fill recycler view and adapter
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.pokemonsRecyclerView);
        mAdapter = new PokemonAdapter(mActivity, mPokemonsList);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mAdapter);

        loadPokemons();

        return viewRoot;
    }

    private void loadPokemons() {
        TaskListPokemons task = new TaskListPokemons();
        task.execute();
    }

    class TaskListPokemons extends AsyncTask<Void, Void, Void> {

        private int resultCode = -1;
        private String resultBody = null;

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.setConnectTimeout(3000, TimeUnit.MILLISECONDS);
            httpClient.setReadTimeout(3000, TimeUnit.MILLISECONDS);
            httpClient.setWriteTimeout(3000, TimeUnit.MILLISECONDS);

            Request request = new Request.Builder()
                    .url(Api.LIST_POKEMONS_ENDPOINT)
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

                mPokemonsList.clear();  // Clear pokemon list

                try {
                    JSONObject root = new JSONObject(resultBody);
                    JSONArray jsonTags = root.getJSONArray("results");

                    // Loop through all pokemons
                    for (int i = 0; i < jsonTags.length(); i++) {
                        JSONObject jsonResult = jsonTags.getJSONObject(i);

                        // Add pokemon to list
                        mPokemonsList.add(
                                new Pokemon(
                                        jsonResult.getString("name"),
                                        jsonResult.getString("url")
                                )
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
