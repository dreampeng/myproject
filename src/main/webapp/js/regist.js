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
});

function sendValidateCode() {
    var data = {};
    data.email = $('#eamil').val();
    data.sendType = '0';
    data.codeType = '0';
    sendAjax("/validate/getcode", data, function (retData) {

    }, function (retData) {

    }, true);
}

function submit() {

}