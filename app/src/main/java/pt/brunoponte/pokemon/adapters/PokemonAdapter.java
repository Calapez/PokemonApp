package pt.brunoponte.pokemon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pt.brunoponte.pokemon.MainActivity;
import pt.brunoponte.pokemon.R;
import pt.brunoponte.pokemon.models.pokemon.SimplePokemonModel;
import pt.brunoponte.pokemon.util.GeneralMethods;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {

    private static final String TAG = PokemonAdapter.class.getSimpleName();

    private MainActivity mActivity;
    private List<SimplePokemonModel> pokemonsList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imgView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textName);
            imgView = itemView.findViewById(R.id.imgPhoto);
        }

        TextView getTextView() {
            return textView;
        }
        ImageView getImgView() {
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

        // Add pokemon photo to view in list
        if (pokemon.getPhotoUrl() != null && !pokemon.getPhotoUrl().isEmpty()) {
            addPhotoToImageView(pokemon.getPhotoUrl(), viewHolder.getImgView());
        }

        viewHolder.itemView.setOnClickListener(v ->
                mActivity.openFragPokemonDetails(pokemonsList.get(position))
        );
    }

    @Override
    public int getItemCount() {
        if (pokemonsList == null)
            return 0;

        return pokemonsList.size();
    }

    private void addPhotoToImageView(String url, ImageView imgView) {
        Picasso.get()
            .load(url)
            .resize(300, 300)
            .into(imgView);
    }
}
