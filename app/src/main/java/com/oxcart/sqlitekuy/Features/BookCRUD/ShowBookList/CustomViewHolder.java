package com.oxcart.sqlitekuy.Features.BookCRUD.ShowBookList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oxcart.sqlitekuy.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    TextView titleTextView;
    TextView bookNumTextView;
    TextView authorTextView;
    TextView yearTextView;
    TextView descriptionTextView;

    ImageView crossButtonImageView;
    ImageView editButtonImageView;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);

        titleTextView = itemView.findViewById(R.id.titleTextView);
        bookNumTextView = itemView.findViewById(R.id.book_number_TextView);
        authorTextView = itemView.findViewById(R.id.author_TextView);
        yearTextView = itemView.findViewById(R.id.year_TextView);
        descriptionTextView = itemView.findViewById(R.id.description_TextView);

        crossButtonImageView = itemView.findViewById(R.id.crossImageView);
        editButtonImageView = itemView.findViewById(R.id.editImageView);
    }
}
