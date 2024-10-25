<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!-- <div class="container-fluid" style="background-color: purple;">
                <div class="row"> -->
                    <!-- Sidebar -->
                    <div id="sidebar" class="col-md-3 col-lg-2 p-0" style="height: 400px;">
                        <div class="list-group list-group-flush">
                            <div style="height: 80px;">
                            </div>
                            <c:if test="${pageContext.request.userPrincipal != null && pageContext.request.isUserInRole('ROLE_ADMIN')}">
                                <a href="/admin" class="list-group-item list-group-item-action d-flex justify-content-center">
                                    <i class="material-icons" style="font-size: 40px;">&#xe8b0;</i>
                                </a>
                            </c:if>
                            <a href="/" class="list-group-item list-group-item-action d-flex justify-content-center">
                                <i class="material-icons" style="font-size: 42px;">&#xe88a;</i>
                            </a>
                            <a href="#" class="list-group-item list-group-item-action d-flex justify-content-center">
                                <i class="material-icons" style="font-size: 40px;">&#xe853;</i>
                            </a>
                            <a href="#" class="list-group-item list-group-item-action d-flex justify-content-center"
                                id="openPopupBtn" onclick="handle(), preventDefault()">
                                <p>
                                    <span class="glyphicon" style="font-size: 40px;">&#xe081;</span> <!-- create post -->
                                </p>
                                <input type="hidden" value="${pageContext.request.userPrincipal.name}">
                            </a>
                            <a href="#" class="list-group-item list-group-item-action d-flex justify-content-center">
                                <p>
                                    <span class="glyphicon" style="font-size: 40px;">&#xe123;</span>
                                </p>
                            </a>
                            <c:if test="${not empty pageContext.request.userPrincipal}">
                                <form method="post" action="/logout">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                    <button
                                        class="list-group-item list-group-item-action d-flex justify-content-center">
                                        <i class="material-icons" style="font-size: 40px;">&#xe879;</i>
                                    </button>
                                </form>
                            </c:if>
                        </div>
                    </div>
                <!-- </div>
            </div> -->
            <script>
                function handle() {
                    var user = document.getElementById("openPopupBtn").querySelector("input").value;
                    console.log(user);
                    if (user == "") {
                        alert("Please login to create a post");
                    } else {
                        window.location.href = "/create";
                    }
                }
            </script>