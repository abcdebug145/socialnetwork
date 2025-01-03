
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
<nav class="navbar default-layout col-lg-12 col-12 p-0 fixed-top d-flex align-items-top flex-row">
    <div class="text-center navbar-brand-wrapper d-flex align-items-center justify-content-start">
        <div class="me-3">
            <button class="navbar-toggler navbar-toggler align-self-center" type="button" data-bs-toggle="minimize">
                <span class="icon-menu"></span>
            </button>
        </div>
        <div>
            <a class="navbar-brand brand-logo" href="/admin">
                <h3>Admin</h3>
            </a>
        </div>
    </div>
    <div class="navbar-menu-wrapper d-flex align-items-top">
        <ul class="navbar-nav">
            <li class="nav-item fw-semibold d-none d-lg-block ms-0">
                <h1 class="welcome-text">Good Morning, <span class="text-black fw-bold">${currAccount.fullName}</span></h1>
                <h3 class="welcome-sub-text">Your performance summary this week </h3>
            </li>
        </ul>
        <ul class="navbar-nav ms-auto">
            <li class="nav-item dropdown d-none d-lg-block user-dropdown">
                <a class="nav-link" id="UserDropdown" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                    <img src="/images/avatar/${currAccount.avatar}" alt="avatar" class="img-xs rounded-circle">
                </a>
                <div class="dropdown-menu dropdown-menu-end navbar-dropdown" aria-labelledby="UserDropdown">
                    <div class="dropdown-header text-center">
                        <img height="70" width="70"  class="img-md rounded-circle" src="/images/avatar/${currAccount.avatar}" alt="Profile image">
                        <p class="mb-1 mt-3 font-weight-semibold">${currAccount.fullName}</p>
                        <p class="fw-light text-muted mb-0">${currAccount.email}</p>
                    </div>
                    <a href="/admin/account/profile" class="dropdown-item">
                        <i class="mdi mdi-account-circle text-primary"></i>
                        Profile
                    </a>
                    <a href="/admin/account/change-password" class="dropdown-item">
                        <i class="mdi mdi-lock text-primary"></i>
                        Change Password
                    </a>
                    <a href="/admin/logout" class="dropdown-item">
                        <i class="mdi mdi-logout text-primary"></i>
                        Logout
                    </a>
                </div>
            </li>
        </ul>
        <button type="button" class="ms-3 btn btn-outline-danger" data-bs-toggle="modal" data-bs-target="#reportListModal" id="open-form-report">
            <i class="bi bi-flag-fill"></i>
        </button>
    </div>
</nav>
<div class="modal fade" id="reportListModal" tabindex="-1" aria-labelledby="reportListModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="reportListModalLabel">Report List</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th>Reporter</th>
                        <th>Reason</th>
                        <th>Title</th>
                        <th>Images</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${reportList}" var="report">
                        <tr>
                            <td>${report.account.username}</td>
                            <td>${report.reason}</td>
                            <td>${report.post.title}</td>
                            <td><img src="images/post/${report.post.image}"></td>
                            <td>
                                <form action="/admin/ban-user" method="post" style="display: inline;">
                                    <input type="hidden" name="accountId" value="${report.post.account.id}">
                                    <button type="submit" class="btn btn-primary">Ban</button>
                                </form>
                                <!-- Delete Report Form -->
                                <form action="/admin/delete-report" method="post" style="display: inline;">
                                    <input type="hidden" name="reportId" value="${report.id}">
                                    <button type="submit" class="btn btn-danger">Delete</button>
                                </form>
                                <a href="${pageContext.request.contextPath}/post/${report.post.id}" class="btn btn-info">View Post</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<script src="/webjars/jquery/dist/jquery.min.js"></script>
<script src="/webjars/sockjs-client/sockjs.min.js"></script>
<script src="/webjars/stomp-websocket/stomp.min.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="/js/custom.js"></script>