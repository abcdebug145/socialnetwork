<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <nav class="navbar navbar-expand-lg bg-body-tertiary fs-5">
                <div class="container-fluid">
                    <svg xmlns="http://www.w3.org/2000/svg" height="30" width="30" fill="currentColor"
                        class="bi bi-image pt-2">
                        <path d="M6.002 5.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0" />
                        <path
                            d="M2.002 1a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V3a2 2 0 0 0-2-2zm12 1a1 1 0 0 1 1 1v6.5l-3.777-1.947a.5.5 0 0 0-.577.093l-3.71 3.71-2.66-1.772a.5.5 0 0 0-.63.062L1.002 12V3a1 1 0 0 1 1-1z" />
                    </svg>
                    <a class="navbar-brand" href="#">SN</a>
                    <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                        <span class="navbar-toggler-icon"></span>
                    </button>
                    <div class="collapse navbar-collapse" id="navbarSupportedContent">
                        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                            <li class="nav-item">
                                <a class="nav-link rounded-pill" aria-current="page" href="#"
                                    onclick="ChangeCurrentActive(this)">Home</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link rounded-pill" href="#" onclick="ChangeCurrentActive(this)">Watch</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link rounded-pill" href="#"
                                    onclick="ChangeCurrentActive(this)">Explore</a>
                            </li>
                        </ul>
                        <form class="d-flex col-6 mx-auto" role="search">
                            <div class="input-group">
                                <input class="form-control border-end-0 border rounded-pill" type="search"
                                    placeholder="search" id="example-search-input" />
                                <span class="input-group-append">
                                    <button class="btn btn-outline-secondary bg-white border-0 rounded-pill ms-n5"
                                        type="button">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="16" height="30"
                                            fill="currentColor" class="bi bi-search pt-2">
                                            <path
                                                d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0" />
                                        </svg>
                                    </button>
                                </span>
                            </div>
                        </form>
                        <c:choose>
                            <c:when test="${empty pageContext.request.userPrincipal.name}">
                                <div class="d-flex">
                                    <a href="#" class="btn btn-md me-2 btn-secondary px-3 py-2 rounded-pill"
                                        type="button" data-bs-toggle="modal" data-bs-target="#loginModal">
                                        Login
                                    </a>
                                    <a href="/register" class="btn btn-md btn-outline-dark px-3 py-2 rounded-pill"
                                        type="button">
                                        Sign Up
                                    </a>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="d-flex">
                                    <img src="${pageContext.request.contextPath}images/avatar/default-avatar.png"
                                        height="40" width="40">
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </nav>
            <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 id="loginModalLabel">Join our Social Network</h4>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <form action="/login" method="post">
                                <p class="text-center">Please login to your account</p>

                                <!-- Error and logout notifications -->
                                <c:if test="${param.error != null}">
                                    <div class="alert alert-danger text-center my-2">Invalid email or password.</div>
                                </c:if>
                                <c:if test="${param.logout != null}">
                                    <div class="alert alert-success text-center my-2">Logout successful.</div>
                                </c:if>

                                <!-- Username input -->
                                <div class="mb-3">
                                    <label for="form2Example11" class="form-label">Username</label>
                                    <input type="email" id="form2Example11" class="form-control" name="username"
                                        placeholder="ur_mail@example.com" required>
                                </div>

                                <!-- Password input -->
                                <div class="mb-3">
                                    <label for="form2Example22" class="form-label">Password</label>
                                    <input type="password" id="form2Example22" class="form-control" name="password"
                                        placeholder="Enter your password" required>
                                </div>

                                <!-- CSRF Token -->
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />

                                <!-- Login button -->
                                <div class="d-grid mb-3">
                                    <button type="submit" class="btn btn-dark">Log in</button>
                                </div>

                                <!-- Forgot password -->
                                <div class="text-center mb-3">
                                    <a class="text-muted" href="#!">Forgot password?</a>
                                </div>

                                <!-- Registration link -->
                                <div class="d-flex justify-content-center align-items-center">
                                    <p class="mb-0">Don't have an account?</p>
                                    <a href="/register" class="ms-2">Create new</a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <script>
                document.addEventListener("")

                function ChangeCurrentActive(object) {
                    var obj = document.querySelector(".active");
                    if (obj) {
                        obj.style.fontWeight = 400;
                        obj.classList.remove("active");
                    }
                    object.classList.add("active");
                    object.style.fontWeight = 600;
                }
            </script>