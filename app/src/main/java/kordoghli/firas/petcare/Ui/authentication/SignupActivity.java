package kordoghli.firas.petcare.Ui.authentication;

import android.app.ProgressDialog;
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

public class SignupActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private EditText emailEt, usernameEt, passwordEt, passwordCEt, phoneNumberEt;
    private Button signUpBtn;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // User Session Manager
        sessionManager = new SessionManager(getApplicationContext());

        emailEt = findViewById(R.id.etEmailSignUp);
        usernameEt = findViewById(R.id.etUsernameSignUp);
        passwordEt = findViewById(R.id.etPasswordSignUp);
        passwordCEt = findViewById(R.id.etPaswwordCSignUp);
        phoneNumberEt = findViewById(R.id.etPhoneSignUp);
        signUpBtn = findViewById(R.id.btnSignUp);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()) {
                    displayLoader();
                    signUp();
                }
            }
        });

    }

    private void signUp() {

        JsonObject object = new JsonObject();

        object.addProperty("username", usernameEt.getText().toString().trim());
        object.addProperty("email", emailEt.getText().toString().trim());
        object.addProperty("phone", phoneNumberEt.getText().toString().trim());
        object.addProperty("password", passwordEt.getText().toString().trim());

        ApiUtil.getServiceClass().signUp(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Gson gson = new Gson();
                pDialog.dismiss();
                JsonObject myJsonResponse = new JsonObject();
                myJsonResponse.getAsJsonObject(gson.toJson(response.body()));
                boolean existe = gson.toJson(response.body()).contains("false");
                if (existe) {
                    emailEt.setError("email already exists");
                    emailEt.requestFocus();
                } else {
                    User responseUser = gson.fromJson(response.body(), User.class);
                    sessionManager.createUserLoginSession(gson.toJson(responseUser));
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println(t.getMessage());
                pDialog.dismiss();
                Toast.makeText(SignupActivity.this, "please connect to the internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        if (usernameEt.getText().toString().equals("")) {
            usernameEt.setError("required");
            usernameEt.requestFocus();
            return false;
        }
        if (emailEt.getText().toString().equals("") || !emailEt.getText().toString().contains("@")) {
            emailEt.setError("email not valid");
            emailEt.requestFocus();
            return false;
        }
        if (phoneNumberEt.getText().toString().equals("")) {
            phoneNumberEt.setError("required");
            phoneNumberEt.requestFocus();
            return false;
        }
        if (passwordEt.getText().toString().equals("")) {
            passwordEt.setError("required");
            passwordEt.requestFocus();
            return false;
        }
        if (!passwordCEt.getText().toString().equals(passwordEt.getText().toString())) {
            passwordCEt.setError("unmatched password");
            passwordCEt.requestFocus();
            return false;
        }
        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(SignupActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
