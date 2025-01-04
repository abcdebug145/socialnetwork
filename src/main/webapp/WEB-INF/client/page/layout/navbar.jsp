<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <nav class="navbar navbar-expand-lg bg-body-tertiary fs-5">
                <input type="hidden" value="${currAccount.username}" id="hidden-username">
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
                        <form class="d-flex col-6 mx-auto" role="search" action="/search" method="GET"
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
                        <c:choose>
                            <c:when test="${empty pageContext.request.userPrincipal.name}">
                                <div class="d-flex">
                                    <a href="#" class="btn btn-md me-2 btn-secondary px-3 py-2 rounded-pill"
                                        type="button" data-bs-toggle="modal" data-bs-target="#loginModal"
                                        id="open-form-login">
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
                                    <img src="/images/avatar/${currAccount.avatar}" height="40" width="40">
                                </div>
                                
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </nav>
            <!-- form login -->
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
                                <div id="loginError" class="alert-danger text-center my-2"
                                    style="display: none; color: red; font-size: 16px">
                                </div>
                                <div id="logoutSuccess" class="alert alert-success text-center my-2"
                                    style="display: none;"></div>

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
                                    <button type="button" class="btn btn-dark" onclick="login()">Log in</button>
                                </div>

                                <!-- Forgot password -->
                                <div class="text-center mb-3">
                                    <a class="text-muted" href="${pageContext.request.contextPath}/forgot-password">Forgot password?</a>
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
                function login() {
                    const username = document.getElementById('form2Example11').value;
                    const password = document.getElementById('form2Example22').value;
                    const csrfParameterName = '${_csrf.parameterName}';
                    const csrfToken = '${_csrf.token}';

                    fetch('/login', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams({
                            username: username,
                            password: password,
                            [csrfParameterName]: csrfToken
                        })
                    })
                        .then(response => {
                            if (response.ok) {
                                window.location.reload();
                            } else {
                                return response.text().then(text => { throw new Error(text); });
                            }
                        })
                        .catch(error => {
                            document.getElementById('loginError').style.display = 'block';
                            document.getElementById('loginError').innerText = 'Invalid email or password.';
                        });
                }
            </script>