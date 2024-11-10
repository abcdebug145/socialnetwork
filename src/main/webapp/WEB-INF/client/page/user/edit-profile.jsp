<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <meta name="description" content=""/>
    <meta name="author" content=""/>
    <title>User</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>

<body onload="setEnableOnChange()">
<div class="container row">
    <div class="col-sm-1 container-fluid">
        <jsp:include page="../layout/sidebar.jsp"/>
    </div>
    <div class="col-sm-11">
        <h5 class=" fs-3 mb-3">Edit Profile</h5>
        <p class="fs-5">Keep your personal details private.
            Information you add here is visible to anyone who can view your profile.</p>
        <div class="container">
            <p>Photo</p>
        </div>
        <div class="d-flex container">
            <img class="rounded-circle"
                 id="avatarPicture" width="100" height="100" src="https://placehold.co/200x200">
            <input id="avatarInput" type="file" value="avatar" accept=".png,.jpeg,.jpg,.svg"
                   class="form-control" onchange="changeProfilePic(this.files[0])"
                   style="display: none"/>
            <button type="button" class="btn btn-primary rounded-pill my-4 ms-2"
                    onclick="document.getElementById('avatarInput').click()">
                Change
            </button>
        </div>
        <div class="container">
            <form:form action="/profile/edit-profile" method="post" modelAttribute="account">
            <div class="mb-3">
                <label for="fullname" class=" col-form-label">Full Name:</label>
                <div class="row">
                    <div class="col-6">
                        <input type="text" class="form-control rounded-pill border-3 border" id="fullname"
                        "path="fullName"/>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="about" class=" col-form-label">About:</label>
                    <div class="row">
                        <div class="col-6">
                            <textarea id="about" placeholder="Tell us about yourself"
                                      class="form-control rounded-pill border-3 border"
                                      style="resize:none"></textarea>
                        </div>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="username" class="col-form-label">Username:</label>
                    <div class="row">
                        <div class="col-6">
                            <input type="text" class="form-control rounded-pill border-3 border" id="username"
                                   path="username"
                                   value="<%= session.getAttribute("username").toString().split("@")[0] %>"/>
                        </div>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="address" class="col-sm-3 col-form-label">Address(private):</label>
                    <div class="row">
                        <div class="col-6">
                            <input type="text" class="form-control rounded-pill border-3 border" id="Address"
                                   path="address"/>
                        </div>
                    </div>
                </div>
                <h5 class=" fs-3 mb-3">Account management</h5>
                <p class="fs-5">Make changes to your personal information or account type.</p>
                <div class="mb-5">
                    <label for="email" class="col-form-label">Email:</label>
                    <div class="row">
                        <div class="col-6">
                            <input type="email" class="form-control rounded-pill border-3 border" id="email"
                                   value="<%=session.getAttribute("username")%>" path="email"/>
                        </div>
                    </div>
                    <label for="password" class="col-form-label">Password:</label>
                    <div class="row mb-5">
                        <div class="col-4">
                            <input type="password" class="form-control rounded-pill border-3 border" id="password"
                                   path="passsword"/>
                        </div>
                        <div class="col-2 ">
                            <button type="button" class="btn btn-secondary rounded-pill ms-2">
                                Change Password
                            </button>
                        </div>
                    </div>
                </div>
                </form:form>
                <div style="height: 80px"></div>
            </div>
        </div>
    </div>
</div>
<div class="container fixed-bottom text-black d-flex bg-white justify-content-center border-top text-black">
    <div class="my-2">
        <button id="reset-button" class="btn rounded-pill fs-5 fw-normal text-black" type="reset" disabled
                style="background-color:#e9e9e9">
            reset
        </button>
        <button id="save-button" class="btn btn-danger rounded-pill fs-5 fw-bold " type="submit" disabled>
            Save Changes
        </button>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous">
</script>

<script>
    function changeProfilePic(file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById('avatarPicture').src = e.target.result;
        };
        reader.readAsDataURL(file);
    }

    function setEnableOnChange() {
        const inputs = document.querySelectorAll('input');
        inputs.forEach(input => {
            input.addEventListener('change', () => {
                document.getElementById('save-button').disabled = false;
                document.getElementById('reset-button').disabled = false;
            });
        });
    }
</script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
    $(document).ready(() => {
        const avatarFile = $("#imageFile");
        avatarFile.change(function (e) {
            const imgURL = URL.createObjectURL(e.target.files[0]);
            $("#avatarPreview").attr("src", imgURL);
            $("#avatarPreview").css({"display": "block"});
        });
    });
</script>
<script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
        crossOrigin="anonymous"></script>
</script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
        crossorigin="anonymous"></script>
</body>

</html>