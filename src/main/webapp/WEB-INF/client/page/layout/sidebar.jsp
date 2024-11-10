<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container-fluid">
    <!-- Sidebar -->
    <div id="sidebar" class="float-md-start text-center">
        <div class="list-group list-group-flush">
            <a href="/" class="list-group-item list-group-item-action ">
                <i class="material-icons fs-1">&#xe88a;</i>
            </a>
            <a href="/profile/edit-profile" class="list-group-item list-group-item-action ">
                <i class="material-icons fs-1">&#xe853;</i>
            </a>
            <a class="list-group-item list-group-item-action " data-bs-toggle="modal"
               href="#CreatePostModal">
                <i class="bi bi-plus-square fs-1"></i>
            </a>
            <a href="#" class="list-group-item list-group-item-action">
                <span class="glyphicon fs-1">&#xe123;</span>
            </a>
            <form method="post" action="/logout">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                <button class="list-group-item list-group-item-action border border-0">
                    <i class="material-icons fs-1">&#xe879;</i>
                </button>
            </form>
        </div>
    </div>
</div>