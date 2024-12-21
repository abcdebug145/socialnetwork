$(document).ready(function () {
    connect('admin');

    function attachClickHandler() {
        $('.btn-change-status').off('click').on('click', function (event) {
            var selectedId = $(this).attr("id-account");
            var selectedUsername = $(this).closest('tr').find('.username-col').text().trim();
            var currStatus = $(this).attr("curr-status");
            var currSpan = $(this).closest('tr').find('td:has(span[data-bs-toggle="tooltip"])');
            var currBtn = $(this).closest('tr').find('td:has(a.btn-change-status)');
            $.ajax({
                url: '/admin/api-changeStatus-account?id=' + selectedId + '&currStatus=' + currStatus,
                type: 'PUT',
                success: function () {
                    if (currStatus === "ACTIVE") {
                        currSpan.html('<span style="color: red; font-size: 30px;" data-bs-toggle="tooltip" title="Account is banned">●</span>');
                        currBtn.html('<a href="/profile/' + selectedUsername + '" type="button" style="width: 95px;" class="btn btn-success">View</a>' +
                            '<a type="button" style="width: 95px; background-color: #96fa09" class="btn btn-success btn-delete btn-change-status" id-account="' + selectedId + '" curr-status="BANNED">\nUnban\n</a>'
                        );
                        stompClient.send("/app/ban", {}, selectedUsername);
                    }
                    if (currStatus === "BANNED") {
                        currSpan.html('<span style="color: greenyellow; font-size: 30px;" data-bs-toggle="tooltip" title="Account is active">●</span>');
                        currBtn.html('<a href="/profile/' + selectedUsername + '" type="button" style="width: 95px;" class="btn btn-success">View</a>' +
                            '<a type="button" style="width: 95px;" class="btn btn-danger btn-delete btn-change-status" id-account="' + selectedId + '" curr-status="ACTIVE">\nBan\n</a>'
                        );
                    }
                    attachClickHandler();
                }
            });
        });
    }

    attachClickHandler(); // Initial attachment of the click handler
});

var stompClient = null;

function connect(username) {
    var socket = new SockJS('/noti');
    stompClient = Stomp.over(socket);
    stompClient.connect({ username: username, }, function () {
        console.log('Web Socket is connected');
        stompClient.subscribe('/users/queue/messages', function (message) {

        });
    });
}