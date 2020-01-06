package kordoghli.firas.petcare.Ui.Account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import kordoghli.firas.petcare.Data.Lost;
import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Ui.LostAndFound.LostDetailActivity;
import kordoghli.firas.petcare.Utile.Adapters.LostAndFoundAdapter;
import kordoghli.firas.petcare.Utile.Adapters.PostsAdapter;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLostAndFoundActivity extends AppCompatActivity {
    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LostAndFoundAdapter lostAndFoundAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lost_and_found);

        mRecycleView = findViewById(R.id.rvMyLostAndFound);
        mRecycleView.setHasFixedSize(true);
        mShimmerViewContainer = findViewById(R.id.myLostAndFoundsShimmer);

        // User Session Manager
        sessionManager = new SessionManager(getApplicationContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);

        myLastAndFound(currentUser.getId());
    }

    private void myLastAndFound(Integer id){
        JsonObject object = new JsonObject();
        object.addProperty("id_user", id);

        ApiUtil.getServiceClass().getAllMylostAndFound(object).enqueue(new Callback<List<Lost>>() {
            @Override
            public void onResponse(Call<List<Lost>> call, Response<List<Lost>> response) {
                final List<Lost> lostList = response.body();
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);

                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                lostAndFoundAdapter = new LostAndFoundAdapter(lostList);
                mRecycleView.setLayoutManager(mLayoutManager);
                mRecycleView.setAdapter(lostAndFoundAdapter);

                lostAndFoundAdapter.setOnItemClickListener(new LostAndFoundAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getApplicationContext(), LostDetailActivity.class);
                        intent.putExtra("idLostFromLostAndFound", lostList.get(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Lost>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }
}
