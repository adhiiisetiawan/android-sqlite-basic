package com.oxcart.sqlitekuy.Features.BookCRUD.ShowBookList;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.oxcart.sqlitekuy.Features.BookCRUD.CreateBook.Book;
import com.oxcart.sqlitekuy.Features.BookCRUD.CreateBook.BookCreateDialogFragment;
import com.oxcart.sqlitekuy.Features.BookCRUD.CreateBook.BookCreateListener;
import com.oxcart.sqlitekuy.Features.ReviewCRUD.CreateReview.Review;
import com.oxcart.sqlitekuy.R;
import com.oxcart.sqlitekuy.dbHelper.DatabaseContract;
import com.oxcart.sqlitekuy.dbHelper.DatabaseQueryClass;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements BookCreateListener, SearchView.OnQueryTextListener {
    private DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(this);

    private List<Book> bookList = new ArrayList<>();

    private TextView summaryTextView;
    private TextView bookListEmptyTextView;
    private RecyclerView recyclerView;
    private BookListRecyclerViewAdapter bookListRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Logger.addLogAdapter(new AndroidLogAdapter());

        recyclerView = findViewById(R.id.recyclerView);
        summaryTextView = findViewById(R.id.summaryTextView);
        bookListEmptyTextView = findViewById(R.id.emptyListTextView);

        bookList.addAll(databaseQueryClass.getAllBook());

        bookListRecyclerViewAdapter = new BookListRecyclerViewAdapter(this, bookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bookListRecyclerViewAdapter);

        viewVisibility();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBookCreateDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        printSummary();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_delete){

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure, You wanted to delete all students?");
            alertDialogBuilder.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            boolean isAllDeleted = databaseQueryClass.deleteAllBook();
                            if(isAllDeleted){
                                bookList.clear();
                                bookListRecyclerViewAdapter.notifyDataSetChanged();
                                viewVisibility();
                            }
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void viewVisibility() {
        if(bookList.isEmpty())
            bookListEmptyTextView.setVisibility(View.VISIBLE);
        else
            bookListEmptyTextView.setVisibility(View.GONE);
        printSummary();
    }

    private void openBookCreateDialog() {
        BookCreateDialogFragment bookCreateDialogFragment = BookCreateDialogFragment.newInstance("Create Student", this);
        bookCreateDialogFragment.show(getSupportFragmentManager(), DatabaseContract.CREATE_BOOK);
    }

    private void printSummary() {
        long bookNum = databaseQueryClass.getNumberOfBook();
        long reviewNum = databaseQueryClass.getNumberOfReview();

        summaryTextView.setText(getResources().getString(R.string.database_summary, bookNum, reviewNum));
    }


    @Override
    public void onBookCreated(Book book) {
        bookList.add(book);
        bookListRecyclerViewAdapter.notifyDataSetChanged();
        viewVisibility();
        Logger.d(book.getTitle());
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<Book> bookArrayList = new ArrayList<>();
        for (Book book : bookList){
            String titleBooks= book.getTitle().toLowerCase();
            if (titleBooks.contains(newText)){
                bookArrayList.add(book);
            }
        }

        bookListRecyclerViewAdapter.setFilter(bookArrayList);
        return true;
    }
}
