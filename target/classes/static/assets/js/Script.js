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

$(document).ready(function () {
    $('.increment-btn').click(function () {
        var inputElement = $(this).closest('.input-group').find('.quantity-input');
        var newValue = parseInt(inputElement.val()) + 1;
        inputElement.val(newValue).trigger('change');
    });

    $('.decrement-btn').click(function () {
        var inputElement = $(this).closest('.input-group').find('.quantity-input');
        var newValue = parseInt(inputElement.val()) - 1;
        if (newValue >= 1) {
            inputElement.val(newValue).trigger('change');
        }
    });

    $('.quantity-input').blur(function () {
        var inputElement = $(this);
        var currentValue = inputElement.val();
        if (currentValue === '') {
            inputElement.val('1');
            inputElement.change(); // Trigger the 'change' event to submit the form
        }
    });
});