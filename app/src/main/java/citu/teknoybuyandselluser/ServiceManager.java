package citu.teknoybuyandselluser;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import citu.teknoybuyandselluser.services.TBSUserInterface;
import io.realm.RealmObject;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 ** Created by jack on 1/02/16.
 */
public class ServiceManager {
    private static TBSUserInterface SOLE_INSTANCE;

    public static TBSUserInterface getInstance () {
        if (SOLE_INSTANCE == null) {
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setExclusionStrategies(new ExclusionStrategy() {
                        @Override
                        public boolean shouldSkipField(FieldAttributes f) {
                            return f.getDeclaringClass().equals(RealmObject.class);
                        }

                        @Override
                        public boolean shouldSkipClass(Class<?> clazz) {
                            return false;
                        }
                    })
                    .create();

            SOLE_INSTANCE = new Retrofit.Builder()
                    .baseUrl(Constants.UrlUser.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
                    .create(TBSUserInterface.class);
        }

        return SOLE_INSTANCE;
    }
}
