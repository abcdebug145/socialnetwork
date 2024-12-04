$(document).ready(function () {
    $('.btn-change-status').click(function (event) {
        var selectedId = $(this).attr("id-account");
        var currStatus = $(this).attr("curr-status") == 'ACTIVE' ? 1 : 2;
        var currSpan = $(this).closest('tr').find('td:has(span[data-bs-toggle="tooltip"])');
        var currBtn = $(this).closest('tr').find('td:has(a.btn-change-status)');

        $.ajax({
            url: '/admin/api-changeStatus-account?id=' + selectedId + '&currStatus=' + currStatus,
            type: 'PUT',
            success: function () {
                if (currStatus == 1) {
                    currSpan.html('<span style="color: red; font-size: 30px;" data-bs-toggle="tooltip" title="Account is banned">●</span>');
                    currBtn.html('<a href="/admin/accounts/account-details?id=' + selectedId + '" type="button" style="width: 95px;" class="btn btn-success">View</a>' +
                        '<a type="button" style="width: 95px; background-color: #96fa09" class="btn btn-success btn-delete btn-change-status" id-account="' + selectedId + '" curr-status="BANNED">\nUnban\n</a>'
                    );
                }
                if (currStatus == 2) {
                    currSpan.html('<span style="color: greenyellow; font-size: 30px;" data-bs-toggle="tooltip" title="Account is active">●</span>');
                    currBtn.html('<a href="/admin/accounts/account-details?id=' + selectedId + '" type="button" style="width: 95px;" class="btn btn-success">View</a>' +
                        '<a type="button" style="width: 95px;" class="btn btn-danger btn-delete btn-change-status" id-account="' + selectedId + '" curr-status="ACTIVE">\nBan\n</a>'
                    );
                }
                // window.location.reload();
            }
        });
    });
});
