const index = {
    init: function() {
        let _this = this;
        $("#sign-up").on("click", function () {
            _this.signUp();
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
           url: '/api/members',
           dataType: 'json',
           contentType: 'application/json; charset=utf-8',
           data: JSON.stringify(data)
        }).done(function (){
            alert('회원가입이 완료되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

index.init();
