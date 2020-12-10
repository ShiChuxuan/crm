<%--
  Created by IntelliJ IDEA.
  User: 20465
  Date: 2020/12/9
  Time: 10:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";

    /*
    *       需求：
    *
    *           根据交易表中的不同阶段的数量进行一个统计最终形成一个漏斗图（倒三角）
    *
    *           将统计出来的阶段的数量比较多的，往上面排列
    *           将统计出来的阶段的数量比较小得，往下面排列
    *
    *       例如：
    *           01资质审查  10条
    *           02需求分析  85条
    *           03价值建议  3条
    *           ...
    *           07成交      100条
    *
    *       sql：
    *           按照阶段进行分组
    *
    *           resultType:"map"
    *
    *           select
    *
    *           stage，count(*)
    *
    *           from tbl_tran
    *
    *           group by stage
    *
    *
    * */
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <script type="text/javascript" src="ECharts/echarts.min.js"></script>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>


    <script type="text/javascript">
        $(function () {
           //页面加载完毕后绘制统计图表
            getCharts();

        })
        function getCharts() {
            $.ajax({
                url:"workbench/tran/getCharts.do",
                type:"get",
                dataType:"json",
                success(result){
                    /*
                    *
                    *   data:
                    *        {"total":?,"dataList":[ {value: 60, name: '访问'},{value: 60, name: '访问'}]}
                    *
                    * */
                    // 基于准备好的dom，初始化echarts实例
                    var myChart = echarts.init(document.getElementById('main'));

                    // 我们要画的图
                    option = {
                        title: {
                            text: '交易漏斗图',
                            subtext: '统计交易数量的漏斗图'
                        },
                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c}%"
                        },
                        toolbox: {
                            feature: {
                                dataView: {readOnly: false},
                                restore: {},
                                saveAsImage: {}
                            }
                        },
                        legend: {
                            data: ['展现','点击','访问','咨询','订单']
                        },

                        series: [
                            {
                                name:'交易漏斗图',
                                type:'funnel',
                                left: '10%',
                                top: 60,
                                //x2: 80,
                                bottom: 60,
                                width: '80%',
                                // height: {totalHeight} - y - y2,
                                min: 0,
                                max: result.total,
                                minSize: '0%',
                                maxSize: '100%',
                                sort: 'descending',
                                gap: 2,
                                label: {
                                    show: true,
                                    position: 'inside'
                                },
                                labelLine: {
                                    length: 10,
                                    lineStyle: {
                                        width: 1,
                                        type: 'solid'
                                    }
                                },
                                itemStyle: {
                                    borderColor: '#fff',
                                    borderWidth: 1
                                },
                                emphasis: {
                                    label: {
                                        fontSize: 20
                                    }
                                },
                                data: result.dataList
                                /*[
                                    {value: 60, name: '访问'},
                                    {value: 40, name: '咨询'},
                                    {value: 20, name: '订单'},
                                    {value: 80, name: '点击'},
                                    {value: 100, name: '展现'}
                                ]*/
                            }
                        ]
                    };

                    // 使用刚指定的配置项和数据显示图表。
                    myChart.setOption(option);

                }

            })

        }
    </script>

</head>
<body>
        <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
        <div id="main" style="width: 600px;height:400px;"></div>

</body>
</html>
