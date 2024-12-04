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
                <!-- Plugin css for this page -->
                <link rel="stylesheet" type="text/css" href="/js/select.dataTables.min.css">
                <!-- End plugin css for this page -->
                <!-- inject:css -->
                <link rel="stylesheet" href="/css/style.css">
                <!-- endinject -->
                <style>
                    .gradient-custom {
                        /* fallback for old browsers */
                        background: #f6d365;

                        /* Chrome 10-25, Safari 5.1-6 */
                        background: -webkit-linear-gradient(to right bottom, rgba(246, 211, 101, 1), rgba(253, 160, 133, 1));

                        /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
                        background: linear-gradient(to right bottom, rgba(246, 211, 101, 1), rgba(253, 160, 133, 1))
                    }
                </style>
            </head>

            <body class="with-welcome-text">
                <div class="container-scroller">
                    <!-- partial:partials/_navbar.html -->
                    <jsp:include page="../../layout/navbar.jsp" />
                    <!-- partial -->
                    <div class="container-fluid page-body-wrapper">
                        <!-- partial:partials/_sidebar.html -->
                        <jsp:include page="../../layout/sidebar.jsp" />
                        <!-- partial -->
                        <div class="main-panel">
                            <div class="content-wrapper">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="home-tab">
                                            <%--content here--%>
                                                <div class="row d-flex justify-content-center align-items-center h-100">
                                                    <!-- Change col-lg-6 to col-lg-8 or col-lg-10 for a wider profile card -->
                                                    <div class="col col-lg-8 mb-4 mb-lg-0">
                                                        <div class="card mb-3" style="border-radius: .5rem;">
                                                            <div class="row g-0" modelAttribute="account">
                                                                <div class="col-md-4 gradient-custom text-center text-white"
                                                                    style="border-top-left-radius: .5rem; border-bottom-left-radius: .5rem;">
                                                                    <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava1-bg.webp"
                                                                        alt="Avatar" class="img-fluid my-5"
                                                                        style="width: 120px;">
                                                                    <h6>#${account.id}</h6>
                                                                    <h5>${account.fullName}</h5>
                                                                    <p>${account.role.name}</p>
                                                                    <i class="far fa-edit mb-5"></i>
                                                                </div>
                                                                <div class="col-md-8">
                                                                    <div class="card-body p-4">
                                                                        <h6>Information</h6>
                                                                        <hr class="mt-0 mb-4">
                                                                        <div class="row pt-1">
                                                                            <div class="col-6 mb-3">
                                                                                <h6>Username</h6>
                                                                                <p class="text-muted">
                                                                                    ${account.username}</p>
                                                                            </div>
                                                                            <div class="col-6 mb-3">
                                                                                <h6>Email</h6>
                                                                                <p class="text-muted">${account.email}
                                                                                </p>
                                                                            </div>
                                                                        </div>
                                                                        <%-- <h6>Projects</h6>--%>
                                                                            <%-- <hr class="mt-0 mb-4">--%>
                                                                                <div class="row pt-1">
                                                                                    <div class="col-6 mb-3">
                                                                                        <h6>Address</h6>
                                                                                        <p class="text-muted">
                                                                                            ${account.address}</p>
                                                                                    </div>

                                                                                </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
                <script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js"
                    crossorigin="anonymous"></script>
                <script src="/js/off-canvas.js"></script>
                <script src="/js/template.js"></script>
                <script src="/js/settings.js"></script>
                <script src="/js/hoverable-collapse.js"></script>
                <script src="/js/todolist.js"></script>
                <!-- endinject -->
                <!-- Custom js for this page-->
                <script src="/js/jquery.cookie.js" type="text/javascript"></script>
                <%--<script src="js/dashboard.js"></script>--%>
                    <script src="/js/Chart.roundedBarCharts.js"></script>
                    <script src="/js/custom.js"></script>
                    <!-- End custom js for this page-->
            </body>

            </html>