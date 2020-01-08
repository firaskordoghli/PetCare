package kordoghli.firas.petcare.Utile.retrofit;

public class ApiUtil {

    private static final String BASE_URL = "http://192.168.1.6:3000/";
    private static final String Photo_URL = "http://192.168.1.6:3000/uploads/";

    public static String photoUrl (){
        return Photo_URL;
    }

    public static RetrofitInterface getServiceClass() {
        return RetrofitAPI.getRetrofit(BASE_URL).create(RetrofitInterface.class);
    }

}
