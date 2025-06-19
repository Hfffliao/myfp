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
                <h1>欢迎来到home</h1>
                <div class="hero1"> <h2>霖依创造</h2></div>

            </div>
        </section>

        <div class="card-container">
            <article class="card">
                <h2>创新设计</h2>
                <p>采用最新设计理念，打造卓越用户体验</p>
            </article>
            <article class="card">
                <h2>尖端技术</h2>
                <p>运用前沿技术，保障系统稳定高效</p>
            </article>
            <article class="card">
                <h2>优质服务</h2>
                <p>7×24小时专业团队技术支持</p>
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