package kordoghli.firas.petcare.Utile.retrofit;

public class ApiUtil {

    private static final String BASE_URL = "http://192.168.1.16:3000/";

    public static RetrofitInterface getServiceClass() {
        return RetrofitAPI.getRetrofit(BASE_URL).create(RetrofitInterface.class);
    }

}
