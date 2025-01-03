<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <div class="modal" id="CreatePostModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl">
                <div class="modal-content w-75 mx-auto">
                    <div class="modal-header">
                        <h3 class="my-4 model-title">Create Post</h3>
                    </div>
                    <form:form action="/create-post" method="post" enctype="multipart/form-data"
                        modelAttribute="newPost" id="create-post-form">
                        <div class="modal-body">
                            <div class="row mb-3">
                                <label for="title" class="col-sm-3 col-form-label">Title:</label>
                                <div class="col-sm-9">
                                    <form:input type="text" class="form-control" id="title" path="title" />
                                </div>
                            </div>
                            <div class="row mb-3">
                                <label class="col-sm-3 col-form-label">Picture:</label>
                            </div>
                            <div class="row mb-3">
                                <img src="" id="postPicture" />
                                <div class="col-sm-9">
                                    <input type="file" id="fileChooser" onchange="uploadImage(this.files[0])"
                                        accept="image/*" style="display: none" name="postFile" />
                                    <button class="btn btn-outline-primary my-3" type="button"
                                        onclick="document.querySelector('#fileChooser').click()">
                                        Upload Picture
                                    </button>
                                </div>
                            </div>
                            <div class="d-grid gap-2 d-flex justify-content-center">
                                <button name="" id="" class="btn btn-primary w-25" onclick="handle()">Post</button>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
        <div class="modal" id="NotificationModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl">
                <div class="modal-content w-75 mx-auto">
                    <div class="modal-header">
                        <h3 class="my-4 model-title">Notification</h3>
                    </div>
                    <div class="modal-body" id="noti-container">
                        <c:forEach var="notification" items="${notifications}">
                            <c:choose>
                                <c:when test="${notification.read eq true}">
                                    <div class="row border border-info mb-3">
                                </c:when>
                                <c:otherwise>
                                    <div class="row border border-info mb-3" style="background-color: lightblue;">
                                </c:otherwise>
                            </c:choose>
                            <div class="col-sm-3">
                                <img src="/images/avatar/${notification.account.avatar}" alt="avatar" height="50"
                                    width="50">
                            </div>
                            <div class="col-sm-9">
                                <p>${notification.message}</p>
                            </div>
                            <div>
                                ${notification.timeAgo}
                            </div>
                    </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        </div>
        <script>
            function handle() {
                document.getElementById('create-post-form').submit();
            }
        </script>