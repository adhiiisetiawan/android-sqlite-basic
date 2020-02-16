package com.oxcart.sqlitekuy.Features.BookCRUD.UpdateBookInfo;


import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.oxcart.sqlitekuy.Features.BookCRUD.CreateBook.Book;
import com.oxcart.sqlitekuy.R;
import com.oxcart.sqlitekuy.dbHelper.DatabaseContract;
import com.oxcart.sqlitekuy.dbHelper.DatabaseQueryClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookUpdateDialogFragment extends DialogFragment {
    private static long bookNum;
    private static int bookItemPosition;
    private static BookUpdateListener bookUpdateListener;

    private Book mBook;

    private EditText bookNumEditText;
    private EditText titleEditText;
    private EditText authorEditText;
    private EditText yearEditText;
    private EditText descriptionEditText;

    private Button updateButton;
    private Button cancelButton;

    private long bookNumber = -1;
    private String titleString = "";
    private String authorString = "";
    private int yearString = 0;
    private String descrptionString = "";

    private DatabaseQueryClass databaseQueryClass;

    public BookUpdateDialogFragment() {
        // Required empty public constructor
    }

    public static BookUpdateDialogFragment newInstance(long bookNumbers, int position, BookUpdateListener listener){
        bookNum = bookNumbers;
        bookItemPosition = position;
        bookUpdateListener = listener;
        BookUpdateDialogFragment bookUpdateDialogFragment = new BookUpdateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Update book information");
        bookUpdateDialogFragment.setArguments(args);

        bookUpdateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return bookUpdateDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_update_dialog, container, false);

        databaseQueryClass = new DatabaseQueryClass(getContext());

        bookNumEditText = view.findViewById(R.id.edit_text_book_number);
        titleEditText = view.findViewById(R.id.edit_text_book_title);
        authorEditText = view.findViewById(R.id.edit_text_book_author);
        yearEditText = view.findViewById(R.id.edit_text_book_year);
        descriptionEditText = view.findViewById(R.id.edit_text_book_description);

        updateButton = view.findViewById(R.id.updateStudentInfoButton);
        cancelButton = view.findViewById(R.id.cancelButton);

        String title = getArguments().getString(DatabaseContract.TITLE);
        getDialog().setTitle(title);

        mBook = databaseQueryClass.getBookByBookNumber(bookNum);

        if(mBook!=null){
            bookNumEditText.setText(String.valueOf(mBook.getBook_number()));
            titleEditText.setText(mBook.getTitle());
            authorEditText.setText(mBook.getAuthor());
            yearEditText.setText(String.valueOf(mBook.getYear()));
            descriptionEditText.setText(mBook.getDescription());

            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookNumber = Integer.parseInt(bookNumEditText.getText().toString());
                    titleString = titleEditText.getText().toString();
                    authorString = authorEditText.getText().toString();
                    yearString = Integer.parseInt(yearEditText.getText().toString());
                    descrptionString = descriptionEditText.getText().toString();

                    mBook.setBook_number(bookNumber);
                    mBook.setTitle(titleString);
                    mBook.setAuthor(authorString);
                    mBook.setYear(yearString);
                    mBook.setDescription(descrptionString);

                    long id = databaseQueryClass.updateBookInfo(mBook);

                    if(id>0){
                        bookUpdateListener.onBookInfoUpdated(mBook, bookItemPosition);
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

        }
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
