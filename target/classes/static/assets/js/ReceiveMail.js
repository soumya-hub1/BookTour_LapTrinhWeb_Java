$(document).ready(function () {
    $('#sendmail').submit(function (event) {
        event.preventDefault(); // Ngăn chặn việc gửi form mặc định

        var email = $('input[name="email"]').val(); // Lấy giá trị email từ input

        $.ajax({
            url: '/',
            type: 'POST',
            data: {email: email}, // Dữ liệu gửi đi
            success: function (response) {
                $('#errorMessage').empty(); // Xóa thông báo lỗi trước đó
                $('#errorMessage').removeClass('alert alert-danger').addClass('alert alert-success');
                $('#errorMessage').text(response); // Hiển thị thông báo thành công
            },
            error: function (xhr, status, error) {
                var errorMessage = xhr.responseText; // Lấy thông báo lỗi từ phản hồi
                $('#errorMessage').removeClass('alert alert-success').addClass('alert alert-danger');
                $('#errorMessage').text(errorMessage); // Hiển thị thông báo lỗi
            }
        });
    });
});