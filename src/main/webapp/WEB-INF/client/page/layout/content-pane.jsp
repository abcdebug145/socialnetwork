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
                                    <c:set var="liked" value="false"/>
                                    <c:forEach var="i" items="${postLiked}">
                                        <c:if
                                                test="${i.post.id == post.id}">
                                            <c:set var="liked"
                                                   value="true"/>
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
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/masonry-layout@4.2.2/dist/masonry.pkgd.min.js"
        async></script>
<script src="https://unpkg.com/imagesloaded@4.1.4/imagesloaded.pkgd.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
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
</script>
</body>
</html>
