const index = {
    init: function () {
        let _this = this;
        $("#sign-up").on("click", function () {
            _this.signUp();
        });

        $("#sign-in").on("click", function () {
            _this.signIn();
        });

        $("#uploadBtn").on("click", function (event) {
           _this.uploadVideo(event);
        });
    },
    signUp: function () {
        let data = {
            name: $("#name").val(),
            email: $("#email").val(),
            password: $("#password").val(),
            phoneNumber: $("#phoneNumber").val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/members/signUp',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            success: function (data) {
                alert('회원가입이 되었습니다. 축하드립니다.');
                console.log(data);
                window.location.href = '/';
            },
            error: function (error) {
                alert(JSON.stringify(error));
            }
        });
    },

    signIn: function () {
        let data = {
            email: $("#email").val(),
            password: $("#password").val(),
        };

        $.ajax({
            type: 'POST',
            url: '/api/members/signIn',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            success: function (data) {
                alert(data.token);
                localStorage.setItem("token", data.token);
                window.location.href = '/';
            },
            error: function (error) {
                alert(JSON.stringify(error));
            }
        });
    },

    uploadVideo: function (event) {
        event.preventDefault();

        let form = $('#fileUploadForm')[0];
        let formData = new FormData(form);

        $("#uploadBtn").prop("disabled", true);

        $.ajax({
            url: '/api/videos',
            type: 'POST',
            enctype: 'multipart/form-data',
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            beforeSend: function (xhr) {
                const token = localStorage.getItem("token");
                console.log(token);
                xhr.setRequestHeader("Authorization",token);
            },
            success: function(data){
                alert("업로드 성공!!");
                $("#uploadBtn").prop("disabled", false);
            },
            error: function (error) {
                alert(JSON.stringify(error));
                $("#uploadBtn").prop("disabled", false);
            }
        });
    }
};

index.init();
