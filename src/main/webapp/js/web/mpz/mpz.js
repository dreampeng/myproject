//-----------------------------layui start-----------------------------------
let c;
//注意：导航 依赖 element 模块，否则无法进行功能性操作
layui.use('form', function () {
    let form = layui.form;
    form.render();
    $("#submitMpz").on('click', function () {
        let qq = $("#qqNum").val();
        if (qq !== "") {
            add(qq);
        } else {
            msgTips("请输入QQ号码", 'qqNum');
        }
    });
    setInterval(getAllWait, 3000);
});

function add(qq) {
    sendAjax("/japi/mpz/add/" + qq, null, function (retData) {
        if (retData.code === "0000") {
            msgInfo(retData.msg);
        } else {
            msgWarn(retData.msg);
        }
    }, true);
}

function getAllWait() {
    sendAjax("/japi/mpz/get", null, function (retData) {
        let retCode = retData.code;
        if (retCode === "0000") {
            $("#waitQqList").val(retData.data);
        } else {
            msgWarn(retData.msg);
        }
    }, true, false);
}