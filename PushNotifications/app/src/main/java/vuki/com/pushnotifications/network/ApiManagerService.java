package vuki.com.pushnotifications.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mvukosav on 7.8.2016..
 */
public interface ApiManagerService {


    final String baseUrl = "/api";

    @POST(baseUrl + "/RegisterToken")
    Call<Void> getConfig( @Body String readModel );

}
