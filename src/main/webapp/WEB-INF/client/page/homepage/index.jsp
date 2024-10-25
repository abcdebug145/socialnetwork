<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <!-- Required meta tags -->
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <title>Dashboard</title>
                <!-- inject:css -->
                <link rel="stylesheet" href="../css/style.css">
                <link rel="stylesheet" href="../css/custom.css">

                <!-- endinject -->
                <link rel="shortcut icon" href="/images/favicon.png" />
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"
                    integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
                    crossorigin="anonymous">
                <!-- Bootstrap Icons -->
                <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
                <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
                <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

            </head>

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
                            <div class="content-wrapper" style="margin-left: 260px;">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="home-tab">
                                            <!-- content-here -->
                                            <div class="container py-4">
                                                <div class="row" data-masonry='{"percentPosition": true }'>
                                                    <!-- Repeat this column structure for more items -->
                                                    <c:forEach items="${listPost}" var="post">
                                                        <div></div>
                                                        <div class="col-sm-6 col-lg-4 mb-4">
                                                            <div class="card">
                                                                <img src="images/post/${post.image}"
                                                                    class="card-img-top" alt="Placeholder image" />
                                                                <div class="card-body">
                                                                    <h5 class="card-title">${post.title}</h5>
                                                                    <div
                                                                        class="d-flex justify-content-between align-items-center">
                                                                        <div class="d-flex align-items-center">
                                                                            <button
                                                                                style="background: none; border: none; cursor: pointer;"
                                                                                onclick='likePost("${post.id}")'>
                                                                                <img src="/images/dashboard/8324235_ui_essential_app_like_icon.png"
                                                                                    width="30px" height="30px"
                                                                                    alt="Like" class="btn-like like"
                                                                                    id="${post.id}">
                                                                            </button>
                                                                            <h5
                                                                                style="margin-top: 10px; margin-left: 10px;">
                                                                                ${post.liked}
                                                                            </h5>
                                                                        </div>
                                                                        <button class="btn btn-outline-secondary btn-sm"
                                                                            onclick="downloadImg()">
                                                                            <i class="bi bi-download"></i> Download
                                                                        </button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </c:forEach>
                                                    <!-- Add more columns here -->
                                                </div>
                                            </div>
                                            <!-- popup form -->
                                            <div class="container py-4 popup-form" id="popupForm">
                                                <span class="close-btn" id="closePopupBtn"
                                                    style="font-size: 50px;">&times;</span>
                                                <div class="row">
                                                    <div class="col-md-8 col-sm-8">
                                                        <h3 class="my-4">New Post</h3>
                                                        <hr />
                                                        <form action="/">
                                                            <div class="row mb-3">
                                                                <label for="title"
                                                                    class="col-sm-3 col-form-label">Title:</label>
                                                                <div class="col-sm-9">
                                                                    <input type="text" class="form-control"
                                                                        id="title" />
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
                                                                        onchange="verifyFileUpload(this)"
                                                                        style="display: none" />
                                                                    <button class="btn btn-outline-primary my-3"
                                                                        type="button"
                                                                        onclick="document.querySelector('#fileChooser').click()">
                                                                        Upload Picture
                                                                    </button>
                                                                </div>
                                                                <div class="d-grid gap-2">
                                                                    <button type="submit" name="" id=""
                                                                        class="btn btn-primary">
                                                                        Post
                                                                    </button>
                                                                </div>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                            <!-- Overlay -->
                                            <div id="overlay" class="overlay"></div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <script src="/js/custom.js"></script>
                <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
                    integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
                    crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js"
                    integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
                    crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js"
                    integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
                    crossorigin="anonymous"></script>
                <!-- End custom js for this page-->
                <script>
                    function likePost(postId) {
                        const likeBtn = document.getElementById(postId);
                        const likeCount = likeBtn.parentElement.nextElementSibling;
                        let count = parseInt(likeCount.textContent);
                        const liked = likeBtn.classList.contains("like");
                        console.log('/likePost?id=' + postId + '&like=' + liked);
                        fetch('/likePost?id=' + postId + '&like=' + liked, {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            }
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
                                        likeCount.textContent = count + 1;
                                    } else {
                                        likeBtn.src = "/images/dashboard/8324235_ui_essential_app_like_icon.png";
                                        likeBtn.classList.add("like");
                                        likeBtn.classList.remove("liked");
                                        likeCount.textContent = count - 1;
                                    }
                                }
                            })
                            .catch(error => {
                                console.error('Error:', error);
                            });
                    }
                    // JavaScript to handle popup
                    const openPopupBtn = document.getElementById("openPopupBtn");
                    const popupForm = document.getElementById("popupForm");
                    const overlay = document.getElementById("overlay");
                    const closePopupBtn = document.getElementById("closePopupBtn");

                    // Open popup when button is clicked
                    openPopupBtn.addEventListener("click", function () {
                        popupForm.style.display = "block";
                        overlay.style.display = "block";
                    });

                    // Close popup when close button is clicked
                    closePopupBtn.addEventListener("click", function () {
                        popupForm.style.display = "none";
                        overlay.style.display = "none";
                    });

                    // Close popup when clicking outside of the form (on overlay)
                    overlay.addEventListener("click", function () {
                        popupForm.style.display = "none";
                        overlay.style.display = "none";
                    });
                </script>
            </body>

            </html>