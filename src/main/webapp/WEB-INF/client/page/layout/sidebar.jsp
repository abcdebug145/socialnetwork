<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <div class="container-fluid">
        <!-- Main Sidebar -->
        <div id="sidebar" class="float-md-start text-center"
             style="position: fixed; top: 50%; transform: translateY(-50%); left: 0; z-index: 1030;">
            <div class="list-group list-group-flush">
                <a href="/" title="Home" class="list-group-item list-group-item-action">
                    <i class="bi bi-house fs-1"></i>
                </a>
                <a href="/profile/${currAccount.username}" title="Your Profile"
                   class="list-group-item list-group-item-action">
                    <i class="bi bi-person-circle fs-1"></i>
                </a>
                <a class="list-group-item list-group-item-action" title="Create Post"
                   data-bs-toggle="modal" href="#CreatePostModal">
                    <i class="bi bi-plus-square fs-1"></i>
                </a>
                <a class="list-group-item list-group-item-action" title="Notification" id="noti-btn"
                   role="button" onclick="toggleNotifications(event)">
                    <i class="bi bi-bell fs-1"></i>
                    <c:if test="${currAccount.unreadNoti > 0}">
                        <span id="unreadNoti" class="position-absolute bg-danger rounded-circle d-flex align-items-center justify-content-center text-white px-1"
                              style="top: 0; right: 0; height: 15px; min-width: 15px; transform: translate(-90%, 90%); font-size: 12px;">
                                ${currAccount.unreadNoti}
                        </span>
                    </c:if>
                </a>
                <a href="/logout">
                    <button class="list-group-item list-group-item-action border border-0" title="Logout" type="submit">
                        <i class="bi bi-box-arrow-right" style="font-size: 35px"></i>
                    </button>
                </a>
            </div>
        </div>
    
        <!-- Notification Sidebar -->
        <div id="notificationSidebar" class="notification-sidebar">
            <div class="bg-white shadow">
                <!-- Move bg-info to encompass the whole top section -->
                <div class="bg-primary p-3 d-flex justify-content-between align-items-center ">
                    <i class="bi bi-bell fs-4 text-white"></i>
                    <h4 class="mb-0 text-white">Notifications</h4>
                    <button type="button" class="btn-close" onclick="toggleNotifications(event)" aria-label="Close"></button>
                </div>
                <div id="noti-container">
                    <!-- Notifications will be dynamically inserted here -->
                </div>
            </div>
        </div>

    </div>
    <jsp:include page="create-post.jsp"/>

<style>
    /* Notification Sidebar Styles */
    .notification-sidebar {
        position: fixed;
        left: 80px;
        top: 50%;
        transform: translateY(-50%);
        height: 80vh;
        width: 500px;
        background: white;
        box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
        z-index: 1020;
        overflow-y: auto;
        overflow-x:hidden;
        display: none;
        transition: all 0.3s ease-in-out;
    }

    .notification-sidebar.show {
        display: block;
    }

    /* Bell button active state */
    #noti-btn.active {
        background-color: #e9ecef;
        color: #0d6efd;
        transform: scale(1.05);
        transition: all 0.3s ease;
    }
    /*add hover state for notification sidebar*/
    .notification-sidebar a:hover {
        background-color: #e9ecef;
    }
</style>

<script>
    let isNotificationsOpen = false;

    function toggleNotifications(event) {
        event.preventDefault();
        const notificationSidebar = document.getElementById('notificationSidebar');
        const bellButton = document.getElementById('noti-btn');

        isNotificationsOpen = !isNotificationsOpen;

        if (isNotificationsOpen) {
            console.log('Opening notifications...');
            notificationSidebar.classList.add('show');
            bellButton.classList.add('active');
            getNotifications();
        } else {
            console.log('Closing notifications...');
            notificationSidebar.classList.remove('show');
            bellButton.classList.remove('active');
        }
    }

    function getNotifications() {
        console.log('Fetching notifications...');
        // Reset unread notifications count
        $.ajax({
            url: "/resetUnreadNoti",
            type: "POST",
            success: function () {
                console.log('Unread notifications reset.');
                const unreadNoti = document.getElementById("unreadNoti");
                if (unreadNoti) {
                    unreadNoti.remove();
                }
            }
        });

        // Fetch notifications from the server
        $.ajax({
            url: "/getNotifications",
            type: "GET",
            success: function (data) {
                const notiContainer = document.getElementById("noti-container");
                notiContainer.innerHTML = "";
                data.forEach(notification => {
                    const notiElement = createNotificationElement(notification);
                    notiContainer.appendChild(notiElement);
                });
            }
        });
    }

    function createNotificationElement(notification) {
        const notiElement = document.createElement("a");
        notiElement.href = "/post/" + notification.post.id;
        notiElement.classList.add("row", "align-items-center", "border", "border-info", "p-2");
        notiElement.style.textDecoration = "none";

        // Highlight unread notifications
        if (!notification.read) {
            notiElement.style.backgroundColor = "lightblue";
        }

        // Avatar
        const avatarElement = document.createElement("div");
        avatarElement.classList.add("col-sm-3", "text-center");
        const avatarImg = document.createElement("img");
        avatarImg.src = "/images/avatar/" + notification.account.avatar;
        avatarImg.alt = "avatar";
        avatarImg.classList.add("rounded-circle");
        avatarImg.height = 50;
        avatarImg.width = 50;
        avatarElement.appendChild(avatarImg);

        // Message
        const messageElement = document.createElement("div");
        messageElement.classList.add("col-sm-6", "text-center");
        const boldName = document.createElement("strong");
        boldName.innerText = notification.account.username + " ";
        const message = document.createElement("span");
        message.innerText = notification.message;
        messageElement.appendChild(boldName);
        messageElement.appendChild(message);

        // Time
        const timeElement = document.createElement("div");
        timeElement.classList.add("col-sm-3", "text-end", "text-muted","mt-auto");
        timeElement.innerText = notification.timeAgo;

        // Append all elements
        notiElement.appendChild(avatarElement);
        notiElement.appendChild(messageElement);
        notiElement.appendChild(timeElement);

        return notiElement;
    }
</script>
