//-----------------------------layui start-----------------------------------
layui.use('form', function () {
    var form = layui.form;
    form.render();
});
//-----------------------------layui end-----------------------------------
var canReg = false;
$(function () {
    //获取验证码按钮
    $("#getValidateCode").on('click', function () {
        sendValidateCode();
    });
    //注册按钮
    $("#submit").on('click', function () {
        submit();
    });
    //重置按钮
    $("#reset").on('click', function () {
        formReset('form_regist');
    });
    $("#userName").blur(function () {
        isReg();
    });
});

function isReg() {
    var userName = $('#userName').val();
    if (userName == "" || !userName) {
        return false;
    }
    if (regular(userName, regularExpression.userName)) {
        msgTips("用户名格式不正确", 'userName');
        return false;
    }
    sendAjax("/user/isreg", {"userName": userName}, function (retData) {
        var retCode = retData.code;
        if (retCode == "0100") {
            msgTips("用户名已存在!", 'userName');
        } else if (retCode != "0000") {
            msgWarn(retData.msg);
        }
    }, true, false);
}

function sendValidateCode() {
    var data = {};
    data.sendTo = $('#email').val();
    data.sendType = '0';
    data.codeType = '0';
    if (data.sendTo == "" || !data.sendTo) {
        msgTips("你还没有填写呢", 'email');
        return;
    }
    if (regular(data.sendTo, regularExpression.email)) {
        msgTips("邮箱格式不正确", 'email');
        return false;
    }
    sendAjax("/validate/getcode", data, function (retData) {
        var retCode = retData.code;
        if (retCode == "0000") {
            msgInfo("验证码已发送,请注意查收");
        } else {
            msgWarn(retData.msg);
        }
    }, true);
}

function submit() {
    if (canReg) {
        msgTips("用户名已存在！", 'userName');
        return false;
    }

    var data = {};
    data.userName = $('#userName').val();
    data.userPass = $('#userPass').val();
    data.reUserPass = $('#reUserPass').val();
    data.email = $('#email').val();
    data.validCode = $('#validCode').val();
    if (!validForm(data)) {
        return false;
    }
    sendAjax("/user/regist", data, function (retData) {
        var retCode = retData.code;
        if (retCode == "0000") {
            msgInfo("欢迎加入！");
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
    if (data.userPass != "" && data.userPass == data.userName) {
        msgTips("不能和用户名相同哦", 'userPass');
        return false;
    }
    if (regular(data.userPass, regularExpression.userPass)) {
        msgTips("密码格式不正确", 'userName');
        return false;
    }
    if (data.userPass != data.reUserPass) {
        msgTips("两次输入密码不一样", 'reUserPass');
        return false;
    }
    if (data.email == "" || !data.email) {
        msgTips("请填写邮箱", 'email');
        return false;
    }
    if (regular(data.email, regularExpression.email)) {
        msgTips("邮箱格式不正确", 'email');
        return false;
    }
    if (data.validCode == "" || !data.validCode) {
        msgTips("请输入收到的验证码", 'validCode');
        return false;
    }
    return true;
}
