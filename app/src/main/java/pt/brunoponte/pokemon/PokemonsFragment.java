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

    private static final String TAG = PokemonsFragment.class.getSimpleName();
    private static final int PAGE_SIZE = 10;

    private MainActivity mActivity;

    /* Pagination */
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private String nextEndpoint = String.format(Api.LIST_POKEMONS_LIMIT_ENDPOINT, PAGE_SIZE);

    /* Pokemon list */
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private PokemonAdapter mAdapter;
    private List<Pokemon> mPokemonsList = new ArrayList<>(PAGE_SIZE);

    /* Scroll Listener for handling pagination with Recycler View */
    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visibleItemCount = mLayoutManager.getChildCount();
            int totalItemCount = mLayoutManager.getItemCount();
            int firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition();

            if (!isLoading && !isLastPage) {
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    // Load new page of pokemons
                    loadMorePokemons();
                }
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = (View) inflater.inflate(R.layout.pokemons_fragment, container, false);

        // Recycler View and Adapter
        mLayoutManager = new LinearLayoutManager(mActivity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.pokemonsRecyclerView);
        mAdapter = new PokemonAdapter(mActivity, mPokemonsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);// Pagination
        mRecyclerView.addOnScrollListener(recyclerViewOnScrollListener);

        return viewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadMorePokemons();
    }

    private void loadMorePokemons() {
        isLoading = true;
        TaskLoadPokemons task = new TaskLoadPokemons();
        task.execute();
    }

    class TaskLoadPokemons extends AsyncTask<Void, Void, Void> {

        private int resultCode = -1;
        private String resultBody = null;

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.setConnectTimeout(3000, TimeUnit.MILLISECONDS);
            httpClient.setReadTimeout(3000, TimeUnit.MILLISECONDS);
            httpClient.setWriteTimeout(3000, TimeUnit.MILLISECONDS);

            Request request = new Request.Builder()
                    .url(nextEndpoint)
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
                    nextEndpoint = root.getString("next");
                    JSONArray jsonPokemons = root.getJSONArray("results");

                    if (jsonPokemons.length() < PAGE_SIZE)
                        isLastPage = true;

                    // Loop through all pokemons
                    for (int i = 0; i < jsonPokemons.length(); i++) {
                        JSONObject jsonResult = jsonPokemons.getJSONObject(i);

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

            isLoading = false;

            if (!message.isEmpty())
                Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
        }
    }


}
