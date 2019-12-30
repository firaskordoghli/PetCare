package kordoghli.firas.petcare.Ui.Account;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import kordoghli.firas.petcare.Data.Adoption;
import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Ui.Adoptions.AdoptionDetailsActivity;
import kordoghli.firas.petcare.Ui.MyPet.MyPetDetailsActivity;
import kordoghli.firas.petcare.Utile.Adapters.MyAdoptionsAdapter;
import kordoghli.firas.petcare.Utile.SessionManager;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAdoptionsFragment extends Fragment {
    private SessionManager sessionManager;
    private RecyclerView mRecycleView;
    private MyAdoptionsAdapter petAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView noAdoptionstv;


    public MyAdoptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_adoptions, container, false);

        mRecycleView = view.findViewById(R.id.rvMyAdoptionsProfile);
        mRecycleView.setHasFixedSize(true);
        noAdoptionstv = view.findViewById(R.id.tvNoAdoptionsProfile);

        // User Session Manager
        sessionManager = new SessionManager(getContext());
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);
        listAdoptions(currentUser.getId());

        return view;
    }

    private void listAdoptions(Integer id_user) {
        JsonObject object = new JsonObject();
        object.addProperty("id_user", id_user);

        ApiUtil.getServiceClass().getAllMyAdoptions(object).enqueue(new Callback<List<Adoption>>() {
            @Override
            public void onResponse(Call<List<Adoption>> call, Response<List<Adoption>> response) {
                final List<Adoption> adoptionList = response.body();

                if (adoptionList.size() == 0) {
                    noAdoptionstv.setVisibility(View.VISIBLE);
                } else {
                    mLayoutManager = new LinearLayoutManager(getContext());
                    petAdapter = new MyAdoptionsAdapter(adoptionList);
                    mRecycleView.setLayoutManager(mLayoutManager);
                    mRecycleView.setAdapter(petAdapter);

                    petAdapter.setOnItemClickListener(new MyAdoptionsAdapter.OnItemClickListener() {
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
        Gson gson = new Gson();
        final User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);
        listAdoptions(currentUser.getId());
    }

}
