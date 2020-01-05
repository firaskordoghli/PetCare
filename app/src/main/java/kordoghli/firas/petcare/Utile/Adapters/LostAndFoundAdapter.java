package kordoghli.firas.petcare.Utile.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kordoghli.firas.petcare.Data.Lost;
import kordoghli.firas.petcare.R;

public class LostAndFoundAdapter extends RecyclerView.Adapter<LostAndFoundAdapter.LostAndFoundViewHolder> {
    private List<Lost> mlostList;
    private LostAndFoundAdapter.OnItemClickListener mListener;

    public LostAndFoundAdapter(List<Lost> lostList) {
        this.mlostList = lostList;
    }

    public void setOnItemClickListener(LostAndFoundAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public LostAndFoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lost_and_found, parent, false);
        LostAndFoundViewHolder lostAndFoundViewHolder = new LostAndFoundViewHolder(view, mListener);
        return lostAndFoundViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LostAndFoundViewHolder holder, int position) {
        Lost lost = mlostList.get(position);

        holder.descriptionTv.setText(lost.getDescription());
        holder.classificationTv.setText(lost.getClassification());

        if (lost.getClassification().equals("Lost")){
            holder.classificationTv.setBackgroundColor(Color.parseColor("#d11141"));
        }else if (lost.getClassification().equals("Found")) {
            holder.classificationTv.setBackgroundColor(Color.parseColor("#00aedb"));
        }

    }

    @Override
    public int getItemCount() {
        return mlostList.size();
    }

    public interface OnItemClickListener { void onItemClick(int position);}

    public class LostAndFoundViewHolder extends RecyclerView.ViewHolder {
        public TextView descriptionTv, classificationTv;
        private ImageView lostAndFoundIv;

        public LostAndFoundViewHolder(@NonNull View itemView, final LostAndFoundAdapter.OnItemClickListener listener) {
            super(itemView);
            descriptionTv = itemView.findViewById(R.id.tvDescriptionLostAndFoundItem);
            classificationTv = itemView.findViewById(R.id.tvTypeLostAndFoundItem);
            lostAndFoundIv = itemView.findViewById(R.id.ivLostAndFoundItem);

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
