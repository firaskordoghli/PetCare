package kordoghli.firas.petcare.Utile.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kordoghli.firas.petcare.Data.Post;
import kordoghli.firas.petcare.R;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {
    private List<Post> mPostsList;
    private PostsAdapter.OnItemClickListener mListener;

    public PostsAdapter(List<Post> postsList) {
        this.mPostsList = postsList;
    }

    public void setOnItemClickListener(PostsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        PostViewHolder postViewHolder = new PostViewHolder(view, mListener);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = mPostsList.get(position);

        holder.titlePostTv.setText(post.getSubject());
        holder.datePostTv.setText(post.getDate().substring(0, 10));
        holder.timePostTv.setText(post.getDate().substring(10, 16));
        holder.descriptionPostTv.setText(post.getContent());

    }

    @Override
    public int getItemCount() {
        return mPostsList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView titlePostTv, datePostTv,timePostTv, descriptionPostTv;

        public PostViewHolder(@NonNull View itemView, final PostsAdapter.OnItemClickListener listener) {
            super(itemView);
            titlePostTv = itemView.findViewById(R.id.tvPostTitleItem);
            timePostTv = itemView.findViewById(R.id.tvPostTimeItem);
            datePostTv = itemView.findViewById(R.id.tvPostDateItem);
            descriptionPostTv = itemView.findViewById(R.id.tvPostDescriptionItem);

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
