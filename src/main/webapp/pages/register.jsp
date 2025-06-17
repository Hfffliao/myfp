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
        #area1 {
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
        <form action="<%=Code.host %>register" method="get" class="card1">
            <h3> 用户名（邮箱地址，用于接收验证码）：</h3>
            <div style="display: flex;flex-direction: row;width: 100%;justify-content: center;gap: 1%;">
                <input type ="email" name="username" value="<%=request.getAttribute("username")%>" class="card-in" style="display: block;">
                <input type="button" value="获取验证码" id="area" style="font-size: small;width: 20%;" onclick=startCountdown('area')>
            </div>
        </form>
        <form action="<%=Code.host %>register" method="post" class="card1">
            <input type ="hidden" name="username" value="<%=request.getAttribute("username")%>">
            <h3>  密码：</h3>
            <input type ="password" name="password" class="card-in" >
            <h3>  再次输入密码：</h3>
            <input type ="password" name="repassword" class="card-in" >
            验证码：<input type ="text" name="verification" class="card-in">
            <input type="submit" value="注册" id="area1">
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
    // 倒计时控制函数 - 核心解决方案
    function startCountdown(buttonId) {
        // 0. 获取按钮元素
        const button = document.getElementById(buttonId);
        if (!button) return;

        // 1. 获取当前时间并保存到 sessionStorage
        const startTime = new Date().getTime();
        sessionStorage.setItem(`${buttonId}StartTime`, startTime);

        // 2. 保存按钮原始文本
        const originalValue = button.value;
        sessionStorage.setItem(`${buttonId}OriginalValue`, originalValue);

        // 3. 立即更新按钮状态
        button.disabled = true;
        button.value = `${originalValue} (${60}s)`;

        // 4. 提交表单获取验证码
        document.querySelector('form[method="get"]').submit();
    }

    // 页面加载时检查是否有倒计时需要恢复
    window.addEventListener('DOMContentLoaded', () => {
        // 0. 只检查"获取验证码"按钮
        const buttonId = 'area';
        const button = document.getElementById(buttonId);
        if (!button) return;

        // 1. 尝试获取sessionStorage中的倒计时数据
        const storedStartTime = sessionStorage.getItem(`${buttonId}StartTime`);
        const storedOriginalValue = sessionStorage.getItem(`${buttonId}OriginalValue`);

        // 2. 如果没有存储数据或已过期，则重置按钮状态
        if (!storedStartTime || !storedOriginalValue) {
            button.disabled = false;
            button.value = "获取验证码";
            return;
        }

        // 3. 计算剩余时间
        const startTime = parseInt(storedStartTime);
        const elapsed = Math.floor((Date.now() - startTime) / 1000);
        let remaining = Math.max(0, 60 - elapsed);

        // 4. 如果时间已过期
        if (remaining <= 0) {
            // 恢复按钮并清除存储
            button.disabled = false;
            button.value = storedOriginalValue;
            sessionStorage.removeItem(`${buttonId}StartTime`);
            sessionStorage.removeItem(`${buttonId}OriginalValue`);
            return;
        }

        // 5. 更新按钮初始状态
        button.disabled = true;
        button.value = storedOriginalValue +remaining+`s`;

        // 6. 启动倒计时计时器
        const timer = setInterval(() => {
            // 计算新的剩余时间
            const currentElapsed = Math.floor((Date.now() - startTime) / 1000);
            remaining = Math.max(0, 60 - currentElapsed);

            if (remaining > 0) {
                button.value = storedOriginalValue+remaining+`s`;
            } else {
                // 倒计时结束，恢复按钮状态
                clearInterval(timer);
                button.disabled = false;
                button.value = storedOriginalValue;

                // 清除存储的数据
                sessionStorage.removeItem(`${buttonId}StartTime`);
                sessionStorage.removeItem(`${buttonId}OriginalValue`);
            }
        }, 1000); // 每秒更新一次
    });
</script>
</body>
</html>