var projectName = document.title.split("/")[0];
var taskName = document.title.split("/")[1];
var task;
var status = $("#status");
var description = $("#description");
var developers = $("developers");
var comments = $("comments");
var commentsCount;

function loadTask(){
    $.get("http://localhost:8080/api/manager/projects/" + projectName + "/tasks/" + taskName, function(response, status){
        task = response;
        $.get("http://localhost:8080/api/manager/projects/" + projectName + "/tasks/" + taskName + "/comments", function(response, status){
            task["comments"] = response;
            commentsCount = response.length;
            showTaskOnPage();
        });
    });
}

function showTaskOnPage(){
    $("#status").val(task.status);

    description.append(task.description);

    task.developers.forEach(function(element, index, array){
        developers.append("<a href='#'>" + element.username + "</a>  ")
    });

    $("#comments-count").empty();
    $("#comments-count").append(commentsCount);

    task.comments.forEach(function(element, index, array){
        displayComment(element);
    });
}

function displayComment(comment){
    var media = $("<div></div>", {
        "class": "media"
    });
    var postedAt = $("<small></small>",{
    });
    var date = new Date(comment.postedAt);
    postedAt.append(date);
    var p1 = $("<p></p>", {
        "class": "pull-right"
    });
    var innerDiv = $("<div></div>", {
        "class": "media-body"
    });
    var h4 = $("<h4></h4>", {
        "class": "media-heading username"
    });
    h4.append(comment.author.name);
    var commentText = $("<div></div>", {
        id: comment.id
    });
    commentText.append(comment.text);
    var p2 = $("<p></p>", {});
    var small = $("<small></small>", {});
    var editLink = $("<a></a>", {
        "data-id": comment.id,
        href: "#"
    });
    editLink.append("Edit");
    editLink.click(editComment);
    var deleteLink = $("<a></a>", {
        "data-id": comment.id,
        href: "#"
    });
    deleteLink.append("Delete");
    deleteLink.click(deleteComment);
    small.append(editLink);
    small.append("   ");
    small.append(deleteLink);
    p2.append(small);
    innerDiv.append(h4);
    innerDiv.append(commentText);
    innerDiv.append(p2);
    p1.append(postedAt);
    media.append(p1);
    media.append(innerDiv);
    $("#comments").append(media);
}

function editComment(){
    if($(this).text() == "Edit"){
        var commentEditBox = $("<input>", {
            id: $(this).data("id"),
            "class": "form-control"
        });
        commentEditBox.val($("#" + $(this).data("id")).text());
        $("#" + $(this).data("id")).replaceWith(commentEditBox);

        $(this).html("Save");
    } else {
        var commentTextBox = $("<div></div>", {
            id: $(this).data("id")
        });
        commentTextBox.append($("#" + $(this).data("id")).val());
        $("#" + $(this).data("id")).replaceWith(commentTextBox);

        saveComment($(this).data("id"), $("#" + $(this).data("id")).text());

        $(this).html("Edit");
    }
}

function saveComment(commentId, commentText){
    var commentDto = {};
    commentDto["id"] = commentId;
    commentDto["text"] = commentText;
    $.ajax({
        url: "http://localhost:8080/api/manager/projects/" + projectName + "/tasks/" + taskName + "/comments/edit",
        type: "PUT",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(commentDto),
        success: function () {
        }
    })
}

function deleteComment(){
    $.ajax({
        url: "http://localhost:8080/api/manager/projects/" + projectName + "/tasks/" + taskName + "/comments/delete/" + $(this).data("id"),
        type: "DELETE",
        success: function () {
            location.reload();
        }
    })
}

function createComment(){
    var commentDto = {};
    commentDto["text"] = $("#comment-text").val();
    $.post({
        url: "http://localhost:8080/api/manager/projects/" + projectName + "/tasks/" + taskName + "/comments/create",
        type: "post",
        data: JSON.stringify(commentDto),
        dataType: "json",
        contentType: "application/json"
    }, function(response){
        if(response != null){
            commentsCount++;
            $("#comments-count").empty();
            $("#comments-count").append(commentsCount);
            displayComment(response);
        }
    })
}

$("#post-comment").click(function(){
    createComment();
})

$("#status").change(function(){
    $("#save-status").removeAttr("disabled");
});

$("#save-status").click(function(){
    var statusDto = {};
    statusDto["status"] = $("#status").val();
    $.ajax({
        url: "http://localhost:8080/api/manager/projects/" + projectName + "/tasks/" + taskName + "/status/edit",
        type: "PUT",
        contentType: "application/json",
        dataType: "json",
        data: JSON.stringify(statusDto),
        success: function () {
            $("#save-status").attr("disabled", "disabled");
        }
    })
});

loadTask();