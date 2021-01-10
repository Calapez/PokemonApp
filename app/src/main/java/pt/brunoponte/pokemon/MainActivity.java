package pt.brunoponte.pokemon;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            openFragPokemonList();
        }
    }

    public void openFragPokemonList() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, PokemonListFragment.getInstance());
        transaction.commit();
    }

    public void openFragPokemonDetails(Pokemon pokemon) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("pokemonObject", pokemon);

        PokemonDetailsFragment fragment = PokemonDetailsFragment.getInstance();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

}
