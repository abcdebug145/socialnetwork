<%@ page import="com.project.socialnetwork.entity.Account" %>
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
                <jsp:include page="../layout/sidebar.jsp" />
                <div class="container-fluid mb-3 d-flex flex-column justify-content-center align-items-center">
                    <img src="/images/avatar/${account.avatar}" class="rounded-circle mb-3 p-2" width="150"
                        height="150">
                    <h3 class="p-2 mb-3">${account.username}</h3>
                    <p class="fs-14 text-secondary-emphasis mb-3">${account.email}</p>
                    <c:if test="${account.email==sessionScope.username}">
                        <a href="/profile/edit-profile" class="btn btn-secondary rounded-pill mb-3 fs-5">Edit
                            Profile</a>
                    </c:if>
                    <c:if test="${currAccount.role == 'ADMIN' && account.email!=sessionScope.username}">
                        <c:choose>
                            <c:when test="${account.status == 'ACTIVE'}">
                                <a onclick="changeStatus(event)" type="button" style="width: 95px;"
                                    class="btn btn-danger btn-delete" id-account="${account.id}"
                                    curr-status="${account.status}" username="${account.username}">
                                    Ban
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a onclick="changeStatus(event)" type="button"
                                    style="width: 95px; background-color: #96fa09" class="btn btn-success btn-delete"
                                    id-account="${account.id}" curr-status="${account.status}"
                                    username="${account.username}">
                                    Unban
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                    <jsp:include page="../layout/content-pane.jsp" />
                </div>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
                <script src="/webjars/jquery/dist/jquery.min.js"></script>
                <script src="/webjars/sockjs-client/sockjs.min.js"></script>
                <script src="/webjars/stomp-websocket/stomp.min.js"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <script src="/js/app.js"></script>
                <script>
                    function changeStatus(event) {
                        let selectedUsername = event.target.getAttribute('username');
                        let selectedId = event.target.getAttribute('id-account');
                        let currStatus = event.target.getAttribute('curr-status');
                        let currBtn = $(event.target);
                        $.ajax({
                            url: '/admin/api-changeStatus-account?id=' + selectedId + '&currStatus=' + currStatus,
                            type: 'PUT',
                            success: function (data) {
                                if (currStatus == 'ACTIVE') {
                                    currBtn.replaceWith(
                                        '<a onclick="changeStatus(event)" type="button" style="width: 95px; background-color: #96fa09" class="btn btn-success btn-delete btn-change-status" id-account="' + selectedId + '" curr-status="BANNED" username="' + selectedUsername + '">\nUnban\n</a>'
                                    );
                                    stompClient.send("/app/ban", {}, selectedUsername);
                                }
                                if (currStatus == 'BANNED') {
                                    currBtn.replaceWith(
                                        '<a onclick="changeStatus(event)" type="button" style="width: 95px;" class="btn btn-danger btn-delete btn-change-status" id-account="' + selectedId + '" curr-status="ACTIVE" username="' + selectedUsername + '">\nBan\n</a>'
                                    );
                                }
                            },
                            error: function (error) {
                                console.error('Error:', error);
                            }
                        });
                    }
                </script>
            </body>

            </html>