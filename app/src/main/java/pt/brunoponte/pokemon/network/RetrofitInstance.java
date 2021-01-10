package pt.brunoponte.pokemon.network;

import pt.brunoponte.pokemon.util.Endpoints;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {

    private static Retrofit.Builder retrofitBuilder
            = new Retrofit.Builder()
            .baseUrl(Endpoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit;

    public Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = retrofitBuilder.build();
        }
        return retrofit;
    }

}
