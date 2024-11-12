<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

                <!DOCTYPE html>
                <html lang="en">

                <head>
                    <!-- Required meta tags -->
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                    <meta name="_csrf" content="${_csrf.token}" />
                    <meta name="_csrf_header" content="${_csrf.headerName}" />
                    <title>Social Network</title>
                    <!-- inject:css -->
                    <!-- endinject -->
                    <link rel="shortcut icon" href="/images/favicon.png" />
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                        rel="stylesheet"
                        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                        crossorigin="anonymous">
                    <!-- Bootstrap Icons -->
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
                    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
                </head>

                <body class="with-welcome-text">
                    <div class="container-scroller">
                        <c:if test="${empty pageContext.request.userPrincipal}">
                            <jsp:include page="../layout/navbar.jsp" />
                        </c:if>
                        <div class="container-fluid page-body-wrapper">
                            <c:if test="${not empty pageContext.request.userPrincipal}">
                                <jsp:include page="../layout/sidebar.jsp" />
                            </c:if>
                            <div class="main-panel">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="home-tab">
                                            <!-- content-here -->
                                            <div class="container grid">
                                                <div class="row d-flex justify-content-center"
                                                    data-masonry='{"percentPosition": true }'>
                                                    <!-- Card 1 -->
                                                    <c:forEach items="${listPost}" var="post">
                                                        <div class="col-md-4 mb-4">
                                                            <div class="card">
                                                                <c:choose>
                                                                    <c:when
                                                                        test="${not fn:contains(post.image, 'http')}">
                                                                        <img src="images/post/${post.image}"
                                                                            class="card-img-top"
                                                                            alt="Placeholder image">
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <img src="${post.image}" class="card-img-top"
                                                                            alt="Placeholder image">
                                                                    </c:otherwise>
                                                                </c:choose>

                                                                <div class="card-body">
                                                                    <h5 class="card-title">${post.title}</h5>
                                                                    <div style="font-size: 12px;">
                                                                        by @<a href="#"
                                                                            style="text-decoration: none;">${post.account.username}</a>
                                                                    </div>
                                                                    <hr>
                                                                    <div
                                                                        class="d-flex justify-content-between align-items-center">
                                                                        <div class="d-flex align-items-center">
                                                                            <button
                                                                                style="background: none; border: none; cursor: pointer;"
                                                                                onclick='likePost("${post.id}")'>
                                                                                <c:set var="liked" value="false" />
                                                                                <c:forEach var="i" items="${postLiked}">
                                                                                    <c:if
                                                                                        test="${i.post.id == post.id}">
                                                                                        <c:set var="liked"
                                                                                            value="true" />
                                                                                    </c:if>
                                                                                </c:forEach>
                                                                                <c:choose>
                                                                                    <c:when test="${liked}">
                                                                                        <img src="/images/dashboard/8324235_ui_essential_app_liked_icon.png"
                                                                                            width="30px" height="30px"
                                                                                            alt="Liked"
                                                                                            class="btn-like liked"
                                                                                            id="${post.id}">
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <img src="/images/dashboard/8324235_ui_essential_app_like_icon.png"
                                                                                            width="30px" height="30px"
                                                                                            alt="Like"
                                                                                            class="btn-like like"
                                                                                            id="${post.id}">
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                            </button>
                                                                            <h5 class="pt-1 ps-1">
                                                                                ${post.likeCount}
                                                                            </h5>
                                                                        </div>
                                                                        <button class="btn btn-outline-secondary btn-sm"
                                                                            onclick="downloadImg()">
                                                                            <i class="bi bi-download"></i> Download
                                                                        </button>
                                                                    </div>
                                                                    <div>
                                                                        <input type="text"
                                                                            placeholder="Say something about that picture!"
                                                                            id="comment-${post.id}" name="comment">
                                                                        <button class="btn btn-primary" type="button"
                                                                            onclick='submitComment("${post.id}")'>
                                                                            Comment
                                                                        </button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                </div>
                                            </div>

                                            <!-- popup form -->
                                            <div class="modal" id="CreatePostModal" tabindex="-1"
                                                aria-labelledby="exampleModalLabel" aria-hidden="true">
                                                <div class="modal-dialog modal-xl">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h3 class="my-4 model-title">Create Post</h3>
                                                        </div>
                                                        <form:form action="/create-post" method="post"
                                                            enctype="multipart/form-data" modelAttribute="newPost">
                                                            <div class="modal-body">
                                                                <div class="row mb-3">
                                                                    <label for="title"
                                                                        class="col-sm-3 col-form-label">Title:</label>
                                                                    <div class="col-sm-9">
                                                                        <form:input type="text" class="form-control"
                                                                            id="title" path="title" />
                                                                    </div>
                                                                </div>
                                                                <div class="row mb-3">
                                                                    <label for="content"
                                                                        class="col-sm-3 col-form-label">Picture:</label>
                                                                </div>
                                                                <div class="row mb-3">
                                                                    <img src="" id="postPicture" />
                                                                    <div class="col-sm-9">
                                                                        <input type="file" id="fileChooser"
                                                                            onchange="uploadImage(this.files[0])"
                                                                            accept="image/*" style="display: none"
                                                                            name="postFile" />
                                                                        <button class="btn btn-outline-primary my-3"
                                                                            type="button"
                                                                            onclick="document.querySelector('#fileChooser').click()">
                                                                            Upload Picture
                                                                        </button>
                                                                    </div>
                                                                </div>
                                                                <div class="d-grid gap-2">
                                                                    <button type="submit" name="" id=""
                                                                        class="btn btn-primary">Post
                                                                    </button>
                                                                </div>
                                                        </form:form>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Notification Popup -->

                                            <!-- Overlay -->
                                            <div id="overlay" class="overlay"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <script src="https://cdn.jsdelivr.net/npm/masonry-layout@4.2.2/dist/masonry.pkgd.min.js"
                        async></script>
                    <script src="https://code.jquery.com/jquery-3.3.1.min.js"
                        integrity="sha384-tsQFqpERiu9W1NUMXXfO5pLnAM6ScJS/6aINHpqlnqu8qZWjW0k6ujUib3WTOE6s"
                        crossorigin="anonymous"></script>
                    <script src="https://unpkg.com/imagesloaded@4.1.4/imagesloaded.pkgd.min.js"></script>


                    <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"
                        crossorigin="anonymous"></script>

                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
                        crossorigin="anonymous"></script>

                    <!-- End custom js for this page-->
                    <script>
                        document.addEventListener("DOMContentLoaded", function () {
                            const grid = document.querySelector('.grid .row');

                            imagesLoaded(grid, function () {
                                new Masonry(grid, {
                                    itemSelector: '.col-md-4',
                                    percentPosition: true,
                                    columnWidth: '.col-md-4'
                                });
                            });
                        });

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