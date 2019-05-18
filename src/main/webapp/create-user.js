function createUser() {

    var formData = $('#registery').serializeFormJSON();

    $.ajax({
        url: '/users/create',
        method: 'post',
        dataType: 'json',
        data : {
            map: JSON.stringify(formData)
        },
        success: function (json) {

        }
    });

    ;

}