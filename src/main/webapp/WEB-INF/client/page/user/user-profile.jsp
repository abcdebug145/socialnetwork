<%@ page import="com.project.socialnetwork.domain.Account" %>
    <%@ page import="com.project.socialnetwork.service.AccountService" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>User Profile</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
                    integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                    crossorigin="anonymous">
            </head>

            <body>
                <input type="hidden" value="${currAccount.username}" id="hidden-username">
                <div class="container-fluid mb-3 d-flex flex-column justify-content-center align-items-center">
                    <img src="/images/avatar/${account.avatar}" class="rounded-circle mb-3 p-2" width="150"
                        height="150">
                    <h3 class="p-2 mb-3">${account.username}</h3>
                    <c:choose>
                        <c:when test="${account.email==sessionScope.username}">
                            <p class="fs-14 text-secondary-emphasis mb-3">${account.email}</p>
                            <a href="/profile/edit-profile" class="btn btn-secondary rounded-pill mb-3 fs-5">Edit
                                Profile</a>
                        </c:when>
                    </c:choose>
                    <jsp:include page="../layout/content-pane.jsp"></jsp:include>
                </div>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
                <script src="/webjars/jquery/dist/jquery.min.js"></script>
                <script src="/webjars/sockjs-client/sockjs.min.js"></script>
                <script src="/webjars/stomp-websocket/stomp.min.js"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <script src="/js/app.js"></script>
            </body>

            </html>