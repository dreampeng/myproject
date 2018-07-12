//-----------------------------layui start-----------------------------------
//注意：导航 依赖 element 模块，否则无法进行功能性操作
layui.use('form', function () {
    var form = layui.form;
    form.render();

    //登录按钮
    $("#submit").on('click', function () {
        submit();
    });


    function submit() {

        var data = {};
        data.userName = $('#userName').val();
        data.userPass = $('#userPass').val();
        data.validCode = $('#validCode').val();
        if (!validForm(data)) {
            return false;
        }
        sendAjax("/user/login", data, function (retData) {
            var retCode = retData.code;
            if (retCode == "0000") {
                msgInfo("欢迎回家！");
            } else {
                msgWarn(retData.msg);
            }
        }, true);
    }

    function validForm(data) {
        if (data.userName == "" || !data.userName) {
            msgTips("请填写用户名", 'userName');
            return false;
        }
        if (regular(data.userName, regularExpression.userName)) {
            msgTips("用户名格式不正确", 'userName');
            return false;
        }
        if (data.userPass == "" || !data.userPass) {
            msgTips("请填写密码", 'userPass');
            return false;
        }
        if (regular(data.userPass, regularExpression.userPass)) {
            msgTips("密码格式不正确", 'userPass');
            return false;
        }
        if (data.validCode == "" || !data.validCode) {
            msgTips("请输入验证码", 'validCode');
            return false;
        }
        return true;
    }
});