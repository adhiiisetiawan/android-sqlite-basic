package com.oxcart.sqlitekuy.Features.ReviewCRUD.UpdateReviewInfo;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.oxcart.sqlitekuy.Features.ReviewCRUD.CreateReview.Review;
import com.oxcart.sqlitekuy.R;
import com.oxcart.sqlitekuy.dbHelper.DatabaseContract;
import com.oxcart.sqlitekuy.dbHelper.DatabaseQueryClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewUpdateDIalogFragment extends DialogFragment {
    private static ReviewUpdateListener reviewUpdateListener;
    private static long reviewId;
    private static int position;
    private EditText reviwerNameEditText;
    private EditText rattingEditText;
    private EditText commentEditText;
    private Button updateButton;
    private Button cancelButton;
    private DatabaseQueryClass databaseQueryClass;

    public ReviewUpdateDIalogFragment() {
        // Required empty public constructor
    }

    public static ReviewUpdateDIalogFragment newInstance(long revId, int pos, ReviewUpdateListener listener) {
        reviewId = revId;
        position = pos;
        reviewUpdateListener = listener;

        ReviewUpdateDIalogFragment reviewUpdateDIalogFragment = new ReviewUpdateDIalogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Update review information");
        reviewUpdateDIalogFragment.setArguments(args);

        reviewUpdateDIalogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return reviewUpdateDIalogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_update_dialog, container, false);

        reviwerNameEditText = view.findViewById(R.id.reviewer_name_editTexts);
        rattingEditText = view.findViewById(R.id.ratting_editTexts);
        commentEditText = view.findViewById(R.id.comment_editTexts);
        updateButton = view.findViewById(R.id.updateButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        String title = getArguments().getString(DatabaseContract.TITLE);
        getDialog().setTitle(title);

        Review review = databaseQueryClass.getReviewById(reviewId);

        reviwerNameEditText.setText(review.getReviwer_name());
        rattingEditText.setText(String.valueOf(review.getRatting()));
        commentEditText.setText(review.getComment());
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reviewersName = reviwerNameEditText.getText().toString();
                int ratting = Integer.parseInt(rattingEditText.getText().toString());
                String comment = commentEditText.getText().toString();

                Review reviews = new Review(reviewId, reviewersName, ratting, comment);

                long rowCount = databaseQueryClass.updateReviewInfo(reviews);

                if (rowCount > 0) {
                    reviewUpdateListener.onReviewInfoUpdate(reviews, position);
                    getDialog().dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            //noinspection ConstantConditions
            dialog.getWindow().setLayout(width, height);
        }
    }
}
