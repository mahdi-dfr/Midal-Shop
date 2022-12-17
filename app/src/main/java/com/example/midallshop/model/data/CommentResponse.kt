package com.example.midallshop.model.data


import com.google.gson.annotations.SerializedName

data class CommentResponse(
    @SerializedName("comments")
    var comments: List<Comment>,
    @SerializedName("success")
    var success: Boolean,
)

data class Comment(
    @SerializedName("commentId")
    var commentId: String,
    @SerializedName("text")
    var text: String,
    @SerializedName("userEmail")
    var userEmail: String,
)

data class AddCommentResponse(
    @SerializedName("success")
    var success : Boolean,
    @SerializedName("message")
    var message : String
)
