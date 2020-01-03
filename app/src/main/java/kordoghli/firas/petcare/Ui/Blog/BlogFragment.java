package kordoghli.firas.petcare.Ui.Blog;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import kordoghli.firas.petcare.Data.Post;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.Adapters.PostsAdapter;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlogFragment extends Fragment {
    private FloatingActionButton toAddPostBtn;
    private RecyclerView mRecycleView;
    private PostsAdapter postAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;


    public BlogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blog, container, false);
        toAddPostBtn = view.findViewById(R.id.btnToAddPost);
        mRecycleView = view.findViewById(R.id.rvPosts);
        mRecycleView.setHasFixedSize(true);
        mShimmerViewContainer = view.findViewById(R.id.postShimmer);

        listPosts();

        toAddPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),AddBlogActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void listPosts(){
        ApiUtil.getServiceClass().getAllPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                final List<Post> postList = response.body();
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);

                mLayoutManager = new LinearLayoutManager(getContext());
                postAdapter = new PostsAdapter(postList);
                mRecycleView.setLayoutManager(mLayoutManager);
                mRecycleView.setAdapter(postAdapter);

                postAdapter.setOnItemClickListener(new PostsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getContext(), BlogDetailActivity.class);
                        intent.putExtra("idPostFromBlog", postList.get(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
        listPosts();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

}
