package pt.brunoponte.pokemon;

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

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {

    private static final String TAG = "PokemonAdapter";

    private Context context;
    private List<Pokemon> pokemonsList;

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

    public PokemonAdapter(Context context, List<Pokemon> pokemonsList) {
        this.context = context;
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
        Pokemon pokemon = pokemonsList.get(position);

        viewHolder.getTextView().setText(pokemon.getName());  // Set name
        //viewHolder.getImgView().setImageDrawable(null);

        if (pokemon.getPhotoUrl().isEmpty()) {
            // Fetch and add proper photo to image view
            new TaskGetPhoto(context, viewHolder.getImgView(), pokemon)  // Set photo
                    .execute();
        } else {
            // Add photo to image view
            addPhotoToImageView(pokemon.getPhotoUrl(), viewHolder.getImgView());
        }
    }

    @Override
    public int getItemCount() {
        return pokemonsList.size();
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
        private Pokemon pokemon;
        private int resultCode;
        private String resultBody;

        public TaskGetPhoto(Context context, ImageView imgView, Pokemon pokemon) {
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
                    .url(pokemon.getEndpoint())
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
