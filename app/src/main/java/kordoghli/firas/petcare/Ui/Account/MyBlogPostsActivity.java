package kordoghli.firas.petcare.Ui.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import kordoghli.firas.petcare.Data.Post;
import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Ui.Blog.BlogDetailActivity;
import kordoghli.firas.petcare.Utile.Adapters.PostsAdapter;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBlogPostsActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private PostsAdapter postAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private SessionManager sessionManager;
    private TextView noPostTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_blog_posts);

        mRecycleView = findViewById(R.id.rvMyBlogPosts);
        mRecycleView.setHasFixedSize(true);
        mShimmerViewContainer = findViewById(R.id.myBlogPostsShimmer);
        noPostTv = findViewById(R.id.tvNoMyBlogPost);

        // User Session Manager
        sessionManager = new SessionManager(getApplicationContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);

        myPosts(currentUser.getId());
    }

    private void myPosts(Integer id){
        JsonObject object = new JsonObject();
        object.addProperty("id_user", id);

        ApiUtil.getServiceClass().getAllMyPosts(object).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                final List<Post> postList = response.body();
                if (postList.size() == 0){
                    noPostTv.setVisibility(View.VISIBLE);
                    mShimmerViewContainer.setVisibility(View.GONE);
                }else {
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);

                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    postAdapter = new PostsAdapter(postList);
                    mRecycleView.setLayoutManager(mLayoutManager);
                    mRecycleView.setAdapter(postAdapter);

                    postAdapter.setOnItemClickListener(new PostsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(getApplicationContext(), BlogDetailActivity.class);
                            intent.putExtra("idPostFromBlog", postList.get(position).getId());
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // User Session Manager
        sessionManager = new SessionManager(getApplicationContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);

        myPosts(currentUser.getId());
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }
}
