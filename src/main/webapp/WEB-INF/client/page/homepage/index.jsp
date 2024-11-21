<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <title>Social Network</title>
    <!-- inject:css -->
    <!-- endinject -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body>
<c:choose>
    <c:when test="${empty param.postId}">
        <div class="container-scroller">
            <c:if test="${empty pageContext.request.userPrincipal}">
                <jsp:include page="../layout/navbar.jsp"/>
            </c:if>
            <div class="container-fluid page-body-wrapper">
                <c:if test="${not empty pageContext.request.userPrincipal}">
                    <jsp:include page="../layout/sidebar.jsp"/>
                </c:if>
                <div class="main-panel">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="home-tab">
                                <!-- content-here -->
                                <jsp:include page="../layout/content-pane.jsp"></jsp:include>
                                <jsp:include page="../layout/create-post.jsp"></jsp:include>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:when>
    <c:otherwise>
        <div class="container post-detail-container row">
            <div class="col-sm-1">
                <jsp:include page="../layout/sidebar.jsp"></jsp:include>
            </div>
            <div class="col-sm-11 row">
                <div class="post-image col-sm-6 mb-5">
                    <img src="/images/post/${post.image}" class="img-fluid" alt="Post Image">
                </div>
                <div class="container col-sm-6 position-relative overflow-auto " style="max-height: 400px">
                    <h1>${post.title}</h1>
                    <hr>
                    <h3>Comments</h3>
                    <c:forEach items="${comments}" var="comment">
                        <div class="d-flex justify-content-between fs-6">
                            <p>
                                <a class="text-decoration-none text-black" href="/profile/${comment.account.username}">
                                    <img class="pe-2 rounded-circle" height="50" width="50" alt="avatar"
                                         src="/images/avatar/${comment.account.avatar}"/>
                                    <strong>${comment.account.username}:</strong>
                                </a>${comment.content}
                            </p>
                            <h6 name="date-comment" id="${comment.date}">${comment.timeAgo}</h6>
                        </div>
                    </c:forEach>
                    <div class="position-absolute bottom-0 pb-5 col-12">
                        <form:form action="/comment" method="post">
                            <input type="hidden" name="postId" value="${post.id}">
                            <div class="form-group">
                                <input type="text" class="form-control" name="comment" id="${post.id}"
                                       rows="3" placeholder="Write a comment"></input>
                            </div>
                        </form:form>
                    </div>
                </div>
                <jsp:include page="../layout/content-pane.jsp"></jsp:include>
            </div>
        </div>
    </c:otherwise>
</c:choose>

<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"
        crossorigin="anonymous"></script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>

<!-- End custom js for this page-->

<script>
    function submitComment(postId) {
        const comment = document.getElementById('comment-' + postId).value;
        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
        console.log("CMt: " + comment);

        fetch('/createComment?postId=' + postId + '&comment=' + comment, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    console.log(data);
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

    function uploadImage(imgToUpload) {
        const img = document.getElementById('postPicture');
        const fileReader = new FileReader();

        const maxWidth = 800; // Maximum width you want for the image
        const maxHeight = 600; // Maximum height you want for the image

        fileReader.onload = function (event) {
            const tempImg = new Image();

            tempImg.onload = function () {
                let width = tempImg.width;
                let height = tempImg.height;

                const aspectRatio = width / height;

                if (width > maxWidth || height > maxHeight) {
                    if (aspectRatio > 1) {
                        width = maxWidth;
                        height = maxWidth / aspectRatio;
                    } else {
                        height = maxHeight;
                        width = maxHeight * aspectRatio;
                    }
                }

                img.src = event.target.result;
                img.width = width;
                img.height = height;
            };

            tempImg.src = event.target.result;
        };

        // Read the uploaded file as a data URL
        fileReader.readAsDataURL(imgToUpload);
    }

    function likePost(postId) {
        <c:if test="${empty pageContext.request.userPrincipal}">
        document.querySelector('#open-form-login').click();
        return;
        </c:if>
        const likeBtn = document.getElementById(postId);
        const likeCount = likeBtn.parentElement.nextElementSibling;
        let count = parseInt(likeCount.textContent);
        const liked = likeBtn.classList.contains("like");

        const csrfParameterName = '${_csrf.parameterName}';
        const csrfToken = '${_csrf.token}';

        fetch('/likePost?id=' + postId + '&like=' + liked, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    if (liked) {
                        likeBtn.src = "/images/dashboard/8324235_ui_essential_app_liked_icon.png";
                        likeBtn.classList.remove("like");
                        likeBtn.classList.add("liked");
                        likeCount.textContent = data.likeCount;
                    } else {
                        likeBtn.src = "/images/dashboard/8324235_ui_essential_app_like_icon.png";
                        likeBtn.classList.add("like");
                        likeBtn.classList.remove("liked");
                        likeCount.textContent = data.likeCount;
                    }
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }
</script>
</body>

</html>