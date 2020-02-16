package com.oxcart.sqlitekuy.Features.ReviewCRUD.CreateReview;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.oxcart.sqlitekuy.R;
import com.oxcart.sqlitekuy.dbHelper.DatabaseQueryClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewCreateDialogFragment extends DialogFragment {
    private static long bookNumber;
    private static ReviewCreateListener reviewCreateListener;

    private EditText reviewerNameEditText;
    private EditText rattingEditText;
    private EditText commentEditText;

    private Button createButton;
    private Button cancelButton;

    public ReviewCreateDialogFragment() {
        // Required empty public constructor
    }

    public static ReviewCreateDialogFragment newInstance(long bookBookNumber, ReviewCreateListener listener) {
        bookNumber = bookBookNumber;
        reviewCreateListener = listener;

        ReviewCreateDialogFragment reviewCreateDialogFragment = new ReviewCreateDialogFragment();

        reviewCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return reviewCreateDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_create_dialog, container, false);
        getDialog().setTitle(getResources().getString(R.string.add_new_review));

        reviewerNameEditText = view.findViewById(R.id.reviewer_name_editText);
        rattingEditText = view.findViewById(R.id.ratting_editText);
        commentEditText = view.findViewById(R.id.comment_editText);

        createButton = view.findViewById(R.id.createButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewerName = reviewerNameEditText.getText().toString();
                int ratting = Integer.parseInt(rattingEditText.getText().toString());
                String comment = commentEditText.getText().toString();

                Review review = new Review(-1, reviewerName, ratting, comment);

                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                long id = databaseQueryClass.insertReview(review, bookNumber);

                if (id > 0) {
                    review.setId(id);
                    reviewCreateListener.onReviewCreated(review);
                    getDialog().dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
