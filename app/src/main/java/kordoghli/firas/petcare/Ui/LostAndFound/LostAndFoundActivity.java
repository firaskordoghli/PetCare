package kordoghli.firas.petcare.Ui.LostAndFound;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import kordoghli.firas.petcare.Data.Lost;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.Adapters.LostAndFoundAdapter;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LostAndFoundActivity extends AppCompatActivity {
    private ImageView toAddLostBtn,toLostBtn,toFoundBtn;

    private RecyclerView mRecycleView;
    private RecyclerView.LayoutManager mLayoutManager;
    private LostAndFoundAdapter lostAndFoundAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found);

        toAddLostBtn=findViewById(R.id.btnToAddLost);
        toLostBtn=findViewById(R.id.btnToLost);
        toFoundBtn=findViewById(R.id.btnToFound);
        mRecycleView = findViewById(R.id.rvLostAndFound);
        mRecycleView.setHasFixedSize(true);

        mShimmerViewContainer = findViewById(R.id.lostAndFoundShimmer);

        toAddLostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddLostActivity.class);
                startActivity(intent);
            }
        });

        toLostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LostActivity.class);
                startActivity(intent);
            }
        });

        toFoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FoundActivity.class);
                startActivity(intent);
            }
        });

        getAllLostAndFound();
    }

    private void getAllLostAndFound(){
        ApiUtil.getServiceClass().getAllLostAndFound().enqueue(new Callback<List<Lost>>() {
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
                        Intent intent = new Intent(getApplicationContext(),LostDetailActivity.class);
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
        getAllLostAndFound();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }


}
