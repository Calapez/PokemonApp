package pt.brunoponte.pokemon.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import pt.brunoponte.pokemon.MainActivity;
import pt.brunoponte.pokemon.R;
import pt.brunoponte.pokemon.models.SimplePokemonModel;
import pt.brunoponte.pokemon.util.GeneralMethods;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {

    private static final String TAG = PokemonAdapter.class.getSimpleName();

    private MainActivity mActivity;
    private List<SimplePokemonModel> pokemonsList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textName);
            imgView = itemView.findViewById(R.id.imgPhoto);
        }

        public TextView getTextView() {
            return textView;
        }
        public ImageView getImgView() {
            return imgView;
        }
    }

    public PokemonAdapter(Context mActivity, List<SimplePokemonModel> pokemonsList) {
        this.mActivity = (MainActivity) mActivity;
        this.pokemonsList = pokemonsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pokemon_row_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        SimplePokemonModel pokemon = pokemonsList.get(position);

        // Set Pokemon name in TextView
        viewHolder.getTextView().setText(
                GeneralMethods.capitalizeFirstLetter(pokemon.getName())
        );

        Log.d(TAG, pokemon.getPhotoUrl());
        if (pokemon.getPhotoUrl() == null || pokemon.getPhotoUrl().isEmpty()) {
            viewHolder.getImgView().setImageDrawable(mActivity.getDrawable(R.drawable.ic_loading));
        } else {
            Log.d(TAG, pokemon.getPhotoUrl());
            addPhotoToImageView(pokemon.getPhotoUrl(), viewHolder.getImgView());
        }

        /* TODO: Delete Old
        // Set Pokemon photo in ImageView
        if (pokemon.getPhotoUrl().isEmpty()) {
            // Set photo as "loading" for now
            viewHolder.getImgView().setImageDrawable(mActivity.getDrawable(R.drawable.ic_loading));

            // Fetch and set photo
            new TaskGetPhoto(mActivity, viewHolder.getImgView(), pokemon)
                    .execute();
        } else {
            // Set photo
            addPhotoToImageView(pokemon.getPhotoUrl(), viewHolder.getImgView());
        }

         */

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.openFragPokemonDetails(pokemonsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (pokemonsList == null)
            return 0;

        return pokemonsList.size();
    }

    public void setPokemonsList(List<SimplePokemonModel> pokemonsList) {
        this.pokemonsList = pokemonsList;
        notifyDataSetChanged();
    }

    private void addPhotoToImageView(String url, ImageView imgView) {
        Picasso.get()
            .load(url)
            .resize(300, 300)
            .into(imgView);
    }

    class TaskGetPhoto extends AsyncTask<Void, Void, Void> {

        private Context context;
        private ImageView imgView;
        private SimplePokemonModel pokemon;
        private int resultCode;
        private String resultBody;

        public TaskGetPhoto(Context context, ImageView imgView, SimplePokemonModel pokemon) {
            this.context = context;
            this.imgView = imgView;
            this.pokemon = pokemon;
            resultCode = -1;
            resultBody = null;
        }

        @Override
        protected Void doInBackground(Void... params) {
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.setConnectTimeout(3000, TimeUnit.MILLISECONDS);
            httpClient.setReadTimeout(3000, TimeUnit.MILLISECONDS);
            httpClient.setWriteTimeout(3000, TimeUnit.MILLISECONDS);

            Request request = new Request.Builder()
                    .url(pokemon.getUrl())
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
                    // Update Theme tags adapter
                    JSONObject root = new JSONObject(resultBody);

                    // Add photo to Pokemon object
                    pokemon.setPhotoUrl(
                            root.getJSONObject("sprites").getString("front_default")
                    );

                    addPhotoToImageView(pokemon.getPhotoUrl(), imgView);
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
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
