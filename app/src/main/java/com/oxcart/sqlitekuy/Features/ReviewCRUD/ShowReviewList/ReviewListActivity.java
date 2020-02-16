package com.oxcart.sqlitekuy.Features.ReviewCRUD.ShowReviewList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oxcart.sqlitekuy.Features.ReviewCRUD.CreateReview.Review;
import com.oxcart.sqlitekuy.Features.ReviewCRUD.CreateReview.ReviewCreateDialogFragment;
import com.oxcart.sqlitekuy.Features.ReviewCRUD.CreateReview.ReviewCreateListener;
import com.oxcart.sqlitekuy.R;
import com.oxcart.sqlitekuy.dbHelper.DatabaseContract;
import com.oxcart.sqlitekuy.dbHelper.DatabaseQueryClass;

import java.util.ArrayList;
import java.util.List;

public class ReviewListActivity extends AppCompatActivity implements ReviewCreateListener, SearchView.OnQueryTextListener {
    private long bookNumber;

    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Review> reviewList = new ArrayList<>();

    private TextView summaryTextView;
    private TextView reviewListEmptyTextView;
    private RecyclerView recyclerView;
    private ReviewListRecyclerViewAdapter reviewListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        summaryTextView = findViewById(R.id.summaryTextView);
        reviewListEmptyTextView = findViewById(R.id.emptyListTextView);

        bookNumber = getIntent().getLongExtra(DatabaseContract.BOOK_NUMBER, -1);

        reviewList.addAll(databaseQueryClass.getAllReviewByBookNo(bookNumber));

        reviewListRecyclerViewAdapter = new ReviewListRecyclerViewAdapter(this, reviewList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(reviewListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReviewCreateDialog();
            }
        });
    }

    private void printSummary() {
        long bookNum = databaseQueryClass.getNumberOfBook();
        long reviewNum = databaseQueryClass.getNumberOfReview();

        summaryTextView.setText(getResources().getString(R.string.database_summary, bookNum, reviewNum));
    }

    private void openReviewCreateDialog() {
        ReviewCreateDialogFragment reviewCreateDialogFragment = ReviewCreateDialogFragment.newInstance(bookNumber, this);
        reviewCreateDialogFragment.show(getSupportFragmentManager(), DatabaseContract.CREATE_REVIEW);
    }


    @Override
    public void onReviewCreated(Review review) {
        reviewList.add(review);
        reviewListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
    }

    public void viewVisibility() {
        if (reviewList.isEmpty())
            reviewListEmptyTextView.setVisibility(View.VISIBLE);
        else
            reviewListEmptyTextView.setVisibility(View.GONE);
        printSummary();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete all review?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                boolean isAllDeleted = databaseQueryClass.deleteAllReviewByBookNum(bookNumber);
                                if (isAllDeleted) {
                                    reviewList.clear();
                                    reviewListRecyclerViewAdapter.notifyDataSetChanged();
                                    viewVisibility();
                                }
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Review> reviewArrayList = new ArrayList<>();
        for (Review review : reviewList) {
            String reviewerName = review.getReviwer_name().toLowerCase();
            if (reviewerName.contains(newText)) {
                reviewArrayList.add(review);
            }
        }

        reviewListRecyclerViewAdapter.setFilter(reviewArrayList);
        return true;
    }
}
