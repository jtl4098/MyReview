package taekyungkil.mohawk.capstoneproject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Codemaker on 12/06/2021.
 */

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}