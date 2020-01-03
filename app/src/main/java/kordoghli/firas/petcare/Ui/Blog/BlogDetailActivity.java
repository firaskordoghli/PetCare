package kordoghli.firas.petcare.Ui.Blog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import kordoghli.firas.petcare.Data.Post;
import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogDetailActivity extends AppCompatActivity {
    private TextView titlePostTv,datePostTv,descriptionPostTv;
    private Integer idPostFromBlog = null;
    private ImageButton backBtn;
    private FloatingActionButton deletePostDetailFab;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        titlePostTv = findViewById(R.id.tvPostDetailTitle);
        datePostTv = findViewById(R.id.tvPostDetailDate);
        descriptionPostTv = findViewById(R.id.tvPostDetailDescription);
        backBtn = findViewById(R.id.btnBackPostDetail);
        deletePostDetailFab = findViewById(R.id.fabPostDetailDelete);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idPostFromBlog = extras.getInt("idPostFromBlog");
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        deletePostDetailFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(BlogDetailActivity.this)
                        .setTitle("Delete post")
                        .setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deletePost(idPostFromBlog);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.ic_warning_black_24dp)
                        .show();
            }
        });
        getPostById(idPostFromBlog);
    }

    private void getPostById(Integer idPostFromBlog){
        JsonObject object = new JsonObject();
        object.addProperty("id", idPostFromBlog);

        // User Session Manager
        sessionManager = new SessionManager(getApplicationContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);

        ApiUtil.getServiceClass().getPostById(object).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();

                if (post.getId_user().equals(currentUser.getId())){
                    deletePostDetailFab.show();
                }else {
                    deletePostDetailFab.hide();
                }

                titlePostTv.setText(post.getSubject());
                datePostTv.setText(post.getDate().substring(0, 10));
                descriptionPostTv.setText(post.getContent());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    private void deletePost(Integer id_post){
        JsonObject object = new JsonObject();
        object.addProperty("id", id_post);

        ApiUtil.getServiceClass().deleteMyPostById(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(BlogDetailActivity.this, "post Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
