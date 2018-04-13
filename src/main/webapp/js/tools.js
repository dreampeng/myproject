/**
 * AJAX POST请求 JSON
 * @param url   请求地址
 * @param data  传输数据
 * @param funsu 成功执行方法
 * @param funer 失败执行方法
 * @param sync  是否异步传输 默认是true是异步。 false就是同步传输
 */

function sendAjax(url, data, funsu, funer, sync) {
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
        error: function (data) {
            if (data == undefined || data == null) {
                layer.msg('网络链接异常', {
                    time: 5000, //20s后自动关闭
                    btn: ['哦']
                });
            }
            funer(data);
        },
        success: function (data) {
            funsu(data);
        }
    });
}