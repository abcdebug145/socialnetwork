<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <!doctype html>
            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>Login</title>
                <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css"
                    integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
                    crossorigin="anonymous">
            </head>

            <body>
                <div id="popupOverlay" class="container col-5">
                    <div class="card-body p-md-5 mx-md-4">
                        <div class="text-center">
                            <h4 class="mt-1 mb-5 pb-1">Join our Social Network</h4>
                        </div>

                        <form action="/login" method="post">
                            <p>Please login to your account</p>
                            <c:if test="${param.error != null}">
                                <div class="my-2" style="color: red;">Invalid email or password.
                                </div>
                            </c:if>
                            <c:if test="${param.logout != null}">
                                <div class="my-2 d-flex justify-content-center" style="color: greenyellow;">Logout success.
                                </div>
                            </c:if>
                            <div data-mdb-input-init="" class="form-outline mb-4" data-mdb-input-initialized="true">
                                <label class="form-label" for="form2Example11" style="margin-left: 0px">Username</label>
                                <input type="email" id="form2Example11" class="form-control"
                                    placeholder="ur_mail@example.com" name="username" />
                                <div class="form-notch">
                                    <div class="form-notch-leading" style="width: 9px"></div>
                                    <div class="form-notch-middle" style="width: 66.4px"></div>
                                    <div class="form-notch-trailing"></div>
                                </div>
                            </div>
                            <div data-mdb-input-init="" class="form-outline mb-4" data-mdb-input-initialized="true">
                                <label class="form-label" for="form2Example22" style="margin-left: 0px">Password</label>
                                <input type="password" id="form2Example22" class="form-control" name="password"
                                    placeholder="Enter your password" />
                                <div class="form-notch">
                                    <div class="form-notch-leading" style="width: 9px"></div>
                                    <div class="form-notch-middle" style="width: 64.8px"></div>
                                    <div class="form-notch-trailing"></div>
                                </div>
                            </div>
                            <div>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                            </div>

                            <div class="text-center pt-1 mb-5 pb-1">
                                <button data-mdb-button-init="" data-mdb-ripple-init="" class="btn btn-dark col-12 mb-3"
                                    type="submit" data-mdb-button-initialized="true">
                                    Log in
                                </button>
                                <a class="text-muted" href="${pageContext.request.contextPath}/forgot-password">Forgot password?</a>
                            </div>

                            <div class="d-flex align-items-center justify-content-center pb-4">
                                <p class="mb-0 me-2">Don't have an account?</p>
                                <a href="/register" class="ms-3">Create new</a>
                            </div>
                        </form>
                    </div>
                </div>
            </body>
            <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
                integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
                crossorigin="anonymous"></script>
            <script src="https://cdn.jsdelivr.net/npm/popper.js@1.14.7/dist/umd/popper.min.js"
                integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
                crossorigin="anonymous"></script>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js"
                integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
                crossorigin="anonymous"></script>
            </html>