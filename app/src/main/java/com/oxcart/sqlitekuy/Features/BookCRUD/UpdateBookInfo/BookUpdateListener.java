package com.oxcart.sqlitekuy.Features.BookCRUD.UpdateBookInfo;

import com.oxcart.sqlitekuy.Features.BookCRUD.CreateBook.Book;

public interface BookUpdateListener {
    void onBookInfoUpdated(Book book, int position);
}
