<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <title>Social Network</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
                <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
            </head>

            <body>
                <input type="hidden" value="${account.username}" id="hidden-username">
                <jsp:include page="../layout/navbar.jsp"></jsp:include>
                <br><br><br>
                <div class="container post-detail-container row mx-auto">
                    <div class="col-sm-11 row">
                        <div class="post-image col-sm-6 mb-5">
                            <img src="/images/post/${post.image}" class="img-fluid" alt="Post Image">
                        </div>
                        <div class="container col-sm-6 position-relative">
                            <h1>${post.title}</h1>
                            <hr>
                            <h3>Comments</h3>
                            <div class="comments-section">
                                <c:forEach items="${comments}" var="comment">
                                    <div style="display: flex; justify-content: space-between;">
                                        <p>
                                            <strong>${comment.account.username}:</strong> ${comment.content}
                                        </p>
                                        <h6 name="date-comment" id="${comment.date}">${comment.timeAgo}</h6>
                                    </div>
                                </c:forEach>
                            </div>
                            <div class="position-absolute bottom-0 pb-5 col-12 cmt-div">
                                <input type="hidden" name="postId" value="${post.account.username}" class="post-owner">
                                <div class="form-group">
                                    <input type="text" class="form-control" name="comment" id="${post.id}" rows="3"
                                        placeholder="Write a comment">
                                </div>
                            </div>
                        </div>
                        <jsp:include page="../layout/content-pane.jsp"></jsp:include>
                    </div>
                </div>
            </body>
            <script src="/webjars/jquery/dist/jquery.min.js"></script>
            <script src="/webjars/sockjs-client/sockjs.min.js"></script>
            <script src="/webjars/stomp-websocket/stomp.min.js"></script>
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
            <script src="/js/app.js"></script>
            </html>