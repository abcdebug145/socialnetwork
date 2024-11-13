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
            <a href="/profile/edit-profile" title="Your Profile" class="list-group-item list-group-item-action ">
                <i class="bi bi-person-circle fs-1"></i>
            </a>
            <a class="list-group-item list-group-item-action " title="Create Post" data-bs-toggle="modal"
               href="#CreatePostModal">
                <i class="bi bi-plus-square fs-1"></i>
            </a>
            <a href="#" class="list-group-item list-group-item-action position-relative"
               title="Notifications"
               onclick="showNotifications()">
                <i class="bi bi-bell fs-1"></i>
                <c:if test="${account.unreadNoti > 0}">
                                <span
                                        class="position-absolute bg-danger rounded-circle d-flex align-items-center justify-content-center text-white px-1"
                                        style="top: 0; right: 0; height: 15px; min-width: 15px; transform: translate(-90%, 90%); font-size: 12px;">
                                        ${account.unreadNoti}
                                </span>
                </c:if>
            </a>
            <form method="post" action="/logout">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button class="list-group-item list-group-item-action border border-0" title="Logout">
                    <i class="bi bi-box-arrow-right" style="font-size: 35px"></i>
                </button>
            </form>
        </div>
    </div>
</div>