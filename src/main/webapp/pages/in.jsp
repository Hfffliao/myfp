<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" import="love.linyi.controller.Code"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=0.6">
    <title>霖依</title>
    <link rel="stylesheet" href="/pages/css/linyistyle.css">

    <!-- 引入字体图标 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>

        /* 卡片布局 */
        .card-container {
            max-width:none;
            margin: 0rem ;
            font-size: large;
            padding: 3rem;
            display:flex;
            display: flex;
            /* 水平排列 */
            flex-direction: column;
            /* 允许换行 */
            flex-wrap: wrap;
            gap: 1rem;
            background:rgba(198, 18, 18, 0.5);
            justify-content: center;
            align-items: center;

        }
        .card1{
            display:flex;
            display: flex;
            /* 水平排列 */
            flex-direction: column;
            /* 允许换行 */
            flex-wrap: wrap;
            gap: 1rem;
            justify-content: center;
            align-items: center;
        }
        .card-in{
            font-size: 20px;
            width: 300px;
            height: 40px;
            padding: 1px; /* 内边距，调整内容与边框的距离 */
        }
        #area {
            width: 25%;
            height: 50px;
            background-color: #e8d4da;
            font-size: large;
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

        /* 页脚样式 */

    </style>
</head>
<body>

<nav class="navbar">
    <div class="nav-container">
        <a href="#" class="logo">霖依智造</a>
        <div class="nav-links">

        </div>
    </div>
</nav>

<main>

    <section class="hero">
        <div class="hero-content">
            <h1>登录home，享受专业的服务</h1>
            <div class="hero1"> <h2>输入给定的用户名和密码进行登录</h2></div>
        </div>
    </section>

    <div class="card-container">
        <form action="<%=Code.host %>login" method="post" class="card1">
            用户名（邮箱地址）：<input type ="text" name="username" class="card-in"><br>

            密码：<input type ="password" name="password" class="card-in"><br>
            <input type="submit"  value="登录" id="area">


        </form>
        <div>
            <a href="<%=Code.host%>pages/register.jsp">注册账号</a>
            <a href="<%=Code.host%>pages/getpassword.jsp">注册账号</a>
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
</script>
</body>
</html>