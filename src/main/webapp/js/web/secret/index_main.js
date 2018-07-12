layui.use('table', function () {
    var table = layui.table;

    table.render({
        elem: '#test'
        , cellMinWidth: 80
        , url: '/demo/table/user/'
        , cols: [[
            {field: 'id', title: '卡号',minWidth: 300, sort: true}
            , {field: 'username', title: '是否使用'}
            , {field: 'sex', title: '生成日期', sort: true}
            , {field: 'city', title: '使用人'}
            , {field: 'sign', title: '生成时间'}
        ]]
        , page: true
        ,limit: 10
        ,limits: [10,20,30,40,50]
        ,id: 'test'
    });
});