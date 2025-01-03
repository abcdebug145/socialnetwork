<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <!-- Required meta tags -->
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
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
                <input type="hidden" value="${currAccount.username}" id="hidden-username">
                <div class="container-scroller">
                    <jsp:include page="../layout/navbar.jsp" />
                    <div class="container-fluid page-body-wrapper">
                        <c:if test="${not empty pageContext.request.userPrincipal}">
                            <jsp:include page="../layout/sidebar.jsp" />
                        </c:if>
                        <div class="main-panel">
                            <div class="row">
                                <div class="col-sm-12">
                                    <div class="home-tab">
                                        <!-- content-here -->
                                        <jsp:include page="../layout/content-pane.jsp"></jsp:include>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <jsp:include page="../layout/report-post.jsp" />
                <script src="https://code.jquery.com/jquery-3.3.1.min.js"
                    integrity="sha384-tsQFqpEReu7ZLhBV2VZlAu7zcOV+rXbYlF2cqB8txI/8aZajjp4Bqd+V6D5IgvKT"
                    crossorigin="anonymous"></script>
                <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
                <script src="/webjars/jquery/dist/jquery.min.js"></script>
                <script src="/webjars/sockjs-client/sockjs.min.js"></script>
                <script src="/webjars/stomp-websocket/stomp.min.js"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <script src="/js/app.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
                <script>
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

                </script>
                <script>
                    $(document).ready(function () {
                        let isLoading = false;
                        let currentPage = 1;
                        let hasMorePosts = true;
                        let postLiked = [];

                        function loadMorePosts() {
                            $.ajax({
                                url: '/loadMorePosts',
                                type: 'GET',
                                data: {
                                    page: currentPage,
                                    size: 40
                                },
                                dataType: 'json',
                                success: function (posts) {
                                    if (posts.length > 0) {
                                        currentPage++;
                                        posts.forEach(function (post) {
                                            let isLiked = false;
                                            for (let i = 0; i < postLiked.length; i++) {
                                                if (post.id === postLiked[i].post.id) {
                                                    isLiked = true;
                                                    postLiked.splice(i, 1);
                                                    break;
                                                }
                                            }
                                            var postHtml = createPostElement(post, isLiked);
                                            $('#post-container').append(postHtml);
                                        });
                                    } else {
                                        hasMorePosts = false;
                                    }
                                    isLoading = false;
                                },
                                error: function (error) {
                                    console.error('Error loading more posts:', error);
                                    isLoading = false;
                                }
                            });
                        }

                        $(window).scroll(function () {
                            if (!isLoading && hasMorePosts &&
                                $(window).scrollTop() + $(window).height() >= $(document).height() - 100) {
                                isLoading = true;
                                loadMorePosts();
                            }
                        });
                        getPostLiked().then(data => {
                            data.forEach(function (item) {
                                postLiked.push(item);
                            });
                        });
                        loadMorePosts();
                    });

                    function getPostLiked() {
                        return fetch('/getPostLikedByAccount', {
                            method: 'GET',
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
                                return data;
                            })
                            .catch(error => {
                                console.error('Error:', error);
                            }
                            );
                        // return data;
                    }

                    function createPostElement(post, isLiked) {
                        // Create the parent element
                        const postElement = document.createElement("div");
                        postElement.className = "col-md-4 mb-4";

                        // Create card
                        const cardDiv = document.createElement("div");
                        cardDiv.className = "card";

                        // Create link
                        const linkElement = document.createElement("a");
                        linkElement.href = `/post/` + post.id;

                        // Image
                        const imgElement = document.createElement("img");
                        imgElement.src = `/images/post/` + post.image;
                        imgElement.className = "card-img-top";
                        imgElement.alt = `This post may contain ` + post.content;

                        // Card body
                        const cardBodyDiv = document.createElement("div");
                        cardBodyDiv.className = "card-body";

                        // Title
                        const titleLink = document.createElement("a");
                        titleLink.href = `/post/` + post.id;
                        titleLink.style.textDecoration = "none";

                        const titleElement = document.createElement("h5");
                        titleElement.className = "card-title";
                        titleElement.textContent = post.title;
                        titleLink.appendChild(titleElement);

                        // Author
                        const authorDiv = document.createElement("div");
                        authorDiv.style.fontSize = "12px";

                        const authorLink = document.createElement("a");
                        authorLink.classList.add("post-owner");
                        authorLink.href = "/profile/" + post.account.username;
                        authorLink.style.textDecoration = "none";
                        authorLink.textContent = "@" + post.account.username;
                        authorDiv.innerHTML = "by ";
                        authorDiv.appendChild(authorLink);

                        // Horizontal line
                        const hrElement = document.createElement("hr");

                        // Like section
                        const likeSectionDiv = document.createElement("div");
                        likeSectionDiv.className = "d-flex justify-content-between align-items-center";

                        const likeContainerDiv = document.createElement("div");
                        likeContainerDiv.className = "d-flex align-items-center";

                        // Like button
                        const likeButton = document.createElement("a");
                        likeButton.style.background = "none";
                        likeButton.style.border = "none";
                        likeButton.style.cursor = "pointer";
                        likeButton.className = "button-like";

                        const likeImg = document.createElement("img");
                        likeImg.width = 30;
                        likeImg.height = 30;
                        likeImg.id = post.id;
                        likeImg.className = isLiked ? "btn-like liked" : "btn-like like";
                        likeImg.src = isLiked
                            ? "/images/dashboard/8324235_ui_essential_app_liked_icon.png"
                            : "/images/dashboard/8324235_ui_essential_app_like_icon.png";
                        likeImg.alt = isLiked ? "Liked" : "Like";

                        likeButton.appendChild(likeImg);

                        // Like count
                        const likeCountElement = document.createElement("h5");
                        likeCountElement.className = "pt-1 ps-1";
                        likeCountElement.textContent = post.likeCount;

                        likeContainerDiv.appendChild(likeButton);
                        likeContainerDiv.appendChild(likeCountElement);

                        // Download button
                        const downloadButton = document.createElement("button");
                        downloadButton.className = "btn btn-outline-secondary btn-sm";
                        downloadButton.setAttribute("onclick", "downloadImg()");
                        downloadButton.innerHTML = '<i class="bi bi-download"></i> Download';

                        // Report button
                        const reportButton = document.createElement("button");
                        reportButton.className = "btn btn-outline-danger btn-sm report-button";
                        reportButton.setAttribute("data-post-id", post.id);
                        reportButton.setAttribute("data-post-title", post.title);
                        imgpath = `/images/post/` + post.image;
                        reportButton.setAttribute("data-post-image", imgpath);
                        reportButton.innerHTML = '<i class="bi bi-flag"></i> Report';

                        // Action section
                        const actionSectionDiv = document.createElement("div");
                        actionSectionDiv.className = "d-flex justify-content-between align-items-center";
                        actionSectionDiv.appendChild(downloadButton);
                        if (document.getElementById("hidden-username").value !== '' &&
                            document.getElementById("hidden-username").value !== post.account.username) {
                            actionSectionDiv.appendChild(reportButton);
                        }
                        
                        // Combine sections
                        likeSectionDiv.appendChild(likeContainerDiv);
                        likeSectionDiv.appendChild(actionSectionDiv);

                        cardBodyDiv.appendChild(titleLink);
                        cardBodyDiv.appendChild(authorDiv);
                        cardBodyDiv.appendChild(hrElement);
                        cardBodyDiv.appendChild(likeSectionDiv);

                        linkElement.appendChild(imgElement);

                        cardDiv.appendChild(linkElement);
                        cardDiv.appendChild(cardBodyDiv);

                        postElement.appendChild(cardDiv);
                        return postElement;
                    }
                    
                    document.addEventListener("DOMContentLoaded", () => {
                        document.body.addEventListener("click", (event) => {
                            if (event.target.closest(".report-button")) {
                                const reportButton = event.target.closest(".report-button");
                                const postId = reportButton.getAttribute("data-post-id");
                                const postTitle = reportButton.getAttribute("data-post-title");
                                const postImage = reportButton.getAttribute("data-post-image");

                                // Populate modal fields
                                document.getElementById("reportModalPostId").value = postId;
                                document.getElementById("reportModalTitle").textContent = postTitle;
                                document.getElementById("reportModalImage").src = postImage;

                                // Show the modal
                                const reportModal = new bootstrap.Modal(document.getElementById("reportModal"));
                                reportModal.show();
                            }
                        });
                    });


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
                </script>
            </body>

            </html>
