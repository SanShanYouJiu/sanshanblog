
$(function () {
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});


$("#exampleTableToolbar3").bootstrapTable({
    url: "/County/PagingFindAll",//具体请求地址
    method: 'post',//请求方式
    cache: false,                      //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
    pagination: true,                  //是否显示分页（*）
    search: true,//是否开启搜索框
    showRefresh: true,//是否显示刷新框
    sidePagination: "server",          //分页方式：client客户端分页，server服务端分页（*）
    queryParams: queryParams,//传递参数（*）
    pageSize: 1,                      //每页的记录行数（*）
    showColumns: true,//是否显示columns按钮
    //toolbar: "#exampleToolbar",//对用的toolbar
    iconSize: "outline",
    sortOrder:"desc",
    icons: {refresh: "glyphicon-repeat", columns: "glyphicon-list"},//对应按钮对应的字体图标
    uniqueId: "ID"//主键id
});



function queryParams(params) {
    return {
        limit:params.limit,
        offset:params.offset,
        order:params.order,
        ordername:params.sort,
        search:params.search
    };
}

