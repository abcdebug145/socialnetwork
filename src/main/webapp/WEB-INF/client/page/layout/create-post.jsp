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
        <div class="modal fade" id="NotificationModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl">
                <div class="modal-content shadow-lg rounded">
                    <div class="modal-header bg-primary text-white">
                        <i class="bi bi-bell-fill fs-3"></i>
                        <h3 class="ms-2 my-4 model-title">Notifications</h3>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body bg-light" id="noti-container">
                    </div>
                </div>
            </div>
        </div>

        <script>
            function handle() {
                document.getElementById('create-post-form').submit();
            }
        </script>