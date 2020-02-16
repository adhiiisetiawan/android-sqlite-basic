package com.oxcart.sqlitekuy.Features.ReviewCRUD.ShowReviewList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oxcart.sqlitekuy.R;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView reviwerNameTextView;
    TextView rattingTextView;
    TextView commentTextView;

    ImageView crossButtonImageView;
    ImageView editButtonImageView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        reviwerNameTextView= itemView.findViewById(R.id.reviewer_name_textView);
        rattingTextView = itemView.findViewById(R.id.ratting_TextView);
        commentTextView = itemView.findViewById(R.id.comment_textView);

        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        editButtonImageView = itemView.findViewById(R.id.editImageView);
    }
}
