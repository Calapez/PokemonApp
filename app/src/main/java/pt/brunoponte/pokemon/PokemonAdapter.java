package pt.brunoponte.pokemon;

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

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {

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
        viewHolder.getTextView().setText(pokemon.getName());
        Picasso.get()
                .load(pokemon.getPhotoUrl())
                .resize(300, 300)
                .into((viewHolder.getImgView()));
    }

    @Override
    public int getItemCount() {
        return pokemonsList.size();
    }
}
