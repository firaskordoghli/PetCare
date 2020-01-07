package kordoghli.firas.petcare.Utile.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import kordoghli.firas.petcare.Data.Comment;
import kordoghli.firas.petcare.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> mCommentList;
    private CommentAdapter.OnItemClickListener mListener;

    public CommentAdapter(List<Comment> commentList) {
        this.mCommentList = commentList;
    }

    public void setOnItemClickListener(CommentAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(view, mListener);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = mCommentList.get(position);
        holder.contentTv.setText(comment.getContent());
        holder.userNameTv.setText(comment.getName());
        holder.dateTv.setText(comment.getDate());
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    public interface OnItemClickListener { void onItemClick(int position);}

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView userNameTv, contentTv,dateTv;

        public CommentViewHolder(@NonNull View itemView, final CommentAdapter.OnItemClickListener listener) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.tvCommentUserItem);
            contentTv = itemView.findViewById(R.id.tvCommentContentItem);
            dateTv = itemView.findViewById(R.id.tvCommentDateItem);

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
