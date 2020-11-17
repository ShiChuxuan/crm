<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

<script type="text/javascript">

	//默认情况下取消和保存按钮是隐藏的
	var cancelAndSaveBtnDefault = true;
	
	$(function(){
		$("#remark").focus(function(){
			if(cancelAndSaveBtnDefault){
				//设置remarkDiv的高度为130px
				$("#remarkDiv").css("height","130px");
				//显示
				$("#cancelAndSaveBtn").show("2000");
				cancelAndSaveBtnDefault = false;
			}
		});
		
		$("#cancelBtn").click(function(){
			//显示
			$("#cancelAndSaveBtn").hide();
			//设置remarkDiv的高度为130px
			$("#remarkDiv").css("height","90px");
			cancelAndSaveBtnDefault = true;
		});
		
		$(".remarkDiv").mouseover(function(){
			$(this).children("div").children("div").show();
		});
		
		$(".remarkDiv").mouseout(function(){
			$(this).children("div").children("div").hide();
		});
		
		$(".myHref").mouseover(function(){
			$(this).children("span").css("color","red");
		});
		
		$(".myHref").mouseout(function(){
			$(this).children("span").css("color","#e6e6e6");
		});
        //=======以上都是动画效果=======
		//打开模态窗口，铺数据
		$("#editBtn").click(function () {
			$.ajax({
				url:"workbench/activity/getUserListAndActivity.do",
				type:"post",
				data:{
					id:"${requestScope.get("activity").getId()}"
				},
				dataType:"json",
				success(result){

					//处理所有者的下拉框
					var html = "<option></option>";
					$.each(result.uList,function (i,n) {
						html +="<option value = '"+n.id+"'>"+n.name+"</option>";
					})
					$("#edit-owner").html(html);

					//处理activity
					$("#edit-id").val(result.a.id);
					$("#edit-owner").val(result.a.owner);
					$("#edit-name").val(result.a.name);
					$("#edit-startDate").val(result.a.startDate);
					$("#edit-endDate").val(result.a.endDate);
					$("#edit-cost").val(result.a.cost);
					$("#edit-description").val(result.a.description);

					//所有的值填写之后，就可以打开模态窗口了
					$("#editActivityModal").modal("show")
				}
			})
			$("#editActivityModal").modal("show");
		})

        //为更新按钮添加点击事件
        $("#updateBtn").click(function () {
            $.ajax({
                url:"workbench/activity/updateActivity.do",
                type:"post",
                dataType:"json",
                data:{
                    id:$.trim($("#edit-id").val()),
                    owner:$.trim($("#edit-owner").val()),
                    name:$.trim($("#edit-name").val()),
                    startDate:$.trim($("#edit-startDate").val()),
                    endDate:$.trim($("#edit-endDate").val()),
                    cost:$.trim($("#edit-cost").val()),
                    description:$.trim($("#edit-description").val())
                },
                success(result){
                    if(result.success){
                        alert("修改成功");
                    }
                }
            })
        })

        //页面加载完毕后，展现该市场活动关联的信息列表
		showRemarkList();

		//显示修改和删除备注的图标
        $("#remarkBody").on("mouseover",".remarkDiv",function(){
            $(this).children("div").children("div").show();
        })
        $("#remarkBody").on("mouseout",".remarkDiv",function(){
            $(this).children("div").children("div").hide();
        })

        //为添加备注按钮添加事件
        $("#addRemarkBtn").click(function () {
            $.ajax({
                url:"workbench/activity/addRemark.do",
                type:"post",
                data:{
                    noteContent:$("#remark").val(),
                    createBy:"${sessionScope.get("user").getName()}",
                    aid:"${requestScope.get("activity").getId()}"
                },
                dataType:"json",
                success(result){
                    /*
                    *   data:{"success":true/false,"remark":{备注}}
                    *
                    * */
                    if(result.success){
                        //添加成功

                        //添加成功以后把文本域的值清空
                        $("#remark").val("");

                        //在textarea文本域上方新增一个div
                        alert("添加备注成功");
                        var html = "";
                        html+='<div id="'+result.remark.id+'" class="remarkDiv" style="height: 60px;">';
                        html+='<img title="'+result.remark.createBy+'" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
                        html+='<div style="position: relative; top: -40px; left: 40px;" >';
                        html+='<h5 id="e'+result.remark.id+'">'+result.remark.noteContent+'</h5>';
                        html+='<font color="gray">市场活动</font> <font color="gray">-</font> <b>${requestScope.get("activity").getName()}</b> <small style="color: gray;" id="s'+result.remark.id+'"> '+(result.remark.editFlag==0?result.remark.createBy:result.remark.editBy)+' 由'+(result.remark.editFlag==0?result.remark.createTime:result.remark.editTime)+'</small>'
                        html+='<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
                        html+='<a class="myHref" href="javascript:void(0);" onclick="updateRemark(\''+result.remark.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color:#FF0000;"></span></a>';
                        html+='&nbsp;&nbsp;&nbsp;&nbsp;';
                        html+='<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+result.remark.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color:#FF0000;"></span></a>';
                        html+='</div>';
                        html+='</div>';
                        html+='</div>';
                        $("#remarkDiv").before(html);

                    }else{
                        alert("添加备注失败");
                    }
                }
            })
        })

        //为取消添加备注按钮添加事件
        $("#cancelBtn").click(function () {
            $("#remark").val("");
        })

        //为更新备注按钮添加事件
        $("#updateRemarkBtn").click(function () {
            var id = $("#remarkId").val()
            $.ajax({
                url:"workbench/activity/updateRemark.do",
                type:"post",
                data:{
                    id:id,
                    noteContent:$.trim($("#noteContent").val())
                },
                dataType:"json",
                success(result){
                    /*
                    *   data{"success":true/false,"ar":{备注}}
                    *
                    * */

                    if(result.success==true){
                        alert("修改备注成功");
                        //修改备注成功后
                        //修改div中相应的信息，需要更新的内容有noteContent,editTime,editBy
                        $("#e"+id).html(result.ar.noteContent);
                        $("#s"+id).html(result.ar.editBy+' 由'+result.ar.editTime);

                    }else{
                        alert("修改备注失败");
                    }
                }
            })
            $("#noteContent").val("");
            $("#editRemarkModal").modal("hide");
        })

	});
		function showRemarkList() {
			$.ajax({
				url:"workbench/activity/showRemarkList.do",
				type:"post",
				data:{
					Aid:"${requestScope.get("activity").getId()}"
				},
				dataType: "json",
				success(result) {
					/*
					*  data
					* 		[{备注1}，{2}，{3}]
					*
					*
					* */

				var html = "";
			    $.each(result,function (i,n) {
					/*
					*   javascript:void(0);：
					*       将超链接禁用，只能以触发事件的形式来操作
					*
					*
					* */

			        html+='<div id="'+n.id+'" class="remarkDiv" style="height: 60px;">';
					html+='<img title="'+n.createBy+'" src="image/user-thumbnail.png" style="width: 30px; height:30px;">';
					html+='<div style="position: relative; top: -40px; left: 40px;" >';
					html+='<h5 id="e'+n.id+'">'+n.noteContent+'</h5>';
                    html+='<font color="gray">市场活动</font> <font color="gray">-</font> <b>${requestScope.get("activity").getName()}</b> <small style="color: gray;" id="s'+n.id+'"> '+(n.editFlag==0?n.createBy:n.editBy)+' 由'+(n.editFlag==0?n.createTime:n.editTime)+'</small>'
					html+='<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">';
					html+='<a class="myHref" href="javascript:void(0);" onclick="updateRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color:#FF0000;"></span></a>';
					html+='&nbsp;&nbsp;&nbsp;&nbsp;';
					html+='<a class="myHref" href="javascript:void(0);" onclick="deleteRemark(\''+n.id+'\')"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color:#FF0000;"></span></a>';
					/*
					*       onclick="deleteRemark(\''+n.id+'\')"
					*       对于动态元素生成所触发的方法，参数必须套用在字符串当中
					*
					* */
					html+='</div>';
					html+='</div>';
					html+='</div>';
				})
                    $("#remarkDiv").before(html);
				}

			})
		}

        function deleteRemark(id){
		    if(confirm("确认删除该备注吗？")){
		        $.ajax({
                    url:"workbench/activity/DeleteRemark.do",
                    type:"post",
                    data:{
                        id:id
                    },
                    dataType:"JSON",
                    success(result){
                        if(result.success){
                            //如果成功，刷新备注列表
                            //这种做法不行，因为用的是before(),不是html(),不会清空前面有的内容
                            //showRemarkList();

                            //找到需要删除记录的div，将div移除掉
                            $("#"+id).remove();//*****

                        }else {
                            alert("删除备注失败！");
                        }
                    }
                })
            }
    }

        function updateRemark(id) {
            //将模态窗口中，隐藏域中的id进行赋值
            $("#remarkId").val(id);
		    //找到指定的存放备注信息的h5标签
            var oldNoteContent = $("#e"+id).html();
            //将旧的备注信息放入修改备注模态窗口的文本域中
            $("#noteContent").val(oldNoteContent);
            //以上信息处理完毕后，将修改备注的模态窗口打开
		    $("#editRemarkModal").modal("show");

        }
    

		
