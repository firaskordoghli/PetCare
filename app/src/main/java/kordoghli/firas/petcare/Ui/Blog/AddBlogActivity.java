package kordoghli.firas.petcare.Ui.Blog;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Calendar;

import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddBlogActivity extends AppCompatActivity {
    private Button addPostBtn;
    private EditText titleEt, descriptionEt;
    private SessionManager sessionManager;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blog);
        addPostBtn = findViewById(R.id.btnAddPost);
        titleEt = findViewById(R.id.etAddPostTitle);
        descriptionEt = findViewById(R.id.etAddPostDescription);

        sessionManager = new SessionManager(getApplicationContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);

        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()){
                    addPost(currentUser.getId());
                }
            }
        });
    }

    private void addPost(Integer id_user) {
        displayLoader();
        JsonObject object = new JsonObject();

        object.addProperty("subject", titleEt.getText().toString());
        object.addProperty("content", descriptionEt.getText().toString());
        object.addProperty("date", Calendar.getInstance().getTime().toString());
        object.addProperty("id_user", id_user);

        ApiUtil.getServiceClass().addPost(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(AddBlogActivity.this, "Post added with succes", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(AddBlogActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInputs() {
        if (titleEt.getText().toString().equals("")) {
            titleEt.setError("required");
            titleEt.requestFocus();
            return false;
        }
        if (descriptionEt.getText().toString().equals("")) {
            descriptionEt.setError("required");
            descriptionEt.requestFocus();
            return false;
        }
        return true;
    }

    private void displayLoader() {
        pDialog = new ProgressDialog(AddBlogActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
