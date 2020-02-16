package com.oxcart.sqlitekuy.Features.ReviewCRUD.UpdateReviewInfo;

import com.oxcart.sqlitekuy.Features.ReviewCRUD.CreateReview.Review;

public interface ReviewUpdateListener {
    void onReviewInfoUpdate(Review review, int position);
}
