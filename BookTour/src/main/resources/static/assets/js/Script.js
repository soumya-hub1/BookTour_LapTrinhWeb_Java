//Preview image before upload
$(document).ready(function () {
    function showPreviewImage(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#imagePreview').attr('src', e.target.result);
            };
            reader.readAsDataURL(input.files[0]);
        }
    }

    $('#imageFile').change(function () {
        showPreviewImage(this);
    });
});


//Validate form change password
$(document).ready(function () {
    $('#changePasswordForm').validate({
        rules: {
            currentPassword: {
                required: true
            },
            newPassword: {
                minlength: 6
            },
            confirmNewPassword: {
                confirmation: true
            }
        },
        messages: {
            currentPassword: {
                required: 'Vui lòng nhập mật khẩu hiện tại'
            },
            newPassword: {
                minlength: 'Mật khẩu mới phải có ít nhất 6 ký tự'
            },
            confirmNewPassword: {
                confirmation: 'Xác nhận mật khẩu không khớp'
            }
        },
    });
});
