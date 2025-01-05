<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <!-- Required meta tags -->
                <meta charset="utf-8">
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
                <title>Dashboard</title>
                <link rel="stylesheet" href="/css/style.css">
            </head>
            <style>
                .container-fluid {
                    padding-right: 15px;
                    padding-left: 15px;
                    margin-right: auto;
                    margin-left: auto;
                    width: 100%;
                }

                /* Add top padding to account for fixed navbar */
                .page-body-wrapper {
                    display: flex;
                    flex-direction: column;
                    min-height: 100vh;
                    width: 100%;
                    padding: 0;
                    padding-top: 70px; /* Adjust this value based on your navbar height */
                }

                .main-panel {
                    width: 100%;
                    margin: 0 auto;
                    padding: 0;
                    position: relative; /* Ensure proper stacking context */
                }

                /* Ensure content stays below navbar */
                .content-wrapper {
                    padding: 1.5rem;
                    width: 100%;
                    max-width: 100%;
                    margin: 0 auto;
                    position: relative;
                    z-index: 1; /* Lower than navbar */
                }

                /* Set navbar z-index */
                .navbar {
                    position: fixed;
                    top: 0;
                    left: 0;
                    right: 0;
                    z-index: 1030; /* Higher than content */
                }

                /* Ensure table header is visible */
                .table thead th {
                    background-color: #fff; /* Or match your design's background color */
                    position: sticky;
                    top: 70px; /* Should match navbar height */
                    z-index: 2;
                }
            </style>

            <body class="with-welcome-text">
                <div class="container-scroller">
                    <jsp:include page="../layout/navbar.jsp" />
                    <div class="container-fluid page-body-wrapper">
                        <div class="main-panel">
                            <div class="content-wrapper">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="home-tab">
                                            <%--content here--%>
                                                <table class="table table-striped">
                                                    <thead>
                                                        <tr style="text-align: center">
                                                            <th scope="col">#ID</th>
                                                            <th scope="col">Username</th>
                                                            <th scope="col">Full name</th>
                                                            <th scope="col">Address</th>
                                                            <th scope="col">Status</th>
                                                            <th scope="col">Action</th>
                                                        </tr>
                                                    </thead>
                                                    <tbody>
                                                        <c:forEach var="account" items="${accounts}">
                                                            <tr style="text-align: center">
                                                                <th scope="row">${account.id}</th>
                                                                <td class="username-col">
                                                                    <c:choose>
                                                                        <c:when test="${account.role == 'ADMIN'}">
                                                                            <span
                                                                                style="color: orangered">${account.username}</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            ${account.username}
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                                <td>${account.fullName}</td>
                                                                <td>${account.address}</td>
                                                                <td>
                                                                    <c:choose>
                                                                        <c:when test="${account.status == 'ACTIVE'}">
                                                                            <span
                                                                                style="color: greenyellow; font-size: 30px;"
                                                                                data-bs-toggle="tooltip"
                                                                                title="Account is active">●</span>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <span style="color: red; font-size: 30px;"
                                                                                data-bs-toggle="tooltip"
                                                                                title="Account is banned">●</span>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                <td>
                                                                    <a href="/profile/${account.username}" type="button"
                                                                        style="width: 95px;"
                                                                        class="btn btn-success">View</a>
                                                                    <c:choose>
                                                                        <c:when test="${account.status == 'ACTIVE'}">
                                                                            <a onclick="event.preventDefault()"
                                                                                type="button" style="width: 95px;"
                                                                                class="btn btn-danger btn-delete btn-change-status"
                                                                                id-account="${account.id}"
                                                                                curr-status="${account.status}">
                                                                                Ban
                                                                            </a>
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <a onclick="event.preventDefault()"
                                                                                type="button"
                                                                                style="width: 95px; background-color: #96fa09"
                                                                                class="btn btn-success btn-delete btn-change-status"
                                                                                id-account="${account.id}"
                                                                                curr-status="${account.status}">
                                                                                Unban
                                                                            </a>
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </td>
                                                            </tr>
                                                        </c:forEach>
                                                    </tbody>
                                                </table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <nav style="display: flex; justify-content: center">
                        <ul class="pagination pagination-circle">
                            <li class="prv-btn"><a class="page-link">Previous</a></li>
                            <c:forEach var="page" begin="1" end="${totalPages}">
                                <li class="page-item"><a class="page-link" href="/admin?page=${page}">${page}</a>
                                </li>
                            </c:forEach>
                            <li class="next-btn"><a class="page-link">Next</a></li>
                        </ul>
                    </nav>
                </div>

                <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js"></script>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"></script>
                <script src="/js/custom.js"></script>
                <!-- End custom js for this page-->
                <script>
                    window.onload = function () {
                        const params = new URLSearchParams(window.location.search);
                        const page = parseInt(params.get('page'));
                        console.log(page);

                        var pageItems = document.getElementsByClassName('page-item');
                        pageItems[page - 1].classList.add('active');

                        if (page == 1) {
                            var button = document.getElementsByClassName('prv-btn');
                            button[0].classList.add('disabled');
                            button = document.getElementsByClassName('next-btn');
                            button[0].classList.remove('disabled');
                        }
                        if (pageItems.length == page) {
                            var button = document.getElementsByClassName('next-btn');
                            button[0].classList.add('disabled');
                            button = document.getElementsByClassName('prv-btn');
                            button[0].classList.remove('disabled');
                        }
                        if (page > 1) {
                            var button = document.getElementsByClassName('prv-btn');
                            button[0].classList.remove('disabled');
                            button[0].children[0].setAttribute('href', '/admin/accounts?page=' + (page - 1));
                        }
                        if (page < pageItems.length) {
                            var button = document.getElementsByClassName('next-btn');
                            button[0].classList.remove('disabled');
                            button[0].children[0].setAttribute('href', '/admin/accounts?page=' + (page + 1));
                        }
                    }
                    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
                    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                        return new bootstrap.Tooltip(tooltipTriggerEl);
                    });
                </script>
            </body>

            </html>