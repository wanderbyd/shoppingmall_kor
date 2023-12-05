/*!
 * Bootstrap v3.0.3 (http://getbootstrap.com)
 * Copyright 2013 Twitter, Inc.
 * Licensed under http://www.apache.org/licenses/LICENSE-2.0
 */

function addReview() {
        var userId = document.getElementById("userId").value;
        var contents = document.getElementById("contents").value;

        // 리뷰 데이터를 JSON 형식으로 생성
        var reviewData = {
            userId: userId,
            contents: contents
        };

        // AJAX를 사용하여 리뷰를 추가하고, 성공한 경우 목록을 업데이트
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/addReview",
            data: JSON.stringify(reviewData),
            success: function (review) {
                // 성공적으로 리뷰가 추가되면 리뷰 목록을 업데이트
                updateReviewList(review);
            },
            error: function (error) {
                console.log("Failed to add review:", error);
            }
        });
    }

    function updateReviewList(review) {
        // 리뷰 목록을 업데이트
        var reviewList = document.getElementById("reviewList");

        var reviewItem = document.createElement("li");
        var reviewUser = document.createElement("p");
        var reviewContent = document.createElement("p");

        reviewUser.innerHTML = "리뷰 유저: " + review.userId;
        reviewContent.innerHTML = "리뷰 내용: " + review.contents;

        reviewItem.appendChild(reviewUser);
        reviewItem.appendChild(reviewContent);

        reviewList.appendChild(reviewItem);
    }



















