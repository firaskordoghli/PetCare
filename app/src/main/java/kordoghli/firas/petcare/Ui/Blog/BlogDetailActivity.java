package kordoghli.firas.petcare.Ui.Blog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.JsonObject;

import kordoghli.firas.petcare.Data.Post;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogDetailActivity extends AppCompatActivity {
    private TextView titlePostTv,datePostTv,descriptionPostTv;
    private Integer idPostFromBlog = null;
    private ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        titlePostTv = findViewById(R.id.tvPostDetailTitle);
        datePostTv = findViewById(R.id.tvPostDetailDate);
        descriptionPostTv = findViewById(R.id.tvPostDetailDescription);
        backBtn = findViewById(R.id.btnBackPostDetail);

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
        getPostById(idPostFromBlog);
    }

    private void getPostById(Integer idPostFromBlog){
        JsonObject object = new JsonObject();
        object.addProperty("id", idPostFromBlog);

        ApiUtil.getServiceClass().getPostById(object).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();
                titlePostTv.setText(post.getSubject());
                datePostTv.setText(post.getDate().substring(0, 10));
                descriptionPostTv.setText(post.getContent());
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }
}
