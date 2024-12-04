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
            </head>

            <body class="with-welcome-text">
                <div class="container-scroller">
                    <!-- partial:partials/_navbar.html -->
                    <jsp:include page="../layout/navbar.jsp" />
                    <!-- partial -->
                    <div class="container-fluid page-body-wrapper">
                        <!-- partial:partials/_sidebar.html -->
                        <jsp:include page="../layout/sidebar.jsp" />
                        <!-- partial -->
                        <div class="main-panel">
                            <div class="content-wrapper">
                                <div class="row">
                                    <div class="col-sm-12">
                                        <div class="home-tab">
                                            <%--content here--%>
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
                <script src="js/jquery.cookie.js" type="text/javascript"></script>
                <%--<script src="js/dashboard.js"></script>--%>
                    <script src="js/Chart.roundedBarCharts.js"></script>
                    <!-- End custom js for this page-->
            </body>

            </html>