/**
 * AJAX POST请求 JSON
 * @param url   请求地址
 * @param data  传输数据
 * @param funsu 成功执行方法
 * @param funer 失败执行方法
 * @param sync  是否异步传输 默认是true是异步。 false就是同步传输
 */

function sendAjax(url, data, funsu, sync) {
    var shadow;
    $.ajax({
        sync: sync,
        cache: false,
        type: 'POST',
        url: url,
        dataType: 'json',
        data: data,
        beforeSend: function () {
            shadow = layer.load(1, {shade: 0.6,}); //换了种风格
        },
        complete: function () {
            layer.close(shadow);
        },
        error: function () {
            msgError("系统错误,请联系管理员");
        },
        success: function (data) {
            funsu(data);
        }
    });
}

/**
 *消息
 * @param content
 */
function msgInfo(content) {
    layer.msg(content, {
        icon: 6,
        time: 2000
    });
}

function msgWarn(content) {
    layer.msg(content, {
        icon: 7,
        time: 2000
    });
}

function msgError(content) {
    layer.msg(content, {
        icon: 5,
        time: 2000
    });
}

function msgTips(content, id) {
    layer.tips(content, '#' + id);
}

/**
 * 表单清空
 * @param id
 */
function formReset(id) {
    var target = $('#' + id);
    var t = target[0];
    if (t) {
        t.reset();
    }
}