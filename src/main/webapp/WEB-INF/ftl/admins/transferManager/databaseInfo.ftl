<#assign contextPath=request.contextPath />

<!DOCTYPE html PUBLIC "-/W3C/DTD XHTML 1.0 Transitional/EN" "http:/www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<html>
  <head>
    <title>menu</title>
   <base id="base" href="${contextPath}">  
   <#include "/common/common.ftl">
   <script type="text/javascript"  charset="UTF-8">
   var searchUrl = "${contextPath}/transfer/databaseInfo/list/data";
   var updateUrl = "${contextPath}/departments/update.do";
   var insertUrl = "${contextPath}/transfer/databaseInfo/update";
   var deleteUrl = "${contextPath}/departments/delete.do";
	$(function() {
	    $('#dataList').datagrid({  
	        title:'列表',  
	        iconCls:'icon-edit',//图标  
	        //width: 700,  
	        height: 'auto',  
	        nowrap: false,  
	        striped: true,  
	        border: true,  
	        collapsible:false,//是否可折叠的  
	        fit: true,//自动大小  
	        url:'#',  
	        remoteSort:false,   
	        singleSelect:true,//是否单选  
	        pagination:true,//分页控件  
	        rownumbers:true,//行号  
	        url:searchUrl, 
	        toolbar:'#tb',
	        columns:[[   
                   		{field:'id',title:'编号',width:100,align:'center'},
                   		{field:'sourceName',title:'数据源名称',width:500,align:'center'},
                   		{field:'tableName',title:'表名',width:300,align:'center'},
                   		{field:'recordCount',title:'表记录数',width:100,align:'center'},
						{field:'recordEalyDate',title:'表记录最早时间',width:150,align:'center',formatter:function(value,row,index) {
								if (value > 0) {
									var unixTimestamp = new Date(value);
									return unixTimestamp.getFullYear() + "-" + (unixTimestamp.getMonth() + 1) + "-" + unixTimestamp.getDate()+" "+unixTimestamp.getHours()+":"+unixTimestamp.getMinutes()+":"+unixTimestamp.getSeconds();
								}else{
									return "";
								}
							}
						},
						{field:'recordLateDate',title:'表记录最晚时间',width:150,align:'center',formatter:function(value,row,index) {
								if (value > 0) {
									var unixTimestamp = new Date(value);
									return unixTimestamp.getFullYear() + "-" + (unixTimestamp.getMonth() + 1) + "-" + unixTimestamp.getDate()+" "+unixTimestamp.getHours()+":"+unixTimestamp.getMinutes()+":"+unixTimestamp.getSeconds();
								}else{
									return "";
								}
							}
						}
	        ]],
	        
	         onBeforeLoad: function (params) {
			      params.pageSize = params.rows
			      params.currentPage = params.page
			      delete params.rows
			      delete params.page
			 }
	        
	    });  
	
	    //设置分页控件  
	    var p = $('#dataList').datagrid('getPager');  
	    $(p).pagination({  
	        pageSize: 10,//每页显示的记录条数，默认为10  
	        pageList: [10,20,30],//可以设置每页记录条数的列表  
	        beforePageText: '第',//页数文本框前显示的汉字  
	        afterPageText: '页    共 {pages} 页',  
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
	    });


        $.ajax({
            type: "GET",
            url: "${contextPath}/datasource/all",
            data: {type:2},
            dataType: "json",
            success: function(datas){
                for(var i=0;i<datas.length;i++){
                    var data=datas[i];
                    $("#sourceId").append("<option value='"+data.id+"'>"+data.name+"</option>")
					$("#querySourceId").append("<option value='"+data.id+"'>"+data.name+"</option>")
                }
            }
        });

	});


	</script>
  </head>
  
  <body class="easyui-layout" >
	<div  region="center" >
		<div id='dataList'>
			<div id="tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="showAddwindow({title:'更新'});">更新</a>|
		</div>
		<div>
			<form  id='searchForm' action="" method="post">
				数据源:
                <select id="querySourceId"  name="sourceId">

                </select>
				<input type="button" onclick="loadList(1);" value="查询"/>
			</form>
		</div>
	</div>
		</div>
	</div>
	
	<div style="visibility:hidden" >
		<div id="addwindow"  title="添加" style="width:600px;height:350px;padding:10px">
			<form id='addForm' action="" method="post">
				<table>
						<tr>
							<td>数据源:</td>
							<td>
								<select id="sourceId"  name="sourceId">

								</select>
							</td>
						</tr>

						
				</table>
			</form>
		</div>

	</div>

	
  </body>
</html>
