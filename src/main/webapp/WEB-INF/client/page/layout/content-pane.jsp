<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>

<head>
    <meta name="_csrf" content="${_csrf.token}">
    <meta name="_csrf.header" content="${_csrf.headerName}">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>

<body>
<div class="container grid">
    <div class="post-container">
        <div class="row d-flex justify-content-center" data-masonry='{"percentPosition": true }'>
            <c:forEach items="${listPost}" var="post">
                <a href="/?postId=${post.id}">
                    <div class="col-md-4 mb-4">
                        <div class="card">
                            <img src="/images/post/${post.image}" class="card-img-top"
                                 alt="This post may be contains ${post.content}">
                            <div class="card-body">
                                <a href="/?postId=${post.id}" style="text-decoration: none;">
                                    <h5 class="card-title">${post.title}</h5>
                                </a>
                                <div style="font-size: 12px;">
                                    by @<a href="#"
                                           style="text-decoration: none;">${post.account.username}</a>
                                </div>
                                <hr>
                                <div class="d-flex justify-content-between align-items-center">
                                    <div class="d-flex align-items-center">
                                        <button style="background: none; border: none; cursor: pointer;"
                                                onclick='likePost("${post.id}")'>
                                            <c:set var="liked" value="false"/>
                                            <c:forEach var="i" items="${postLiked}">
                                                <c:if test="${i.post.id == post.id}">
                                                    <c:set var="liked" value="true"/>
                                                </c:if>
                                            </c:forEach>
                                            <c:choose>
                                                <c:when test="${liked}">
                                                    <img src="/images/dashboard/8324235_ui_essential_app_liked_icon.png"
                                                         width="30px" height="30px" alt="Liked"
                                                         class="btn-like liked" id="${post.id}">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="/images/dashboard/8324235_ui_essential_app_like_icon.png"
                                                         width="30px" height="30px" alt="Like"
                                                         class="btn-like like" id="${post.id}">
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
                            </div>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/masonry-layout@4.2.2/dist/masonry.pkgd.min.js"
        async></script>
<script src="https://unpkg.com/imagesloaded@4.1.4/imagesloaded.pkgd.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script>
    $(document).ready(function () {
        var page = 0;
        var size = 40; // Number of posts to load per request

        function loadMorePosts() {
            $.ajax({
                url: '/loadMorePosts',
                type: 'GET',
                data: {
                    page: page,
                    size: size
                },
                dataType: 'json',
                success: function (posts) {
                    if (posts.length > 0) {
                        page++;
                        posts.forEach(function (post) {
                            // var liked = false;
                            // postLiked.forEach(function (i) {
                            //     if (i.post.id === post.id) {
                            liked = true;
                            //     }
                            // });

                            var likeIcon = liked ?
                                '/images/dashboard/8324235_ui_essential_app_liked_icon.png' :
                                '/images/dashboard/8324235_ui_essential_app_like_icon.png';

                            var postHtml = createPostElement(post, liked);
                            $('.posts-container').append(postHtml);
                        });
                    }
                },
                error: function (error) {
                    console.error('Error loading more posts:', error);
                }
            });
        }

        $(window).scroll(function () {
            if ($(window).scrollTop() + $(window).height() >= $(document).height()) {
                loadMorePosts();
            }
        });

        // Initial load
        loadMorePosts();
    });

    function createPostElement(post, postLiked) {
        // Kiểm tra xem post có được like hay không
        const isLiked = postLiked.some(liked => liked.post.id === post.id);

        // Tạo element cha
        const postElement = document.createElement('a');
        postElement.href = `/?postId=${post.id}`;

        // Tạo column
        const colDiv = document.createElement('div');
        colDiv.className = 'col-md-4 mb-4';

        // Tạo card
        const cardDiv = document.createElement('div');
        cardDiv.className = 'card';

        // Hình ảnh
        const imgElement = document.createElement('img');
        imgElement.src = `/images/post/${post.image}`;
        imgElement.className = 'card-img-top';
        imgElement.alt = `This post may be contains ${post.content}`;

        // Card body
        const cardBodyDiv = document.createElement('div');
        cardBodyDiv.className = 'card-body';

        // Tiêu đề
        const titleLink = document.createElement('a');
        titleLink.href = `/?postId=${post.id}`;
        titleLink.style.textDecoration = 'none';

        const titleElement = document.createElement('h5');
        titleElement.className = 'card-title';
        titleElement.textContent = post.title;
        titleLink.appendChild(titleElement);

        // Tác giả
        const authorDiv = document.createElement('div');
        authorDiv.style.fontSize = '12px';

        const authorLink = document.createElement('a');
        authorLink.href = '#';
        authorLink.style.textDecoration = 'none';
        authorLink.textContent = `@${post.account.username}`;

        authorDiv.innerHTML = 'by ';
        authorDiv.appendChild(authorLink);

        // Horizontal line
        const hrElement = document.createElement('hr');

        // Like section
        const likeSectionDiv = document.createElement('div');
        likeSectionDiv.className = 'd-flex justify-content-between align-items-center';

        const likeContainerDiv = document.createElement('div');
        likeContainerDiv.className = 'd-flex align-items-center';

        // Like button
        const likeButton = document.createElement('button');
        likeButton.style.background = 'none';
        likeButton.style.border = 'none';
        likeButton.style.cursor = 'pointer';
        likeButton.setAttribute('onclick', `likePost("${post.id}")`);

        const likeImg = document.createElement('img');
        likeImg.width = 30;
        likeImg.height = 30;
        likeImg.id = post.id;
        likeImg.className = `btn-like ${isLiked ? 'liked' : 'like'}`;
        likeImg.src = isLiked
            ? '/images/dashboard/8324235_ui_essential_app_liked_icon.png'
            : '/images/dashboard/8324235_ui_essential_app_like_icon.png';
        likeImg.alt = isLiked ? 'Liked' : 'Like';

        likeButton.appendChild(likeImg);

        // Like count
        const likeCountElement = document.createElement('h5');
        likeCountElement.className = 'pt-1 ps-1';
        likeCountElement.textContent = post.likeCount;

        likeContainerDiv.appendChild(likeButton);
        likeContainerDiv.appendChild(likeCountElement);

        // Download button
        const downloadButton = document.createElement('button');
        downloadButton.className = 'btn btn-outline-secondary btn-sm';
        downloadButton.setAttribute('onclick', 'downloadImg()');
        downloadButton.innerHTML = '<i class="bi bi-download"></i> Download';

        // Kết hợp các phần
        likeSectionDiv.appendChild(likeContainerDiv);
        likeSectionDiv.appendChild(downloadButton);

        cardBodyDiv.appendChild(titleLink);
        cardBodyDiv.appendChild(authorDiv);
        cardBodyDiv.appendChild(hrElement);
        cardBodyDiv.appendChild(likeSectionDiv);

        cardDiv.appendChild(imgElement);
        cardDiv.appendChild(cardBodyDiv);

        colDiv.appendChild(cardDiv);
        postElement.appendChild(colDiv);

        return postElement;
    }
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