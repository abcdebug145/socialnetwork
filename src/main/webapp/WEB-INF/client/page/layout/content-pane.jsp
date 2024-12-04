<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
                <html>

                <head>
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
                        rel="stylesheet"
                        integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                        crossorigin="anonymous">
                </head>

                <body>
                    <div class="container grid">
                        <div class="row d-flex justify-content-center post-container"
                            data-masonry='{"percentPosition": true }' id="post-container">
                            <c:forEach items="${listPost}" var="post">
                                <div class="col-md-4 mb-4">
                                    <div class="card">
                                        <a href="/post/${post.id}">
                                            <img src="/images/post/${post.image}" class="card-img-top"
                                                alt="This post may be contains ${post.content}">
                                        </a>
                                        <div class="card-body">
                                            <a href="/post/${post.id}" style="text-decoration: none;">
                                                <h5 class="card-title">${post.title}</h5>
                                            </a>
                                            <div style="font-size: 12px;">
                                                by @<a href="/profile/${post.account.username}"
                                                    style="text-decoration: none;"
                                                    class="post-owner">${post.account.username}</a>
                                            </div>
                                            <hr>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <div class="d-flex align-items-center">
                                                    <button style="background: none; border: none; cursor: pointer;"
                                                        class="button-like">
                                                        <c:set var="liked" value="false" />
                                                        <c:forEach var="i" items="${postLiked}">
                                                            <c:if test="${i.post.id == post.id}">
                                                                <c:set var="liked" value="true" />
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
                                                <div class="d-flex align-items-center m-0">
                                                    <button class="btn btn-outline-secondary btn-sm"
                                                        onclick="downloadImg()">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16"
                                                            fill="currentColor" class="bi bi-download"
                                                            viewBox="0 0 16 16">
                                                            <path
                                                                d="M.5 9.9a.5.5 0 0 1 .5.5v2.5a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1v-2.5a.5.5 0 0 1 1 0v2.5a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2v-2.5a.5.5 0 0 1 .5-.5" />
                                                            <path
                                                                d="M7.646 11.854a.5.5 0 0 0 .708 0l3-3a.5.5 0 0 0-.708-.708L8.5 10.293V1.5a.5.5 0 0 0-1 0v8.793L5.354 8.146a.5.5 0 1 0-.708.708z" />
                                                        </svg>
                                                    </button>
                                                    <c:if test="${account.email==sessionScope.username}">
                                                        <form:form action="/delete-post/${post.id}" method="post"
                                                            id="delete-post-form">
                                                            <button class="btn btn-outline-secondary btn-sm"
                                                                onclick="deletePost('${post.id}'), preventDefault()"
                                                                type="submit">
                                                                <svg xmlns="http://www.w3.org/2000/svg" width="16"
                                                                    height="16" fill="currentColor" class="bi bi-trash"
                                                                    viewBox="0 0 16 16">
                                                                    <path
                                                                        d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z" />
                                                                    <path
                                                                        d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z" />
                                                                </svg>
                                                            </button>
                                                        </form:form>
                                                    </c:if>
                                                </div>
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
                    <script src="https://code.jquery.com/jquery-3.3.1.min.js"
                        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8= sha256-T+aPohYXbm0fRYDpJLr+zJ9RmYTswGsahAoIsNiMld4="
                        crossorigin="anonymous"></script>
                    <script>
                        function deletePost(postId) {
                            if (confirm("Are you sure you want to delete this post?")) {
                                document.forms["delete-post-form"].submit();
                            }
                        }
                    </script>
                </body>

                </html>