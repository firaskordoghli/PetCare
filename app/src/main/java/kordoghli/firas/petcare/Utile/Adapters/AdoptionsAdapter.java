package kordoghli.firas.petcare.Utile.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kordoghli.firas.petcare.Data.Adoption;
import kordoghli.firas.petcare.R;

public class AdoptionsAdapter extends RecyclerView.Adapter<AdoptionsAdapter.AdoptionsViewHolder> {

    private List<Adoption> mAdoptionsList;
    private AdoptionsAdapter.OnItemClickListener mListener;

    public AdoptionsAdapter(List<Adoption> adoptionsList) { this.mAdoptionsList = adoptionsList; }

    public void setOnItemClickListener(AdoptionsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public AdoptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adoptions, parent, false);
        AdoptionsViewHolder adoptionsViewHolder = new AdoptionsViewHolder(view, mListener);
        return adoptionsViewHolder;
    }

    @Override
    public void onBindViewHolder(final AdoptionsViewHolder holder, int position) {
        Adoption adoption = mAdoptionsList.get(position);
        holder.adoptionNameTv.setText(adoption.getName());
        holder.adoptionLocationTv.setText(adoption.getGender());

    }

    @Override
    public int getItemCount() {
        return mAdoptionsList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class AdoptionsViewHolder extends RecyclerView.ViewHolder {
        public ImageView adoptionIv;
        public TextView adoptionNameTv, adoptionLocationTv;

        AdoptionsViewHolder(View itemView, final AdoptionsAdapter.OnItemClickListener listener) {
            super(itemView);
            adoptionNameTv = itemView.findViewById(R.id.etAdoptionNameItem);
            adoptionLocationTv = itemView.findViewById(R.id.etAdoptionLocationItem);
            adoptionIv = itemView.findViewById(R.id.ivAdoptionItem);

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


