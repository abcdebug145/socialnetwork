<div class="modal fade" id="reportModal" tabindex="-1" aria-labelledby="reportModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="reportModalLabel">Report Post</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="/submitReport" method="post">
                <div class="modal-body">
                    <div class="post-content text-center">
                        <img id="reportModalImage" src="" alt="Post Image" class="img-fluid mb-3">
                        <h6 id="reportModalTitle"></h6>
                    </div>
                    <input type="hidden" name="postId" id="reportModalPostId" value="">
                    <div class="mb-3">
                        <label for="reportReason" class="form-label">Reason for reporting</label>
                        <textarea class="form-control" id="reportReason" name="reason" rows="4" placeholder="Explain your reason..."></textarea>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-danger">Submit Report</button>
                </div>
            </form>
        </div>
    </div>
</div>
