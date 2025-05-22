<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" import="love.linyi.controller.Code"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=0.6">
    <title>霖依</title>
    <!-- 引入字体图标 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        /* CSS变量定义 */
        :root {
            --primary-color: #177bdf;
            --secondary-color: #34db95;
            --accent-color: #e67e22;
            --text-color: #4c4545;
            --box-shadow: 1 4px/*方框下面的阴影长度*/ 6px rgba(245, 12, 12, 0.1);
        }

        /* 重置默认样式 */
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', system-ui, sans-serif;
        }

        body {
            line-height: 1.6;
            color: var(--text-color);
            background: #f5f6fa;
        }

        /* 导航栏样式 */
        .navbar {
            background: linear-gradient( var(--primary-color),rgba(88, 88, 205, 0.5));
            padding: 1rem 2rem;
            box-shadow: var(--box-shadow);
            position:relative;
            top: 0;
            z-index: 1000;
        }

        .nav-container {
            max-width: 1200px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            color: white;
            font-size: 1.5rem;
            text-decoration: none;
            transition: all 0.3s ease;
        }

        .logo:hover{
            transform: scale(1.05);

        }

        .nav-links {
            display: flex;
            gap: 2rem;
        }

        /* 主内容区 */
        .hero {

            background: linear-gradient(  rgba(88, 88, 205, 0.5), rgba(198, 18, 18, 0.5)),
            url('https://source.unsplash.com/random/1920x1080') center/cover;
            display:flex;
            align-items:center;
            justify-content: center;
            text-align: center;
            color: rgb(221, 208, 208);
            margin-bottom: 0rem;
        }

        .hero-content h1 {
            font-size: 3.5rem;
            margin-bottom: 3rem;
            text-shadow: 20px 20px 1px rgba(0, 0, 0, 0.5);
        }
        .hero-content h2 {

            transition:tall 0.5s ;
        }
        .hero-content h2:hover{
            transform: scale(1.15);
            transform: translateY(-2px);

        }

        /* 卡片布局 */
        .card-container {
            width:100%;
            margin: 0rem ;
            font-size: large;
            padding: 2rem;
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
            width: 100%;
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
            width: 40%;
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
        footer {
            background: linear-gradient( rgba(198, 18, 18, 0.5), #e61414);
            color: rgb(243, 236, 236);
            padding: 3rem 2rem;
            margin-top: 0rem;
        }

        .footer-content {
            max-width: 1200px;
            margin: 0 auto;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 2rem;
        }

        .social-links {
            display: flex;
            gap: 1rem;
            margin-top: 1rem;
        }

        .social-link {
            color: white;
            font-size: 1.5rem;
            transition: opacity 0.3s ease;
        }

        .social-link:hover {
            opacity: 0.8;
        }
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
            <h1>注册账号</h1>
            <div class="hero1"> <h2>填写相关信息进行注册</h2></div>
        </div>
    </section>

    <div class="card-container">
        <form action="<%=Code.host %>register" method="post" class="card1">
            <h3> 用户名（邮箱地址，用于接收验证码）：</h3>
            <div style="display: flex;flex-direction: row;width: 100%;justify-content: center;gap: 1%;">
                <input type ="email" name="username" value="<%=request.getAttribute("username")%>" class="card-in" style="display: block;">
                <input type="submit" value="获取验证码" id="area" style="font-size: small;width: 20%;">
            </div>
        </form>
        <form action="<%=Code.host %>register" method="post" class="card1">
            <input type ="hidden" name="username" value="<%=request.getAttribute("username")%>">
            <input type ="hidden" name="myverification" value="<%=request.getAttribute("myverification")%>">
            <h3>  密码：</h3>
            <input type ="password" name="password" class="card-in" >
            <h3>  再次输入密码：</h3>
            <input type ="password" name="repassword" class="card-in" >
            验证码：<input type ="text" name="verification" class="card-in">
            <input type="submit" value="注册" id="area">


        </form>
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
<script>
    function disableSubmitButton() {
        // 获取提交按钮
        var submitButton = document.getElementById('area');
        // 禁用按钮
        submitButton.disabled = true;
        // 设置初始倒计时时间
        var countdown = 60;
        // 更新按钮文本显示倒计时
        submitButton.value = '提交 (' + countdown + 's)';

        // 每秒更新一次倒计时
        var timer = setInterval(function() {
            countdown--;
            if (countdown > 0) {
                submitButton.value = '提交 (' + countdown + 's)';
            } else {
                // 倒计时结束，启用按钮
                submitButton.disabled = false;
                submitButton.value = '提交';
                // 清除定时器
                clearInterval(timer);
            }
        }, 1000);
    }
</script>
</body>
</html>