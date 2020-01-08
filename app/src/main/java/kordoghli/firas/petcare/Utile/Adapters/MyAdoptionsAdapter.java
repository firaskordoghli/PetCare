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

import kordoghli.firas.petcare.Data.Adoption;
import kordoghli.firas.petcare.Data.Pet;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.retrofit.ApiUtil;

public class MyAdoptionsAdapter extends RecyclerView.Adapter<MyAdoptionsAdapter.MyAdoptionsViewHolder> {

    private List<Adoption> mAdoptionsList;
    private MyAdoptionsAdapter.OnItemClickListener mListener;

    public MyAdoptionsAdapter(List<Adoption> adoptionList) {
        this.mAdoptionsList = adoptionList;
    }

    public void setOnItemClickListener(MyAdoptionsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public MyAdoptionsAdapter.MyAdoptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_adoptions, parent, false);
        MyAdoptionsAdapter.MyAdoptionsViewHolder MyAdoptionsViewHolder = new MyAdoptionsAdapter.MyAdoptionsViewHolder(view, mListener);
        return MyAdoptionsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdoptionsAdapter.MyAdoptionsViewHolder holder, int position) {
        Adoption adoption = mAdoptionsList.get(position);

        holder.namePetTv.setText(adoption.getName());
        holder.birthPetTv.setText(adoption.getBirthDate());
        Picasso.get().load(ApiUtil.photoUrl() +adoption.getPhoto()).into(holder.petIv);
        if (adoption.getGender().equals("Male")){
            holder.petGenderIv.setImageResource(R.drawable.ic_male);
        }else if (adoption.getGender().equals("Femelle")){
            holder.petGenderIv.setImageResource(R.drawable.ic_female);
        }
    }

    @Override
    public int getItemCount() {
        return mAdoptionsList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class MyAdoptionsViewHolder extends RecyclerView.ViewHolder {
        public ImageView petIv, petGenderIv;
        public TextView namePetTv, birthPetTv;

        public MyAdoptionsViewHolder(@NonNull View itemView, final MyAdoptionsAdapter.OnItemClickListener listener) {
            super(itemView);
            namePetTv = itemView.findViewById(R.id.etMyAdoptionsNameItem);
            birthPetTv = itemView.findViewById(R.id.etMyAdoptionsBirthItem);

            petIv = itemView.findViewById(R.id.ivMyAdoptionsItem);
            petGenderIv = itemView.findViewById(R.id.ivMyAdoptionsGenderItem);

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
