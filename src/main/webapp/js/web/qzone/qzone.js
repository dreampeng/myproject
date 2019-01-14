//-----------------------------layui start-----------------------------------
let c;
//注意：导航 依赖 element 模块，否则无法进行功能性操作
layui.use('form', function () {
    let form = layui.form;
    form.render();
    $("#getQrCode").on('click', function () {
        window.clearInterval(c);
        let qq = $("#qqNum").val();
        if (qq !== "") {
            $("#qqNum").attr("disabled", "disabled");
            getQrPic(qq);
        }
    });
    $("#getMzState").on('click', function () {
        let qq = $("#qqNum").val();
        if (qq !== "") {
            miaoZhan(qq);
        }
    });
});

function getQrPic(qq) {
    sendAjax("/japi/qzone/qrcode/" + qq, null, function (retData) {
        if (retData.code === "0000") {
            $("#qrShow").attr("src", "/qrcode/" + retData.path + ".png?_=" + (new Date()).getTime());
            c = setInterval(login, 3000);
        } else {
            msgWarn(retData.msg);
        }
    }, true);
}

function login() {
    let qq = $("#qqNum").val();
    sendAjax("/japi/qzone/login/" + qq, null, function (retData) {
        let retCode = retData.code;
        if (retCode === "0000") {
            $("#qrShow").hidden;
            let retQq = retData.qq;
            if (retQq) {
                switch (retQq) {
                    case "0":
                        break;
                    case "-1":
                        window.clearInterval(c);
                        msgWarn("已过期，请点击重新获取");
                        $("#qrShow").attr("src", " /img/logo.png");
                        $("#qqNum").removeAttr("disabled");
                        break;
                    default:
                        window.clearInterval(c);
                        msgInfo("登录成功");
                        miaoZhan(retQq);
                }
            } else {
                window.clearInterval(c);
                $("#qqNum").removeAttr("disabled");
                msgWarn("请稍后再试");
            }
        } else {
            window.clearInterval(c);
            $("#qqNum").removeAttr("disabled");
            msgWarn(retData.msg);
        }
    }, true, false);
}

function miaoZhan(qq) {
    sendAjax("/japi/qzone/miao/" + qq, null, function (retData) {
        let retCode = retData.code;
        if (retCode === "0000") {
            if (retData.data) {
                switch (retData.data) {
                    case 1:
                        msgInfo("已开启秒赞服务");
                        break;
                    case -1:
                        msgWarn("请重新登录");
                        break;
                }
            } else {
                $("#qqNum").removeAttr("disabled");
                msgWarn("请稍后再试");
            }
        } else {
            window.clearInterval(c);
            $("#qqNum").removeAttr("disabled");
            msgWarn(retData.msg);
        }
    }, true, false);
}