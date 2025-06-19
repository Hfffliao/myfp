<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" import="love.linyi.controller.Code"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=0.6">
    <title>霖依</title>
    <!-- 引入字体图标 -->
    <link rel="stylesheet" href="/pages/css/linyistyle.css">
    <style>

        .card-container {
            width:100%;
            padding-right: 6rem;
            padding-left: 3rem;
            /*margin: 0rem ;*/
            font-size: large;
            display:grid;
            grid-template-columns:repeat(auto-fit, minmax(5px, 100%));
            place-items: center;
            gap: 1rem;
            background:rgba(198, 18, 18, 0.5);

        }
        .card-container1 {
            width:100%;
            font-size: large;
            display:grid;
            grid-template-columns:repeat(auto-fit, minmax(5px, 100%));
            place-items: center;
            background:rgba(198, 18, 18, 0.5);

        }

        .card {
            background: rgb(191, 207, 221);
            color: rgb(29, 24, 24);
            border-radius: 10px;
            padding: 2rem;
            box-shadow: var(--box-shadow);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .card:hover {
            transform: translateY(-20px);
            box-shadow: 50px 8px 12px rgba(0, 0, 0, 0.2);
        }
        .card-in{
            font-size: 20px;
            width: 100px;
            height: 40px;
            padding: 1px; /* 内边距，调整内容与边框的距离 */
        }
        #area {
            width: 10%;
            height: 50px;
            background-color: #fcf6f7;
            font-size: large;
        }

        /* 动画效果 */
        @keyframes fadeIn {
            from { opacity: 0.5; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .card {
            animation: fadeIn 1s ease forwards;
        }
        .inline-block-label {
            display: inline-block;
            margin: 1rem;
            font-size: 12px;
            /* margin-right: 10px;*/
        }
        .inline-block-label1 {
            display: inline-block;
            margin: 1rem;
            font-size: 18px;
            /* margin-right: 10px;*/
        }
        .mytable {
            border: 3px solid #ffffff;
            border-top-left-radius: 50px;
            border-bottom-left-radius: 50px;

            border-collapse: collapse; /* 合并相邻单元格的边框 */
            overflow-y: auto;
            width: 100%;
            height: 800px;
            margin: 1rem;
            /* 内边距，调整内容与边框的距离 */
            font-size: 12px;
            display: flex;
            /* 水平排列 */
            flex-direction: row;
            /* 允许换行 */
            flex-wrap: wrap;
            /*justify-content: center; !* 水平居中 *!*/
            /*align-items: center; !* 垂直居中 *!*/
        }
        .mytr{
            width: 12.5%;
            display:inline-flex;
            /* 垂直排列 */
            flex-direction: column;
            /* 允许换行 */
            flex-wrap: wrap;
        }
        .mytd {
            display: inline;
            width: 100%;
            border: 2px solid #ffffff;
            padding: 8px;
            background-color: #ffffff;
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
            .nav-links {
                display: none;
            }

            .hero-content h1 {
                font-size: 2.5rem;
            }
        }


    </style>
</head>
<body>
<nav class="navbar">
    <div class="nav-container">
        <a href="#" class="logo">霖依智造</a>
        <i class="fas fa-bars menu-toggle" onclick="toggleMenu()"></i> <!-- 添加汉堡菜单 -->
        <div class="nav-links" id="navLinks">

            <a href="<%=request.getSession().getAttribute("user")==null?Code.host+"main.jsp":"#" %>"
               class="nav-link" ><%=request.getSession().getAttribute("user")%></a>
            <a href="<%=Code.host %>pages/maint.jsp" class="nav-link">首页</a>
            <a href="#" class="nav-link">产品</a>
            <a href="<%=Code.host %>pages/fuwu.jsp" class="nav-link">服务</a>
            <a href="<%=Code.host %>pages/man.jsp" class="nav-link">关于</a>
            <a href="#" class="nav-link">联系</a>
        </div>
    </div>
</nav>
<main>
    <section class="hero">
        <div class="hero-content">
            <h2>服务</h2>
            <h2><span id="message">查询自定义区间超声波测距仪的数据</span></h2><br>
            <h4><span id="message">时间输入‘时 分’；日期输入‘月 日’</span></h4><br>
            <form action="<%=Code.host%>yuan1" method="get">
                <div class="inline-block-label">
                    <h2>查询条件</h2>
                    开始时间：<input type ="text" name="start" value="${param.start}" class="card-in">
                    日期：<input type ="text" name="date" value="${param.date}" class="card-in">
                    结束时间：<input type ="text" name="stop" value="${param.stop}" class="card-in">
                </div>
                <input type="submit"  value="查询" id="area">

            </form>
        </div>
    </section>
    <div class="card-container1">
        <h2>信息表格</h2>
    </div>
    <div class="card-container">
        <div class= "mytable">
            <c:forEach items="${dataList}" var="name">
                <div class="mytr">
                    <div class="mytd">${name.otime}</div>
                    <div class="mytd" style="background-color: #dddddd">${name.distance}</div>
                </div>
            </c:forEach>
        </div>
    </div>


</main>

    <footer>
        <div class="footer-content">
            <div>
                <h3>联系我们</h3>
                <p>电话: (+86) 19807393661</p>

               <p>邮箱： <a href="https://mail.qq.com/" id="copyContent" onclick="copyToClipboard()"> 3390351358@qq.com</a></p>
                <p><a href="https://beian.miit.gov.cn">湘公网安备30174248163393967号</a></p>
            </div>
            <div>
                <h3>关注我们</h3>
                <div class="social-links">
                    <a href="#" class="social-link"><i class="fab fa-weibo"></i></a>
                    <a href="#" class="social-link"><i class="fab fa-weixin"></i></a>
                    <a href="#" class="social-link"><i class="fab fa-github"></i></a>
                </div>
            </div>
        </div>
    </footer>
    <script>
        async function copyToClipboard() {
          // 获取要复制的文本内容
          const text = document.getElementById('copyContent').innerText;

          try {
            // 写入剪贴板
            await navigator.clipboard.writeText(text);
            alert('复制成功: ' + text);
          } catch (err) {
            // 错误处理
            console.error('复制失败:', err);
            alert('请手动复制内容');
          }
        }
        // 添加汉堡菜单切换功能
        function toggleMenu() {
            const navLinks = document.getElementById('navLinks');
            navLinks.style.display = navLinks.style.display === 'flex' ? 'none' : 'flex';
        }

        // 点击菜单外区域关闭菜单（可选）
        document.addEventListener('click', (e) => {
            if (!e.target.closest('.nav-container')) {
                document.getElementById('navLinks').style.display = 'none';
            }
        });
      </script>
</body>
</html>