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
                <%-- <link rel="stylesheet" href="../css/style.css">--%>
                    <%-- <link rel="stylesheet" href="../css/custom.css">--%>

                        <!-- endinject -->
                        <link rel="shortcut icon" href="/images/favicon.png" />
                        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                            rel="stylesheet"
                            integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                            crossorigin="anonymous">
                        <!-- Bootstrap Icons -->
                        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css"
                            rel="stylesheet">
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
                            <div class="content-wrapper">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="home-tab">
                                            <!-- content-here -->
                                            <div class="container py-4">
                                                <div class="row d-flex justify-content-center"
                                                    data-masonry='{"percentPosition": true }'>
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
                                                                                onclick="handleLike()">
                                                                                <img src="/images/dashboard/8324235_ui_essential_app_like_icon.png"
                                                                                    width="30px" height="30px"
                                                                                    alt="Like" class="btn-like like">
                                                                            </button>
                                                                            <h5
                                                                                style="margin-top: 10px; margin-left: 10px;">
                                                                                ${post.liked}</h5>
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
                                            <div class="modal" id="CreatePostModal" tabindex="-1"
                                                aria-labelledby="exampleModalLabel" aria-hidden="true">
                                                <div class="modal-dialog modal-xl">
                                                    <div class="modal-content">
                                                        <div class="modal-header">
                                                            <h3 class="my-4 model-title">Create Post</h3>
                                                        </div>
                                                        <div class="modal-body">
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
                                                            </div>
                                                            <div class="d-grid gap-2">
                                                                <button type="button" name="" id=""
                                                                    class="btn btn-primary">Post
                                                                </button>
                                                            </div>
                                                        </div>
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

                <script src="https://code.jquery.com/jquery-3.3.1.min.js"
                    integrity="sha384-tsQFqpERiu9W1NUMXXfO5pLnAM6ScJS/6aINHpqlnqu8qZWjW0k6ujUib3WTOE6s"
                    crossorigin="anonymous"></script>

                <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"
                    crossorigin="anonymous"></script>

                <script src="/js/custom.js"></script>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
                    integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
                    crossorigin="anonymous"></script>
                <!-- End custom js for this page-->
                <script>
                    function handleLike() {
                        var like = document.querySelector('.btn-like');
                        if (like.classList.contains('liked')) {
                            like.src = "/images/dashboard/8324235_ui_essential_app_like_icon.png";
                            like.classList.remove('liked');
                            like.classList.add('like');
                        } else {
                            like.src = "/images/dashboard/8324235_ui_essential_app_liked_icon.png";
                            like.classList.remove('like');
                            like.classList.add('liked');
                        }
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