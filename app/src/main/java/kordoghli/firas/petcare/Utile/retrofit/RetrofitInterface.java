package kordoghli.firas.petcare.Utile.retrofit;

import com.google.gson.JsonObject;

import java.util.List;

import kordoghli.firas.petcare.Data.Pet;
import kordoghli.firas.petcare.Data.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("signup")
    public Call<JsonObject> signUp(@Body JsonObject object);

    @POST("login")
    public Call<JsonObject> login(@Body JsonObject object);

    @POST("addPet")
    public Call<JsonObject> addPet(@Body JsonObject object);

    @POST("getAllMyPets/")
    public Call<List<Pet>> getAllMyPets(@Body JsonObject object);

    @POST("getMyPetById/")
    public Call<List<Pet>> getMyPetById(@Body JsonObject object);
}
