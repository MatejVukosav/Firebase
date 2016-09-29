package vuki.com.pushnotifications.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vuki.com.pushnotifications.BuildConfig;

/**
 * Created by mvukosav on 7.8.2016..
 */
public class ApiManager implements ApiManagerInterface {

    private static final String DEVELOPING_API_URL = BuildConfig.DEVELOPING_API_URL;

    private static ApiManager apiManagerInstance;
    private ApiManagerService apiManagerService;

    private ApiManager() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor( new LoggingInterceptor2( LoggingInterceptor2.LogLevel.FULL ) )
                .build();

//        if (BuildConfig.DEBUG) {
//            client.interceptors()
//
//        }

       Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory( GsonConverterFactory.create( gson ) )
                .baseUrl( DEVELOPING_API_URL )
                .client( client )
                .build();

        apiManagerService = retrofit.create( ApiManagerService.class );
    }

    public synchronized static ApiManager getInstance() {
        if( apiManagerInstance == null ) {
            apiManagerInstance = new ApiManager();
        }
        return apiManagerInstance;
    }

    private static Gson gson = new GsonBuilder()
            .create();

    @Override
    public ApiManagerService getService() {
        return apiManagerService;
    }
}
