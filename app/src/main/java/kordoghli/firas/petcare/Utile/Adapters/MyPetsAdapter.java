package kordoghli.firas.petcare.Utile.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kordoghli.firas.petcare.Data.Pet;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;

public class MyPetsAdapter extends RecyclerView.Adapter<MyPetsAdapter.PetViewHolder> {

    private List<Pet> mPetsList;
    private MyPetsAdapter.OnItemClickListener mListener;

    public MyPetsAdapter(List<Pet> petList) {
        this.mPetsList = petList;
    }

    public void setOnItemClickListener(MyPetsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_pets, parent, false);
        PetViewHolder petViewHolder = new PetViewHolder(view, mListener);
        return petViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet pet = mPetsList.get(position);

        holder.namePetTv.setText(pet.getName());
        holder.birthPetTv.setText(pet.getBirthDate());
        Picasso.get().load(ApiUtil.photoUrl() +pet.getPhoto()).into(holder.petIv);
        if (pet.getGender().equals("Male")){
            holder.petGenderIv.setImageResource(R.drawable.ic_male);
        }else if (pet.getGender().equals("Femelle")){
            holder.petGenderIv.setImageResource(R.drawable.ic_female);
        }
    }

    @Override
    public int getItemCount() {
        return mPetsList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class PetViewHolder extends RecyclerView.ViewHolder {
        public ImageView petIv, petGenderIv;
        public TextView namePetTv, birthPetTv;

        public PetViewHolder(@NonNull View itemView, final MyPetsAdapter.OnItemClickListener listener) {
            super(itemView);
            namePetTv = itemView.findViewById(R.id.etPetNameItem);
            birthPetTv = itemView.findViewById(R.id.etPetBirthItem);

            petIv = itemView.findViewById(R.id.ivPetItem);
            petGenderIv = itemView.findViewById(R.id.ivGenderItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
