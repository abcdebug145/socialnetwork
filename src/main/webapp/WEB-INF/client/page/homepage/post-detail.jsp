<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
                <title>Post Details</title>
                <style>
                    .post-detail-container {
                        display: flex;
                        flex-wrap: wrap;
                    }

                    .post-image {
                        flex: 1 1 50%;
                        max-width: 50%;
                    }

                    .post-content {
                        flex: 1 1 50%;
                        max-width: 50%;
                        padding: 20px;
                    }
                </style>
            </head>

            <body>
                <div class="container post-detail-container">
                    <div class="post-image">
                        <c:choose>
                            <c:when test="${not fn:containsIgnoreCase(post.image, 'http')}">
                                <img src="images/post/${post.image}" class="img-fluid" alt="Post Image">
                            </c:when>
                            <c:otherwise>
                                <img src="${post.image}" class="img-fluid" alt="Post Image">
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="post-content">
                        <h1>${post.title}</h1>
                        <!-- p desciptino -->
                        <hr>
                        <h3>Comments</h3>
                        <c:forEach items="${comments}" var="comment">
                            <div class="comment">
                                <p><strong>${comment.account.username}:</strong> ${comment.content}</p>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>