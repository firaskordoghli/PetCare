package kordoghli.firas.petcare.Ui.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Ui.MainActivity;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private EditText emailEt, passwordEt;
    private Button loginBtn, toSignUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // User Session Manager
        sessionManager = new SessionManager(getApplicationContext());

        emailEt = findViewById(R.id.etEmailLogin);
        passwordEt = findViewById(R.id.etPasswordLogin);
        loginBtn = findViewById(R.id.btnLogin);
        toSignUpBtn = findViewById(R.id.btnToSignUp);

        toSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    public void login() {
        JsonObject object = new JsonObject();

        object.addProperty("username", emailEt.getText().toString().trim());
        object.addProperty("password", passwordEt.getText().toString().trim());

        ApiUtil.getServiceClass().login(object).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Gson gson = new Gson();
                User responseUser = response.body();
                sessionManager.createUserLoginSession(gson.toJson(responseUser));
                System.out.println(sessionManager.getUserDetails());

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(LoginActivity.this, "please connect to the internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
