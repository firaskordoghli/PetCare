package kordoghli.firas.petcare.Ui.MyPet;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.List;

import kordoghli.firas.petcare.Data.Pet;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.Adapters.MyPetsAdapter;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPetsFragment extends Fragment {

    private Button toAddPetBtn;
    private RecyclerView mRecycleView;
    private MyPetsAdapter petAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public MyPetsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_pets, container, false);
        toAddPetBtn = view.findViewById(R.id.button);
        mRecycleView = view.findViewById(R.id.rvMyPets);
        mRecycleView.setHasFixedSize(true);
        toAddPetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddMyPetActivity.class);
                startActivity(intent);
            }
        });
        listPets();
        return view;
    }

    private void listPets() {
        JsonObject object = new JsonObject();
        object.addProperty("id_user", 1);

        ApiUtil.getServiceClass().getAllMyPets(object).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                final List<Pet> petList = response.body();
                mLayoutManager = new LinearLayoutManager(getContext());
                petAdapter =new MyPetsAdapter(petList);
                mRecycleView.setLayoutManager(mLayoutManager);
                mRecycleView.setAdapter(petAdapter);
                
                petAdapter.setOnItemClickListener(new MyPetsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(getContext(), petList.get(position).getId().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {

            }
        });
    }

}
