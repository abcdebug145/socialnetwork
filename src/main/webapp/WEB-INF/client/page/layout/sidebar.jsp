<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div class="container-fluid">
    <div class="col">
        <!-- Sidebar -->
        <div id="sidebar" class="float-md-start  text-center">
            <div class="list-group list-group-flush">
                <a href="/" class="list-group-item list-group-item-action ">
                    <i class="material-icons" style="font-size: 42px;">&#xe88a;</i>
                </a>
                <a href="/profile/edit-profile" class="list-group-item list-group-item-action ">
                    <i class="material-icons" style="font-size: 40px;">&#xe853;</i>
                </a>
                <a class="list-group-item list-group-item-action " data-bs-toggle="modal"
                   href="#CreatePostModal">
                    <i class="bi bi-plus-square" style="font-size: 40px;"></i>
                </a>
                <a href="#" class="list-group-item list-group-item-action">
                    <span class="glyphicon" style="font-size: 40px;">&#xe123;</span>
                </a>
                <form method="post" action="/logout">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <button class="list-group-item list-group-item-action border border-0">
                        <i class="material-icons" style="font-size: 40px;">&#xe879;</i>
                    </button>
                </form>
                <!-- <a href="/logout" class="list-group-item list-group-item-action d-flex justify-content-center">
        <i class="material-icons" style="font-size: 40px;">&#xe879;</i>
    </a> -->
            </div>
        </div>
    </div>
</div>