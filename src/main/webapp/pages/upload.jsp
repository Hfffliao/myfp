<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" import="love.linyi.controller.Code"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=0.6">
    <title>霖依</title>
    <!-- 引入字体图标 -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="/pages/css/linyistyle.css">
    <style>


        /* 卡片布局 */
        .card-container {
            margin: 0rem ;
            padding: 5rem;
            display:grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 1rem;
            background:rgba(198, 18, 18, 0.5);

        }
        .card-container1 {
            max-width:none;
            padding: 1rem;
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
        .inline-block-label {
            display: inline-block;
            margin: 1rem;
            font-size: 24px;

            /* margin-right: 10px;*/
        }
        .inline-block-label1 {
            display: inline-block;
            margin: 1rem;
            /* margin-right: 10px;*/
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
            <h1>霖依创造</h1>
            <div class="hero1"> <h2>上传属于自己的页面</h2></div>

        </div>
    </section>

    <div class="card-container">
        <div>
            <h3>注：上传的界面会在同级目录下</h3>
        </div>
        <form id="uploadForm" class="inline-block-label1" action="<%=Code.host %>upload" method="post" enctype="multipart/form-data">

            <input class="inline-block-label" type="file" id="file1" name="file" required><br>
            <div style="display: inline-flex; flex-direction: row;width: 100%;align-items: center">
                <input id="uploadbt" class="inline-block-label"  type="button" value="上传" onclick="btonclick()">
                <div id="progressContainer" style="width: 70%;height: 25px; background-color: #f3d5d5 ;color: antiquewhite; display: none;align-items: center">
                    <div id="progressBar" style="width: 0%; height: 20px; background-color: #82aae5; text-align: center;  color: rgb(190, 68, 68);">0%</div>
                </div>
            </div>
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
<script>//进度条
    const uploadForm = document.getElementById('uploadForm');
    const progressContainer = document.getElementById('progressContainer');
    const progressBar = document.getElementById('progressBar');
    const uploadButton = document.getElementById('uploadbt');
    let intervalId;

    function btonclick() {
        uploadButton.disabled = true;
        const formData = new FormData(uploadForm);
        const xhr = new XMLHttpRequest();
        console.log("开始上传");
        xhr.open('POST', '<%=Code.host %>upload', true);
        xhr.timeout = 1000*60*60*24;
        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4 && xhr.status === 200) {
                const responseText = xhr.responseText;
                console.log('服务器返回信息:', responseText);
                // 根据不同的返回信息进行处理
                if (responseText.includes('login')) {
                    alert('请先登录后再进行上传操作');
                    // 可以在这里添加跳转到登录页面的逻辑
                    window.location.href = '<%=Code.host %>main.jsp';
                } else if (responseText.includes('file')) {
                    alert('错误：请求不是文件上传请求');
                    stopUpload();

                } else if (responseText.includes('success')) {

                } else if (responseText.includes('upload error')) {
                    // 处理其他未知响应
                    stopUpload();

                    alert(responseText);
                }
            }
        }
        // 显示进度条
        progressContainer.style.display = 'inline-flex';
        xhr.send(formData);
        console.log("show progress bar");
        // 开始轮询获取进度
        intervalId = setInterval(() => {
            console.log("轮询进度");
            const progressXhr = new XMLHttpRequest();
            progressXhr.open('GET', '<%=Code.host %>upload-progress', true);
            progressXhr.onreadystatechange = function() {
                if (progressXhr.readyState === 4 && progressXhr.status === 200) {
                    const progress = parseInt(progressXhr.responseText);
                    progressBar.style.width = progress + '%';
                    progressBar.textContent = progress + '%';

                    if (progress >= 100 || progress < 0) {

                        if (progress >= 100) {
                            alert('文件上传成功');
                        } else {
                            alert('文件上传失败');
                        }
                        stopUpload();
                    }
                }
            };
            progressXhr.send();
        }, 3000);
    }
    function stopUpload() {
        // 停止上传
        clearInterval(intervalId);
        // 隐藏进度条
        progressContainer.style.display = 'none';
        progressBar.style.width = '0%';
        progressBar.textContent = '0%';
        uploadButton.disabled = false;
    }
</script>
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