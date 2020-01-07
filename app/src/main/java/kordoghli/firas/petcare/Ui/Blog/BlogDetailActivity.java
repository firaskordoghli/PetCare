package kordoghli.firas.petcare.Ui.Blog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kordoghli.firas.petcare.Data.Comment;
import kordoghli.firas.petcare.Data.Post;
import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.Adapters.CommentAdapter;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogDetailActivity extends AppCompatActivity {
    private TextView titlePostTv,datePostTv,descriptionPostTv;
    private Integer idPostFromBlog = null;
    private ImageButton backBtn,addCommentBtn;
    private FloatingActionButton deletePostDetailFab;
    private SessionManager sessionManager;
    private RecyclerView mRecycleView;
    private CommentAdapter commentAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private EditText commentEt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        titlePostTv = findViewById(R.id.tvPostDetailTitle);
        datePostTv = findViewById(R.id.tvPostDetailDate);
        descriptionPostTv = findViewById(R.id.tvPostDetailDescription);
        backBtn = findViewById(R.id.btnBackPostDetail);
        deletePostDetailFab = findViewById(R.id.fabPostDetailDelete);
        commentEt = findViewById(R.id.etAddComment);
        addCommentBtn = findViewById(R.id.btnAddComment);

        mRecycleView = findViewById(R.id.rvCommentsBlogDetail);
        mRecycleView.setHasFixedSize(true);
        mShimmerViewContainer = findViewById(R.id.commentShimmer);

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
        getCommentsForPost(idPostFromBlog);

        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInputs()){
                    addComment(idPostFromBlog);
                }
            }
        });
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

    private void getCommentsForPost (Integer idPostFromBlog){
        JsonObject object = new JsonObject();
        object.addProperty("id_post", idPostFromBlog);

        ApiUtil.getServiceClass().getCommentsForPost(object).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                final List<Comment> commentList = response.body();
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);

                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                commentAdapter = new CommentAdapter(commentList);
                mRecycleView.setLayoutManager(mLayoutManager);
                mRecycleView.setAdapter(commentAdapter);
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
    }

    private void addComment(Integer idPostFromBlog) {
        sessionManager = new SessionManager(getApplicationContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);
        Date currentTime = Calendar.getInstance().getTime();

        JsonObject object = new JsonObject();
        object.addProperty("content", commentEt.getText().toString());
        object.addProperty("name", currentUser.getUsername());
        object.addProperty("date", currentTime.toString().substring(0, 10));
        object.addProperty("id_post", idPostFromBlog);
        object.addProperty("id_user", currentUser.getId());

        ApiUtil.getServiceClass().addComment(object).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Toast.makeText(BlogDetailActivity.this, "comment added", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private boolean validateInputs() {
        if (commentEt.getText().toString().equals("")) {
            commentEt.setError("required");
            commentEt.requestFocus();
            return false;
        }
        return true;
    }
}
