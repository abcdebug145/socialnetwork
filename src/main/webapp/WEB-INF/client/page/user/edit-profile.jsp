<%@ page contentType="text/html; charset=UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
            <!DOCTYPE html>
            <html lang="en">

            <head>
                <meta charset="utf-8" />
                <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
                <meta name="description" content="" />
                <meta name="author" content="" />
                <title>User</title>
                <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
                <link href="${pageContext.request.contextPath}/css/styles.css" rel="stylesheet" />
                <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
                    integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
                    crossorigin="anonymous" />
                <style>
                    .gradient-background {
                        background: linear-gradient(to right bottom,
                                rgba(246, 211, 101, 1),
                                rgba(253, 160, 133, 1));
                    }
                </style>
            </head>

            <body class="sb-nav-fixed">
                <form:form action="/profile/edit-profile" method="post" enctype="multipart/form-data"
                    modelAttribute="account">
                    <div class="container h-100 d-flex justify-content-center align-items-center">
                        <div class="row w-100">
                            <div class="col-md-4 col-sm-8 gradient-background p-4">
                                <div class="card border-0 bg-transparent">
                                    <img src="/images/avatar/${account.avatar}"
                                        class="card-img-top rounded-circle mx-auto d-block" alt="Profile Picture"
                                        id="avatarPicture" style="width: 200px; height: 200px" />

                                    <div class="card-body d-flex justify-content-center">
                                        <input id="avatarInput" type="file" value="avatar" accept=".png,.jpeg,.jpg,.svg"
                                            class="form-control" onchange="changeProfilePic(this.files[0])"
                                            style="display: none" name="avatarFile" />
                                        <button type="button" class="btn btn-primary"
                                            onclick="document.getElementById('avatarInput').click()">
                                            Change Avatar
                                        </button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-8 col-sm-8 text-white" style="background-color: #808080">
                                <h3 class="my-4">User Information</h3>
                                <hr />
                                <div class="row mb-3">
                                    <label for="username" class="col-sm-3 col-form-label">Username:</label>
                                    <div class="col-sm-9">
                                        <form:input type="text" class="form-control" id="username" path="username"
                                            value="${account.username}" />
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label for="email" class="col-sm-3 col-form-label">Email:</label>
                                    <div class="col-sm-9">
                                        <form:input type="email" class="form-control" id="email" path="email"
                                            value="${account.email}" />
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label for="fullname" class="col-sm-3 col-form-label">Full Name:</label>
                                    <div class="col-sm-9">
                                        <form:input type="text" class="form-control" id="fullname" path="fullName"
                                            value="${account.fullName}" />
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <label for="address" class="col-sm-3 col-form-label">Address:</label>
                                    <div class="col-sm-9">
                                        <form:input type="text" class="form-control" id="fullname" path="address"
                                            value="${account.address}" />
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-sm-9 offset-sm-3">
                                        <button class="btn btn-outline-light" type="submit">
                                            Save Changes
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form:form>
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                    crossorigin="anonymous"></script>
                <script src="${pageContext.request.contextPath}/js/scripts.js"></script>
                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                <script>
                    function changeProfilePic(file) {
                        var img = document.getElementById("avatarPicture");
                        img.src = window.URL.createObjectURL(file);
                    }
                </script>
            </body>

            </html>