</script>

</head>
<body>
	
	<!-- 修改市场活动备注的模态窗口 -->
	<div class="modal fade" id="editRemarkModal" role="dialog">
        <%--备注的id--%>
        <input type="hidden" id="remarkId">
        <div class="modal-dialog" role="document" style="width: 40%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改备注</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">内容</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="noteContent"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="updateRemarkBtn">更新</button>
                </div>
            </div>
        </div>
    </div>

    <!-- 修改市场活动的模态窗口 -->
    <div class="modal fade" id="editActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">修改市场活动</h4>
                </div>
                <div class="modal-body">

                    <form class="form-horizontal" role="form">
						<input type="hidden" id="edit-id">
                        <div class="form-group">
                            <label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <select class="form-control" id="edit-owner">
                                    <%--动态填充所有者下拉框--%>
                                </select>
                            </div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-name" >
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-startDate" >
                            </div>
                            <label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-endDate" >
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-cost" >
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="edit-describe" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10" style="width: 81%;">
                                <textarea class="form-control" rows="3" id="edit-description"></textarea>
                            </div>
                        </div>

                    </form>

                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal" >关闭</button>
                    <button type="button" class="btn btn-primary" data-dismiss="modal" id="updateBtn" >更新</button>
                </div>
            </div>
        </div>
    </div>

	<!-- 返回按钮 -->
	<div style="position: relative; top: 35px; left: 10px;">
		<a href="javascript:void(0);" onclick="window.history.back();"><span class="glyphicon glyphicon-arrow-left" style="font-size: 20px; color: #DDDDDD"></span></a>
	</div>
	
	<!-- 大标题 -->
	<div style="position: relative; left: 40px; top: -30px;">
		<div class="page-header">
			<h3>市场活动-${requestScope.get("activity").getName()} <small>${requestScope.get("activity").getStartDate()} ~ ${requestScope.get("activity").getEndDate()}</small></h3>
		</div>
		<div style="position: relative; height: 50px; width: 250px;  top: -72px; left: 700px;">
			<button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-edit"></span> 编辑</button>
			<button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>
		</div>
	</div>
	
	<!-- 详细信息 -->
	<div style="position: relative; top: -70px;">
		<div style="position: relative; left: 40px; height: 30px;">
			<div style="width: 300px; color: gray;">所有者</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.get("activity").getOwner()}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">名称</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestScope.get("activity").getName()}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>

		<div style="position: relative; left: 40px; height: 30px; top: 10px;">
			<div style="width: 300px; color: gray;">开始日期</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.get("activity").getStartDate()}</b></div>
			<div style="width: 300px;position: relative; left: 450px; top: -40px; color: gray;">结束日期</div>
			<div style="width: 300px;position: relative; left: 650px; top: -60px;"><b>${requestScope.get("activity").getEndDate()}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px;"></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -60px; left: 450px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 20px;">
			<div style="width: 300px; color: gray;">成本</div>
			<div style="width: 300px;position: relative; left: 200px; top: -20px;"><b>${requestScope.get("activity").getCost()}</b></div>
			<div style="height: 1px; width: 400px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 30px;">
			<div style="width: 300px; color: gray;">创建者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestScope.get("activity").getCreateBy()}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${requestScope.get("activity").getCreateTime()}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 40px;">
			<div style="width: 300px; color: gray;">修改者</div>
			<div style="width: 500px;position: relative; left: 200px; top: -20px;"><b>${requestScope.get("activity").getEditBy()}&nbsp;&nbsp;</b><small style="font-size: 10px; color: gray;">${requestScope.get("activity").getEditTime()}</small></div>
			<div style="height: 1px; width: 550px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
		<div style="position: relative; left: 40px; height: 30px; top: 50px;">
			<div style="width: 300px; color: gray;">描述</div>
			<div style="width: 630px;position: relative; left: 200px; top: -20px;">
				<b>
					${requestScope.get("activity").getDescription()}
				</b>
			</div>
			<div style="height: 1px; width: 850px; background: #D5D5D5; position: relative; top: -20px;"></div>
		</div>
	</div>
	
	<!-- 备注 -->
	<div id="remarkBody" style="position: relative; top: 30px; left: 40px;">
		<div class="page-header">
			<h4>备注</h4>
		</div>

