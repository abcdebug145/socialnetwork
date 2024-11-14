<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<div class="modal" id="CreatePostModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="my-4 model-title">Create Post</h3>
            </div>
            <%--@elvariable id="newPost" type="com.project.socialnetwork.domain.Post"--%>
            <form:form action="/create-post" method="post" enctype="multipart/form-data" modelAttribute="newPost">
                <div class="modal-body">
                    <div class="row mb-3">
                        <label for="title" class="col-sm-3 col-form-label">Title:</label>
                        <div class="col-sm-9">
                            <form:input type="text" class="form-control" id="title" path="title"/>
                        </div>
                    </div>
                    <div class="row mb-3">
                        <label class="col-sm-3 col-form-label">Picture:</label>
                    </div>
                    <div class="row mb-3">
                        <img src="" id="postPicture"/>
                        <div class="col-sm-9">
                            <input type="file" id="fileChooser" onchange="uploadImage(this.files[0])"
                                   accept="image/*" style="display: none" name="postFile"/>
                            <button class="btn btn-outline-primary my-3" type="button"
                                    onclick="document.querySelector('#fileChooser').click()">
                                Upload Picture
                            </button>
                        </div>
                    </div>
                    <div class="d-grid gap-2">
                        <button type="submit" name="" id="" class="btn btn-primary">Post
                        </button>
                    </div>
                </div>
            </form:form>
        </div>
    </div>
</div>