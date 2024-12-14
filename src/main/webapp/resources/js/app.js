var stompClient = null;

function connect(username) {
    var socket = new SockJS('/hello');
    stompClient = Stomp.over(socket);
    stompClient.connect({ username: username, }, function () {
        console.log('Web Socket is connected');
        stompClient.subscribe('/users/queue/messages', function (message) {
            alert(message.body);
            if (message.body.includes('ban')) {
                window.location.href = '/logout';
            }
        });
    });
}
$(document).ready(function () {
    var currUsername = document.getElementById('hidden-username').value;
    if (currUsername !== '') {
        connect(currUsername);
    }
    $(document).on('click', '.button-like', function (e) {
        e.preventDefault();
        var postId = $(this).find('.btn-like').attr('id');
        var postOwner = $(this).closest('.card-body').find('.post-owner').text().trim().replace('@', '');
        likePost(postId, postOwner);
    });

    $('input[name="comment"]').keypress(function (event) {
        if (event.which === 13) {
            console.log('Enter pressed');
            var postId = $(this).attr('id');
            var comment = $(this).val();
            var owner = $(this).closest('.cmt-div').find('.post-owner').val();
            submitComment(postId, comment, owner);
        }
    });
});

function likePost(postId, owner) {
    var currUsername = document.getElementById('hidden-username').value;

    if (currUsername === '') {
        alert('Please login to like');
        return;
    }

    const likeBtn = $('#' + postId);
    const likeCount = likeBtn.parent().next();
    const liked = likeBtn.hasClass("like");
    $.ajax({
        url: '/likePost',
        method: 'POST',
        data: {
            id: postId,
            like: liked
        },
        dataType: 'json',
        success: function (data) {
            if (data.success) {
                if (liked) {
                    likeBtn.prop("src", "/images/dashboard/8324235_ui_essential_app_liked_icon.png");
                    likeBtn.addClass("liked");
                    likeBtn.removeClass("like");
                    likeCount.text(data.likeCount);
                    if (currUsername !== owner)
                        stompClient.send("/app/like", {}, owner);
                } else {
                    likeBtn.prop("src", "/images/dashboard/8324235_ui_essential_app_like_icon.png");
                    likeBtn.addClass("like");
                    likeBtn.removeClass("liked");
                    likeCount.text(data.likeCount);
                }
            }
        },
        error: function (xhr, status, error) {
            console.error('Error:', error);
        }
    });
}

function submitComment(postId, comment, owner) {
    var currUsername = document.getElementById('hidden-username').value;
    console.log(currUsername + " " + owner);
    if (currUsername === '') {
        alert('Please login to like');
        return;
    }
    event.preventDefault();

    $.ajax({
        url: '/createComment?postId=' + postId + '&comment=' + comment,
        type: 'POST',
        success: function (response) {
            var newCommentHtml = '<div style="display: flex; justify-content: space-between;">' +
                '<p><strong>' + response.username + ':</strong> ' + response.content + '</p>' +
                '<h6 name="date-comment" id="' + response.time + '">' + 'Just now' + '</h6>' +
                '</div>';
            $('.comments-section').prepend(newCommentHtml);
            $('input[name="comment"]').val('');
            if (currUsername !== owner)
                stompClient.send("/app/comment", {}, owner);
        },
        error: function (error) {
            console.error('Error submitting comment:', error);
        }
    });
}

function createNotiElement(message) {
    var notiElement = document.createElement('div');
    notiElement.classList.add('row', 'border', 'border-info', 'mb-3');

    var avtDiv = document.createElement('div');
    avtDiv.classList.add('col-sm-3');

    var avt = document.createElement('img');
    avt.src = '/images/avatar/default-avatar.png';
    avt.width = 50;

    var contentDiv = document.createElement('div');
    contentDiv.classList.add('col-sm-9');
    contentDiv.innerText = message;

    avtDiv.appendChild(avt);
    notiElement.appendChild(avtDiv);
    notiElement.appendChild(contentDiv);

    return notiElement;
}