<%--

		<!-- 备注1 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>哎呦！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:10:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>

--%>
<%--
		<!-- 备注2 -->
		<div class="remarkDiv" style="height: 60px;">
			<img title="zhangsan" src="image/user-thumbnail.png" style="width: 30px; height:30px;">
			<div style="position: relative; top: -40px; left: 40px;" >
				<h5>呵呵！</h5>
				<font color="gray">市场活动</font> <font color="gray">-</font> <b>发传单</b> <small style="color: gray;"> 2017-01-22 10:20:10 由zhangsan</small>
				<div style="position: relative; left: 500px; top: -30px; height: 30px; width: 100px; display: none;">
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-edit" style="font-size: 20px; color: #E6E6E6;"></span></a>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<a class="myHref" href="javascript:void(0);"><span class="glyphicon glyphicon-remove" style="font-size: 20px; color: #E6E6E6;"></span></a>
				</div>
			</div>
		</div>
--%>
		<div id="remarkDiv" style="background-color: #E6E6E6; width: 870px; height: 90px;">
			<form role="form" style="position: relative;top: 10px; left: 10px;">
				<textarea id="remark" class="form-control" style="width: 850px; resize : none;" rows="2"  placeholder="添加备注..."></textarea>
				<p id="cancelAndSaveBtn" style="position: relative;left: 737px; top: 10px; display: none;">
					<button id="cancelBtn" type="button" class="btn btn-default">取消</button>
					<button id="addRemarkBtn" type="button" class="btn btn-primary">添加</button>
				</p>
			</form>
		</div>
	</div>
	<div style="height: 200px;"></div>
</body>
</html>