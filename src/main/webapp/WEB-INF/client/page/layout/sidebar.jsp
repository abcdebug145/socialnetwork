<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <div class="container-fluid">
                <!-- Sidebar -->
                <div id="sidebar" class="float-md-start text-center"
                    style="position: fixed; top: 50%; transform: translateY(-50%);">
                    <div class="list-group list-group-flush">
                        <a href="/" title="Home" class="list-group-item list-group-item-action ">
                            <i class="bi bi-house fs-1"></i>
                        </a>
                        <a href="/profile/${currAccount.username}" title="Your Profile"
                            class="list-group-item list-group-item-action ">
                            <i class="bi bi-person-circle fs-1"></i>
                        </a>
                        <a class="list-group-item list-group-item-action " title="Create Post" data-bs-toggle="modal"
                            href="#CreatePostModal">
                            <i class="bi bi-plus-square fs-1"></i>
                        </a>
                        <a class="list-group-item list-group-item-action" title="Notification" id="noti-btn"
                            data-bs-toggle="modal" href="#NotificationModal" onclick="getNotification(event)">
                            <i class="bi bi-bell fs-1"></i>
                            <c:if test="${currAccount.unreadNoti > 0}">
                                <span id="unreadNoti"
                                    class="position-absolute bg-danger rounded-circle d-flex align-items-center justify-content-center text-white px-1"
                                    style="top: 0; right: 0; height: 15px; min-width: 15px; transform: translate(-90%, 90%); font-size: 12px;">
                                    ${currAccount.unreadNoti}
                                </span>
                            </c:if>
                        </a>
                        <a href="/logout">
                            <button class="list-group-item list-group-item-action border border-0" title="Logout"
                                type="submit">
                                <i class="bi bi-box-arrow-right" style="font-size: 35px"></i>
                            </button>
                        </a>
                    </div>
                </div>
            </div>
            <jsp:include page="create-post.jsp" />
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
            <script>
                $(document).ready(function () {
                    $.ajax({
                        url: "/getAccountStatus",
                        type: "GET",
                        success: function (data) {
                            console.log('status:' + data);
                            if (data === 'BANNED') {
                                alert("Your account has been banned. Please contact the administrator for more information.");
                                window.location.href = "/logout";
                            }
                        }
                    });
                })
                function getNotification(event) {
                    event.preventDefault();
                    console.log('reset notification');
                    $.ajax({
                        url: "/resetUnreadNoti",
                        type: "POST",
                        success: function (data) {
                            console.log('reset notification success');
                            var unreadNoti = document.getElementById("unreadNoti");
                            if (unreadNoti) {
                                unreadNoti.remove();
                            }
                            $('#NotificationModal').modal('show');
                        }
                    });
                    $.ajax({
                        url: "/getNotifications",
                        type: "GET",
                        success: function (data) {
                            var notiContainer = document.getElementById("noti-container");
                            notiContainer.innerHTML = "";
                            console.log(data);
                            data.forEach(notification => {
                                var notiElement = createNotificationElement(notification);
                                notiContainer.appendChild(notiElement);
                            });
                        }
                    });
                }

                function createNotificationElement(notification) {
                    var notiElement = document.createElement("a");
                    notiElement.href = "/post/" + notification.post.id;
                    notiElement.classList.add("row", "border", "border-info", "mb-3");
                    notiElement.style.textDecoration = "none";
                    notiElement.onmouseover = function () {
                        notiElement.style.backgroundColor = "lightgray";
                    };
                    notiElement.onmouseout = function () {
                        notiElement.style.backgroundColor = "";
                    };
                    if (notification.read === false) {
                        notiElement.style.backgroundColor = "lightblue";
                    }
                    var avatarElement = document.createElement("div");
                    avatarElement.classList.add("col-sm-3");
                    var avatarImg = document.createElement("img");
                    avatarImg.src = "/images/avatar/" + notification.account.avatar;
                    avatarImg.alt = "avatar";
                    avatarImg.height = "50";
                    avatarImg.width = "50";
                    avatarElement.appendChild(avatarImg);
                    notiElement.appendChild(avatarElement);
                    var messageElement = document.createElement("div");
                    messageElement.classList.add("col-sm-9");
                    var message = document.createElement("p");
                    message.innerText = notification.message;
                    messageElement.appendChild(message);
                    notiElement.appendChild(messageElement);
                    var timeAgoElement = document.createElement("div");
                    timeAgoElement.innerText = notification.timeAgo;
                    notiElement.appendChild(timeAgoElement);
                    return notiElement;
                }
            </script>