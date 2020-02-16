package com.oxcart.sqlitekuy.Features.BookCRUD.ShowBookList;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.oxcart.sqlitekuy.Features.BookCRUD.CreateBook.Book;
import com.oxcart.sqlitekuy.Features.BookCRUD.UpdateBookInfo.BookUpdateDialogFragment;
import com.oxcart.sqlitekuy.Features.BookCRUD.UpdateBookInfo.BookUpdateListener;
import com.oxcart.sqlitekuy.Features.ReviewCRUD.ShowReviewList.ReviewListActivity;
import com.oxcart.sqlitekuy.R;
import com.oxcart.sqlitekuy.dbHelper.DatabaseContract;
import com.oxcart.sqlitekuy.dbHelper.DatabaseQueryClass;

import java.util.ArrayList;
import java.util.List;

public class BookListRecyclerViewAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private Context context;
    private List<Book> bookList;
    private DatabaseQueryClass databaseQueryClass;

    public BookListRecyclerViewAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
        databaseQueryClass = new DatabaseQueryClass(context);
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        final int itemPosition = position;
        final Book book = bookList.get(position);

        holder.bookNumTextView.setText(String.valueOf(book.getBook_number()));
        holder.titleTextView.setText(book.getTitle());
        holder.authorTextView.setText(book.getAuthor());
        holder.yearTextView.setText(String.valueOf(book.getYear()));
        holder.descriptionTextView.setText(book.getDescription());

        holder.crossButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setMessage("Are you sure, You wanted to delete this book?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteBook(itemPosition);
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
            public void onClick(View v) {
                BookUpdateDialogFragment bookUpdateDialogFragment = BookUpdateDialogFragment.newInstance(book.getBook_number(), itemPosition, new BookUpdateListener() {
                    @Override
                    public void onBookInfoUpdated(Book books, int position) {
                        bookList.set(position, books);
                        notifyDataSetChanged();
                    }
                });
                bookUpdateDialogFragment.show(((BookListActivity) context).getSupportFragmentManager(), DatabaseContract.UPDATE_BOOK);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewListActivity.class);
                intent.putExtra(DatabaseContract.BOOK_NUMBER, book.getBook_number());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    private void deleteBook(int position) {
        Book book = bookList.get(position);
        long count = databaseQueryClass.deleteBookByBookNum(book.getBook_number());

        if (count > 0) {
            bookList.remove(position);
            notifyDataSetChanged();
            ((BookListActivity) context).viewVisibility();
            Toast.makeText(context, "Book deleted successfully", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(context, "Book not deleted. Something wrong!", Toast.LENGTH_LONG).show();

    }

    public void setFilter(ArrayList<Book> newListBook) {
        bookList = new ArrayList<>();
        bookList.addAll(newListBook);
        notifyDataSetChanged();
    }
}
