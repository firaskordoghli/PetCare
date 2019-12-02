package kordoghli.firas.petcare;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private EditText emailEt, usernameEt, passwordEt, passwordCEt;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEt = findViewById(R.id.etEmailSignUp);
        usernameEt = findViewById(R.id.etUsernameSignUp);
        passwordEt = findViewById(R.id.etPasswordSignUp);
        passwordCEt = findViewById(R.id.etPaswwordCSignUp);
        signUpBtn = findViewById(R.id.btnSignUp);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }

    private void signUp() {
        JsonObject object = new JsonObject();

        object.addProperty("username", usernameEt.getText().toString().trim());
        object.addProperty("email", emailEt.getText().toString().trim());
        object.addProperty("password", passwordEt.getText().toString().trim());

        ApiUtil.getServiceClass().signUp(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(SignupActivity.this, response.body().get("status").toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(SignupActivity.this, "please connect to the internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
