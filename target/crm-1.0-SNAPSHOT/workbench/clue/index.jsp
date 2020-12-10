<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		//日历控件
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "top-left"
		});

		//为创建按钮绑定事件
		$("#addBtn").click(function () {
			$.ajax({
				url:"workbench/clue/getUserList.do",
				type:"post",
				dataType:"json",
				success(result){
					/*
					* 	data:[{用户1},{用户2},{用户3}]
					*
					* */
					var html = '<option></option>'
						$.each(result,function (i,n) {
							html+='<option value="'+n.id+'">'+n.name+'</option>'
						})
						$("#create-Owner").html(html);
						$("#create-Owner").val("${sessionScope.user.id}");
				}
			})

			$("#createClueModal").modal("show");

		})

		//为保存按钮绑定事件,执行线索添加操作
		$("#saveBtn").click(function () {
			$.ajax({
				url: "workbench/clue/saveClue.do",
				type:"post",
				data:{
					fullname:$.trim($("#create-fullname").val()),
					appellation:$.trim($("#create-appellation").val()),
					owner:$.trim($("#create-Owner").val()),
					company:$.trim($("#create-company").val()),
					job:$.trim($("#create-job").val()),
					email:$.trim($("#create-email").val()),
					phone:$.trim($("#create-phone").val()),
					website:$.trim($("#create-website").val()),
					mphone:$.trim($("#create-mphone").val()),
					state:$.trim($("#create-state").val()),
					source:$.trim($("#create-source").val()),
					description:$.trim($("#create-description").val()),
					contactSummary:$.trim($("#create-contactSummary").val()),
					nextContactTime:$.trim($("#create-nextContactTime").val()),
					address:$.trim($("#create-address").val())
				},
				dataType:"json",
				success(result) {
					/*
					*
					* 	data{"success"：true/false}
					*
					*
					* */

					if(result.success){
						alert("添加线索成功");
						//刷新列表
						pageList(1,2);
					}else{
						alert("添加线索失败");
					}
				}

			})
			var clueAddForm = $("#clueAddForm")[0];
			clueAddForm.reset();
			$("#createClueModal").modal("hide");
		})

		//为查询按钮绑定事件，执行条件查询操作
		$("#searchBtn").click(function () {
			$("#hidden-fullname").val($.trim($("#search-name").val()))
			$("#hidden-company").val($.trim($("#search-company").val()))
			$("#hidden-phone").val($.trim($("#search-phone").val()))
			$("#hidden-source").val($.trim($("#search-source").val()))
			$("#hidden-owner").val($.trim($("#search-owner").val()))
			$("#hidden-mPhone").val($.trim($("#search-mPhone").val()))
			$("#hidden-state").val($.trim($("#search-state").val()))
			pageList(1,2);
		})

		//全选、全不选
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked",this.checked);
		})

		//全选、全不选2
		$("#tBody").on("click",$(":checkbox[name=xz]"),function () {

			$("#qx").prop("checked",$(":checkbox[name=xz]").length==$(":checkbox[name=xz]:checked").length)

		})

		//为删除按钮绑定事件，执行删除线索操作
		$("#deleteBtn").click(function () {
			var $xz = $(":checkbox[name=xz]:checked");
			if($xz.length==0){
				alert("请选择至少一条线索")
			}else{
				var param = "";
				$.each($xz,function (i,n) {
					param+="id="+n.value;
					if(i<$xz.length-1){
						param+="&"
					}
				})
				$.ajax({
					url:"workbench/clue/deleteClue.do",
					type:"get",
					data:param,
					dataType:"json",
					success(result){
						if(result.success){
							alert("删除成功");
							pageList(1,$("#cluePage").bs_pagination('getOption', 'rowsPerPage'));
							/*
						* 	$("#activityPage").bs_pagination('getOption', 'currentPage'):
						* 		操作后停留在当前页
						*
						* 	$("#activityPage").bs_pagination('getOption', 'rowsPerPage'):
						* 		操作后维持已经设置好的每页展现的记录数
						*
						* 	这两个参数不需要我们进行任何的修改操作
						* 		直接使用即可
						*
						* */
						}
					}
				})

			}
		})

		//为修改按钮绑定事件，打开模态窗口
		$("#editBtn").click(function () {
			var $xz = $(":checkbox[name=xz]:checked");
			if($xz.length==0){
				alert("请选择至少一条线索进行修改");
			}else if($xz.length>1){
				alert("只能选择一条线索进行修改");
			}else{
				$.ajax({
					url:"workbench/clue/getUserListAndClue.do",
					type:"get",
					data:{
						id:$xz.val()
					},
					dataType:"json",
					success(result){
						//data:{userList:[{用户1},{用户2},{用户3},{....}],clue:{id,owner,....}}
						//处理所有者下拉框
						var html = '<option></option>';
						$.each(result.userList,function (i,n) {
							html+='<option value="'+n.id+'">'+n.name+'</option>'
						})
						$("#edit-owner").html(html);
						//处理其他数据
						$("#edit-id").val(result.clue.id);
						$("#edit-company").val(result.clue.company);
						$("#edit-owner").val(result.clue.owner)
						$("#edit-name").val(result.clue.fullname);
						$("#edit-appellation").val(result.clue.appellation);
						$("#edit-job").val(result.clue.job);
						$("#edit-email").val(result.clue.email);
						$("#edit-phone").val(result.clue.phone);
						$("#edit-website").val(result.clue.website);
						$("#edit-mphone").val(result.clue.mphone);
						$("#edit-state").val(result.clue.state);
						$("#edit-source").val(result.clue.source);
						$("#edit-description").val(result.clue.description);
						$("#edit-contactSummary").val(result.clue.contactSummary);
						$("#edit-nextContactTime").val(result.clue.nextContactTime);
						$("#edit-address").val(result.clue.address);
						$("#editClueModal").modal("show")
					}
				})
			}

		})

		//为更新按钮绑定事件,执行线索更新操作
		$("#updateBtn").click(function () {
			alert("更新按钮事件还没做，有空的话回来完善吧！")
		})

		pageList(1,2);

	});

	function pageList(pageNo,pageSize) {

		$("#qx").prop("checked",false);
		$(":checkbox[name=xz]:checked").prop("checked",false)

		$("#search-name").val($("#hidden-fullname").val());
		$("#search-company").val($("#hidden-company").val())
		$("#search-phone").val($("#hidden-phone").val())
		$("#search-source").val($("#hidden-source").val())
		$("#search-owner").val($("#hidden-owner").val())
		$("#search-mPhone").val($("#hidden-mPhone").val())
		$("#search-state").val($("#hidden-state").val())

		$.ajax({
			url:"workbench/clue/pageList.do",
			type:"get",
			data: {
				pageNo:pageNo,
				pageSize:pageSize,
				fullname:$("#search-name").val(),
				company:$("#search-company").val(),
				phone:$("#search-phone").val(),
				source:$("#search-source").val(),
				owner:$("#search-owner").val(),
				mPhone:$("#search-mPhone").val(),
				state:$("#search-state").val()

			},

			dataType:"json",
			success(result){
				/*
				* 	{"total":100,"clueList":[{线索1},{2},{3}]}
				*
				* */
			var html = '';
			$.each(result.dataList,function (i,n) {
				html+='<tr>';
				html+='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';
				html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/clue/getDetail.do?id='+n.id+'\';">'+n.fullname+'</a></td>';
				html+='<td>'+n.company+'</td>';
				html+='<td>'+n.phone+'</td>';
				html+='<td>'+n.mphone+'</td>';
				html+='<td>'+n.source+'</td>';
				html+='<td>'+n.owner+'</td>';
				html+='<td>'+n.state+'</td>';
				html+='</tr>';
			})
				$("#tBody").html(html);
				//计算总页数
				var totalPages = result.total%pageSize==0?result.total/pageSize:parseInt(result.total/pageSize)+1;

				//数据处理完毕后，结合分页查询，对前端展现分页信息
				$("#cluePage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: result.total, // 总记录条数

					visiblePageLinks: 5, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					//该回调函数是在，点击分页组件的时候触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}
	
</script>
</head>
<body>

<input type="hidden" id="hidden-fullname">
<input type="hidden" id="hidden-company">
<input type="hidden" id="hidden-phone">
<input type="hidden" id="hidden-source">
<input type="hidden" id="hidden-owner">
<input type="hidden" id="hidden-mPhone">
<input type="hidden" id="hidden-state">



	<!-- 创建线索的模态窗口 -->
	<div class="modal fade" id="createClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">创建线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="clueAddForm">
					
						<div class="form-group">
							<label for="create-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-Owner">

								</select>
							</div>
							<label for="create-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-appellation">
								  <option></option>
									<c:forEach items="${appellation}" var="a">
										<option value="${a.value}">${a.text}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-fullname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
							<label for="create-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
							<label for="create-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-state">
								  <option></option>
									<c:forEach items="${clueState}" var="b">
										<option value="${b.value}">${b.text}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-source">
								  <option></option>
									<c:forEach items="${source}" var="c">
										<option value="${c.value}">${c.text}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						

						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">线索描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control time" id="create-nextContactTime" readonly>
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>
						
						<div style="position: relative;top: 20px;">
							<div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
							</div>
						</div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改线索的模态窗口 -->
	<div class="modal fade" id="editClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">修改线索</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-clueOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
								  <%--所有者下拉框--%>
								</select>
							</div>
							<label for="edit-company" class="col-sm-2 control-label">公司<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-company" value="动力节点">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-call" class="col-sm-2 control-label">称呼</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-appellation">
								  <%--称呼--%>
									<option></option>
									  <c:forEach items="${appellation}" var="a">
										  <option value="${a.value}">${a.text}</option>
									  </c:forEach>
								</select>
							</div>
							<label for="edit-surname" class="col-sm-2 control-label">姓名<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" value="李四">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">职位</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job" value="CTO">
							</div>
							<label for="edit-email" class="col-sm-2 control-label">邮箱</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email" value="lisi@bjpowernode.com">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-phone" class="col-sm-2 control-label">公司座机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone" value="010-84846003">
							</div>
							<label for="edit-website" class="col-sm-2 control-label">公司网站</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-website" value="http://www.bjpowernode.com">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-mphone" class="col-sm-2 control-label">手机</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone" value="12345678901">
							</div>
							<label for="edit-status" class="col-sm-2 control-label">线索状态</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-state">
								  <%--线索状态--%>
									  <option></option>
									  <c:forEach items="${clueState}" var="b">
										  <option value="${b.value}">${b.text}</option>
									  </c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-source" class="col-sm-2 control-label">线索来源</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source">
								  <%--线索来源--%>
									  <option></option>
									  <c:forEach items="${source}" var="c">
										  <option value="${c.value}">${c.text}</option>
									  </c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"><%--描述--%></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">联系纪要</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary"><%--联系纪要--%></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">下次联系时间</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control" id="edit-nextContactTime">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">详细地址</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address"><%--详细地址--%></textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>线索列表</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司</div>
				      <input class="form-control" type="text" id="search-company">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">公司座机</div>
				      <input class="form-control" type="text" id="search-phone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索来源</div>
					  <select class="form-control" id="search-source">
					  	  <option></option>
						  <c:forEach items="${source}" var="a">
							  <option value="${a.value}">${a.text}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>
				  
				  
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">手机</div>
				      <input class="form-control" type="text" id="search-mPhone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">线索状态</div>
					  <select class="form-control" id="search-state">
						  <option></option>
					  	<c:forEach items="${clueState}" var="a">
							<option value="${a.value}">${a.text}</option>
						</c:forEach>
					  </select>
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus" ></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 50px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
							<td>公司</td>
							<td>公司座机</td>
							<td>手机</td>
							<td>线索来源</td>
							<td>所有者</td>
							<td>线索状态</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr>
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/clue/detail.jsp';">李四先生</a></td>
							<td>动力节点</td>
							<td>010-84846003</td>
							<td>12345678901</td>
							<td>广告</td>
							<td>zhangsan</td>
							<td>已联系</td>
						</tr>--%>
                        <%--<tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/clue/detail.jsp';">李四先生</a></td>
                            <td>动力节点</td>
                            <td>010-84846003</td>
                            <td>12345678901</td>
                            <td>广告</td>
                            <td>zhangsan</td>
                            <td>已联系</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 60px;">
				<div id="cluePage">111111</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>