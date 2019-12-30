package kordoghli.firas.petcare.Utile.retrofit;

import com.google.gson.JsonObject;

import java.util.List;

import kordoghli.firas.petcare.Data.Adoption;
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

    @POST("addAdoption")
    public Call<JsonObject> addAdoptions(@Body JsonObject object);

    @POST("getAllAdoptions/")
    public Call<List<Adoption>> getAllAdoptions();

    @POST("getAllMyAdoptions/")
    public Call<List<Adoption>> getAllMyAdoptions(@Body JsonObject object);

    @POST("getAdoptionById/")
    public Call<Adoption> getAdoptionById(@Body JsonObject object);

    @POST("deleteMyPetById/")
    public Call<JsonObject> deleteMyPetById(@Body JsonObject object);

    @POST("deleteMyAdoptionById/")
    public Call<JsonObject> deleteMyAdoptionById(@Body JsonObject object);
}
