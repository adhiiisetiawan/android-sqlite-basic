package com.oxcart.sqlitekuy.Features.ReviewCRUD.ShowReviewList;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.oxcart.sqlitekuy.Features.ReviewCRUD.CreateReview.Review;
import com.oxcart.sqlitekuy.Features.ReviewCRUD.UpdateReviewInfo.ReviewUpdateDIalogFragment;
import com.oxcart.sqlitekuy.Features.ReviewCRUD.UpdateReviewInfo.ReviewUpdateListener;
import com.oxcart.sqlitekuy.R;
import com.oxcart.sqlitekuy.dbHelper.DatabaseContract;
import com.oxcart.sqlitekuy.dbHelper.DatabaseQueryClass;

import java.util.ArrayList;
import java.util.List;

public class ReviewListRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public ReviewListRecyclerViewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int listPosition = position;
        final Review review = reviewList.get(position);

        holder.reviwerNameTextView.setText(review.getReviwer_name());
        holder.rattingTextView.setText(String.valueOf(review.getRatting()));
        holder.commentTextView.setText(String.valueOf(review.getComment()));

        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this review?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteReview(review);
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        holder.editButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editReview(review.getId(), listPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    private void editReview(long reviewId, int listPosition) {
        ReviewUpdateDIalogFragment reviewUpdateDIalogFragment = ReviewUpdateDIalogFragment.newInstance(reviewId, listPosition, new ReviewUpdateListener() {
            @Override
            public void onReviewInfoUpdate(Review review, int position) {
                reviewList.set(position, review);
                notifyDataSetChanged();
            }
        });
        reviewUpdateDIalogFragment.show(((ReviewListActivity) context).getSupportFragmentManager(), DatabaseContract.UPDATE_REVIEW);
    }


    private void deleteReview(Review review) {
        DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(context);
        boolean isDeleted = databaseQueryClass.deleteReviewById(review.getId());

        if (isDeleted) {
            reviewList.remove(review);
            notifyDataSetChanged();
            ((ReviewListActivity) context).viewVisibility();
        } else
            Toast.makeText(context, "Cannot delete!", Toast.LENGTH_SHORT).show();
    }

    public void setFilter(ArrayList<Review> newListReview) {
        reviewList = new ArrayList<>();
        reviewList.addAll(newListReview);
        notifyDataSetChanged();
    }
}
