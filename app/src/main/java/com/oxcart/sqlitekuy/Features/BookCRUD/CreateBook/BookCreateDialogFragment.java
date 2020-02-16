package com.oxcart.sqlitekuy.Features.BookCRUD.CreateBook;


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
public class BookCreateDialogFragment extends DialogFragment {
    private static BookCreateListener bookCreateListener;

    private EditText editBookNumber;
    private EditText editTitle;
    private EditText editAuthor;
    private EditText editYear;
    private EditText editDescrption;

    private Button btnCreate;
    private Button btnCancel;

    private long bookNumber = -1;
    private String titleString = "";
    private String authorString = "";
    private int yearString = 0;
    private String descrptionString = "";

    public BookCreateDialogFragment() {
        // Required empty public constructor
    }

    public static BookCreateDialogFragment newInstance(String titleDialog, BookCreateListener listener) {
        bookCreateListener = listener;
        BookCreateDialogFragment bookCreateDialogFragment = new BookCreateDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", "Add Book");
        bookCreateDialogFragment.setArguments(args);

        bookCreateDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);

        return bookCreateDialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_create_dialog, container, false);

        editBookNumber = view.findViewById(R.id.edit_text_book_number);
        editTitle = view.findViewById(R.id.edit_text_book_title);
        editAuthor = view.findViewById(R.id.edit_text_book_author);
        editYear = view.findViewById(R.id.edit_text_book_year);
        editDescrption = view.findViewById(R.id.edit_text_book_description);

        btnCreate = view.findViewById(R.id.btn_create);
        btnCancel = view.findViewById(R.id.btn_cancel);

        String title = getArguments().getString("title");
        getDialog().setTitle(title);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookNumber = Integer.parseInt(editBookNumber.getText().toString());
                titleString = editTitle.getText().toString();
                authorString = editAuthor.getText().toString();
                yearString = Integer.parseInt(editYear.getText().toString());
                descrptionString = editDescrption.getText().toString();

                Book book = new Book(-1, bookNumber, titleString, authorString, yearString, descrptionString);

                DatabaseQueryClass databaseQueryClass = new DatabaseQueryClass(getContext());

                long id = databaseQueryClass.insertBook(book);

                if (id > 0) {
                    book.setId(id);
                    bookCreateListener.onBookCreated(book);
                    getDialog().dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
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
