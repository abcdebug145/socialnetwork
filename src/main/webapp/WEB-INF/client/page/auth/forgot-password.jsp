<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reset Password</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        #message {
            display: none;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header text-center">
                    <h4>Reset Your Password</h4>
                </div>
                <div class="card-body">
                    <form id="resetForm" action="${pageContext.request.contextPath}/request-password-reset" method="post">
                        <div class="mb-3">
                            <label for="email" class="form-label">Email Address</label>
                            <input type="email" class="form-control" id="email" name="email" placeholder="Enter your email" required>
                        </div>
                        <div class="d-grid">
                            <button type="submit" id="sendButton" class="btn btn-primary">Send Password Reset Email</button>
                        </div>
                    </form>
                    <div id="message" class="mt-3 alert" role="alert"></div>
                    <div id="countdown" class="mt-2 text-center"></div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    const form = document.getElementById('resetForm');
    const sendButton = document.getElementById('sendButton');
    const messageDiv = document.getElementById('message');
    const countdownDiv = document.getElementById('countdown');

    form.addEventListener('submit', async (event) => {
        event.preventDefault(); // Prevent default form submission

        // Disable the button
        sendButton.disabled = true;
        let countdown = 60;
        countdownDiv.innerText = `Please wait ${countdown} seconds before trying again.`;

        const countdownInterval = setInterval(() => {
            countdown -= 1;
            if (countdown <= 0) {
                clearInterval(countdownInterval);
                sendButton.disabled = false;
                countdownDiv.innerText = ''; // Clear the countdown message
            } else {
                countdownDiv.innerText = `Please wait ${countdown} seconds before trying again.`;
            }
        }, 1000);

        try {
            // Simulate form submission using Fetch API
            const formData = new FormData(form);
            const response = await fetch(form.action, {
                method: 'POST',
                body: formData
            });

            if (response.ok) {
                messageDiv.classList.add('alert-success');
                messageDiv.classList.remove('alert-danger');
                messageDiv.innerText = 'Email sent successfully! Please check your inbox.';
            } else {
                messageDiv.classList.add('alert-danger');
                messageDiv.classList.remove('alert-success');
                messageDiv.innerText = 'Failed to send the email. Please try again later.';
            }
        } catch (error) {
            messageDiv.classList.add('alert-danger');
            messageDiv.classList.remove('alert-success');
            messageDiv.innerText = 'An error occurred while sending the email.';
        }

        messageDiv.style.display = 'block'; // Show the message
    });
</script>
</body>
</html>
