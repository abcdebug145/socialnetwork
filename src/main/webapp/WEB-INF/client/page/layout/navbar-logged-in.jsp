<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <nav class="navbar navbar-expand-lg bg-body-tertiary fs-5">
                <div class="container-fluid">
                    <a class="navbar-brand" href="/">
                        <img src="/images/dashboard/logo.png" alt="asdasd"
                            style="width: 45px; height: 45px; margin-left: 30px; margin-right: 15px;">
                        Social Network</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <form class="d-flex col-6 mx-auto" role="search" action="/" method="GET"
                            modelAttribute="keyword">
                            <div class="input-group">
                                <input class="form-control border-end-0 border rounded-pill" type="search"
                                    placeholder="search" id="example-search-input" name="keyword" value="${keyword}" />
                                <span class="input-group-append">
                                    <button class="btn btn-outline-secondary bg-white border-0 rounded-pill ms-n5"
                                        type="submit">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="30"
                                            fill="currentColor" class="bi bi-search pt-2">
                                            <path
                                                d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0" />
                                        </svg>
                                    </button>
                                </span>
                            </div>
                        </form>
                        <div class="d-flex">
                            <img src="/images/avatar/default-avatar.png" height="40" width="40">
                        </div>
                    </div>
                </div>
            </nav>