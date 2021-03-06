package kordoghli.firas.petcare.Ui.Adoptions;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import kordoghli.firas.petcare.Data.Adoption;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Ui.LostAndFound.LostAndFoundActivity;
import kordoghli.firas.petcare.Utile.Adapters.AdoptionsAdapter;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdoptionsFragment extends Fragment {
    private RecyclerView mRecycleView;
    private AdoptionsAdapter adoptionsAdapter;
    private GridLayoutManager mGridLayoutManager;
    private TextView noAdoptionsTv;
    private Button toLostAndFoundBtn;
    private FloatingActionButton toAddAdoptionFab;
    private ShimmerFrameLayout mShimmerViewContainer;


    public AdoptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_adoptions, container, false);

        noAdoptionsTv = view.findViewById(R.id.textView4);
        toAddAdoptionFab = view.findViewById(R.id.fabToAddAdoption);
        toLostAndFoundBtn = view.findViewById(R.id.bntToLostAndFound);

        mRecycleView = view.findViewById(R.id.rvAdoptions);
        mRecycleView.setHasFixedSize(true);
        mShimmerViewContainer = view.findViewById(R.id.adoptionShimmer);

        toLostAndFoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), LostAndFoundActivity.class);
                startActivity(intent);
            }
        });

        toAddAdoptionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddAdoptionActivity.class);
                startActivity(intent);
            }
        });

        getAllAdoptions();
        return view;
    }

    private void getAllAdoptions() {
        ApiUtil.getServiceClass().getAllAdoptions().enqueue(new Callback<List<Adoption>>() {
            @Override
            public void onResponse(Call<List<Adoption>> call, Response<List<Adoption>> response) {
                final List<Adoption> adoptionList = response.body();
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
                if (adoptionList.size() == 0) {
                    noAdoptionsTv.setVisibility(View.VISIBLE);
                } else {
                    adoptionsAdapter = new AdoptionsAdapter(adoptionList);
                    mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    mRecycleView.setLayoutManager(mGridLayoutManager);
                    mRecycleView.setAdapter(adoptionsAdapter);
                    adoptionsAdapter.setOnItemClickListener(new AdoptionsAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Toast.makeText(getContext(), adoptionList.get(position).getId().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), AdoptionDetailsActivity.class);
                            intent.putExtra("idAdoptionFromAdoptions", adoptionList.get(position).getId());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Adoption>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmer();
        getAllAdoptions();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmer();
        super.onPause();
    }

}
