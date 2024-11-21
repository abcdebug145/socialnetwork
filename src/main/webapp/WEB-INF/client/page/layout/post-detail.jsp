<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
                            <div style="display: flex; justify-content: space-between;">
                                <p>
                                    <strong>${comment.account.username}:</strong> ${comment.content}
                                </p>
                                <h6 name="date-comment" id="${comment.date}">${comment.timeAgo}</h6>
                            </div>
                        </c:forEach>
                        <div class="position-absolute bottom-0 pb-5 col-12">
                            <form:form action="/createComment" method="post">
                                <input type="hidden" name="postId" value="${post.id}">
                                <div class="form-group">
                                    <input type="text" class="form-control" name="comment" id="${post.id}" rows="3"
                                        placeholder="Write a comment"></input>
                                </div>
                            </form:form>
                        </div>
                    </div>
                    <jsp:include page="../layout/content-pane.jsp"></jsp:include>
                </div>
            </div>


            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                document.getElementsByName('comment')[0].addEventListener('keypress', function (e) {
                    if (e.key === 'Enter') {
                        var message = document.getElementsByName('comment')[0].value;
                        var postId = document.getElementById('${post.id}').getAttribute('id');
                        submitComment(message, postId);
                        document.getElementsByName('comment')[0].value = "";
                    }
                });

                function submitComment(message, postId) {
                    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
                    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

                    fetch('/createComment?postId=' + postId + '&comment=' + message, {
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
            </script>