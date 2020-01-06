package kordoghli.firas.petcare.Utile.retrofit;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.List;

import kordoghli.firas.petcare.Data.Adoption;
import kordoghli.firas.petcare.Data.Lost;
import kordoghli.firas.petcare.Data.Pet;
import kordoghli.firas.petcare.Data.Post;
import kordoghli.firas.petcare.Data.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {

    @POST("signup")
    public Call<JsonObject> signUp(@Body JsonObject object);

    @POST("login")
    public Call<JsonObject> login(@Body JsonObject object);

    @POST("getUserById/")
    public Call<User> getUserById(@Body JsonObject object);

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

    @Multipart
    @POST("/upload")
    Call<JsonPrimitive> postImage(@Part MultipartBody.Part image, @Part("upload") RequestBody name);

    @POST("addPost")
    public Call<JsonObject> addPost(@Body JsonObject object);

    @POST("getAllPosts/")
    public Call<List<Post>> getAllPosts();

    @POST("getAllMyPosts/")
    public Call<List<Post>> getAllMyPosts(@Body JsonObject object);

    @POST("getAllMylostsAndFound/")
    public Call<List<Lost>> getAllMylostAndFound(@Body JsonObject object);

    @POST("getPostById/")
    public Call<Post> getPostById(@Body JsonObject object);

    @POST("deleteMyPostById/")
    public Call<JsonObject> deleteMyPostById(@Body JsonObject object);

    @POST("addLost")
    public Call<JsonObject> addLost(@Body JsonObject object);

    @POST("getAllLost/")
    public Call<List<Lost>> getAllLostAndFound();

    @POST("getLost/")
    public Call<List<Lost>> getAllLost();

    @POST("getFound/")
    public Call<List<Lost>> getAllFound();

    @POST("getLostById/")
    public Call<Lost> getLostById(@Body JsonObject object);

    @POST("deleteLostById/")
    public Call<JsonObject> deleteLostById(@Body JsonObject object);
}
