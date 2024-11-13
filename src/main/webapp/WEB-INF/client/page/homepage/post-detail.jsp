<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <title>Post Details</title>
</head>
<body>
<div class="container post-detail-container row">
    <div class="col-sm-1">
        <jsp:include page="../layout/sidebar.jsp"></jsp:include>
    </div>
    <div class="col-sm-11 row">
        <div class="post-image col-sm-6 mb-5">
            <img src="/images/post/${post.image}" class="img-fluid" alt="Post Image">
        </div>
        <div class="container col-sm-6 position-relative">
            <h1>${post.title}</h1>
            <hr>
            <h3>Comments</h3>
            <c:forEach items="${comments}" var="comment">
                <div>
                    <p><strong>$class="comment">
                        {comment.account.username}:</strong> ${comment.content}</p>

                </div>
            </c:forEach>
            <div class="position-absolute bottom-0 pb-5 col-12">
                <form:form action="/comment" method="post">
                    <input type="hidden" name="postId" value="${post.id}">
                    <div class="form-group">
                    <textarea class="form-control" name="content" id="content" rows="3"
                              placeholder="Write a comment"></textarea>
                    </div>
                </form:form>
            </div>
        </div>
        <jsp:include page="../layout/content-pane.jsp"></jsp:include>
    </div>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>