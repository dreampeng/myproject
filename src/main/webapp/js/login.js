//-----------------------------layui start-----------------------------------
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
//-----------------------------layui end-----------------------------------
$(function () {

    //登录按钮
    $("#submit").on('click', function () {
        submit();
    });
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
        msgTips("密码格式不正确", 'userName');
        return false;
    }
    if (data.validCode == "" || !data.validCode) {
        msgTips("请输入验证码", 'validCode');
        return false;
    }
    return true;
}
