//注意：导航 依赖 element 模块，否则无法进行功能性操作
layui.use('element', function () {
    var element = layui.element;
});

layui.use('form', function () {
    var form = layui.form; //只有执行了这一步，部分表单元素才会自动修饰成功

    //……

    //但是，如果你的HTML是动态生成的，自动渲染就会失效
    //因此你需要在相应的地方，执行下述方法来手动渲染，跟这类似的还有 element.init();
    form.render();
});
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
});

function sendValidateCode() {
    var data = {};
    data.sendTo = $('#eamil').val();
    data.sendType = '0';
    data.codeType = '0';
    if (data.sendTo == "" || !data.sendTo) {
        msgTips("你还没有填写呢", 'eamil');
        return;
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
    var data = {};
    data.userName = $('#userName').val();
    data.userPass = $('#userPass').val();
    data.reUserPass = $('#reUserPass').val();
    data.email = $('#eamil').val();
    data.validCode = $('#validCode').val();
    var vaild = validForm(data);
    if(!vaild.result){
        return;
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

function validForm() {
    if (data.userName == "" || !data.userName) {
        msgTips("请填写用户名", 'userName');
        return false;
    }
    if (regular(data.userName,regularExpression.userName)) {
        msgTips("用户名格式不正确", 'userName');
        return false;
    }
    if (data.userPass == "" || !data.userPass) {
        msgTips("请填写密码", 'userPass');
        return false;
    }
    if (regular(data.userPass,regularExpression.userPass)) {
        msgTips("密码格式不正确", 'userName');
        return false;
    }
    if (data.userPass != data.reUserPass) {
        msgTips("两次输入密码不一样", 'reUserPass');
        return false;
    }
    if (data.email == "" || !data.email) {
        msgTips("请填写邮箱", 'eamil');
        return false;
    }
    if (data.validCode == "" || !data.validCode) {
        msgTips("请输入收到的验证码", 'validCode');
        return false;
    }
}
