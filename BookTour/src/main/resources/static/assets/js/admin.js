$(document).ready(function () {
    const sidebarToggle = $('#sidebarToggle');
    if (sidebarToggle.length) {
        sidebarToggle.on('click', function (event) {
            event.preventDefault();
            $('body').toggleClass('sb-sidenav-toggled');
            localStorage.setItem('sb|sidebar-toggle', $('body').hasClass('sb-sidenav-toggled'));
        });
    }
});

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