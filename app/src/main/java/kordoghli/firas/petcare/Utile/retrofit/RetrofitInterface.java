package kordoghli.firas.petcare.Utile.retrofit;

import com.google.gson.JsonObject;

import kordoghli.firas.petcare.Data.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("signup")
    public Call<JsonObject> signUp(@Body JsonObject object);

    @POST("login")
    public Call<JsonObject> login(@Body JsonObject object);
}
