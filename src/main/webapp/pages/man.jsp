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
            width: 100%; /* 宽度充满屏幕 */
            background: linear-gradient(var(--primary-color), rgba(88, 88, 205, 0.5));
            padding: 1rem 2rem;
            box-shadow: var(--box-shadow);
            position: relative;
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

        .nav-link {
            color: rgba(255, 255, 255, 0.9);
            text-decoration:none;
            padding: 0.5rem 1rem;
            border-radius: 6px;
            transition: all 0.3s ease;
        }

        .nav-link:hover {
            background: rgba(147, 137, 137, 0.1);
            transform: translateY(-2px);
        }

        /* 主内容区 */
        .hero {
            min-height: 40vh;
            background: linear-gradient(  rgba(88, 88, 205, 0.5), rgba(198, 18, 18, 0.5)),
            url('https://source.unsplash.com/random/1920x1080') center/cover;
            display:flex;
            align-items:center;
            justify-content: center;
            text-align: center;
            color: rgb(221, 208, 208);
            margin-bottom: 0rem;
            padding: 2rem;
        }

        .hero-content h1 {
            font-size: 2.5rem;
            margin-bottom: 3rem;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
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
            max-width:none;
            margin: 0rem ;
            padding: 5rem;
            display:grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 2rem;
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

        /* 动画效果 */
        @keyframes fadeIn {
            from { opacity: 0.5; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .card {
            animation: fadeIn 1s ease forwards;
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
        @media (max-width: 768px) {
            /* 导航栏改为汉堡菜单 */
            -links {
                display: none; /* 默认隐藏导航链接 */
                width: 100%;
                flex-direction: column;
                position: absolute;
                top: 100%;
                left: 0;
                background: var(--primary-color);
                padding: 1rem;
            }

            .nav-link {
                width: 100%;
                text-align: center;
                padding: 1rem;
            }

            /* 显示汉堡菜单图标 */
            .menu-toggle {
                display: block !important;
                color: white;
                font-size: 1.5rem;
                cursor: pointer;
            }
            /* 卡片容器减少内边距 */
        }
    </style>
</head>
<body>
<nav class="navbar">
    <div class="nav-container">
        <a href="#" class="logo">霖依智造</a>
        <i class="fas fa-bars menu-toggle" onclick="toggleMenu()"></i> <!-- 添加汉堡菜单 -->
        <div class="nav-links" id="navLinks">
            <a href="#" class="nav-link" >liaoyi</a>
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
            <h1>赞助者信息</h1>
            <div class="hero1"> <h2>霖依创造</h2></div>

        </div>
    </section>

    <div class="card-container">
        <arti class="card">
            <h2>满师傅</h2>
            <img src="man.jpg" alt="示例图片">
        </arti>
        <article class="card">

        </article>
        <article class="card">

        </article>
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
