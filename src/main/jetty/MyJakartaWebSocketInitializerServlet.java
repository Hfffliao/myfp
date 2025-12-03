import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.server.ServerContainer;
import jakarta.websocket.server.ServerEndpointConfig;

import java.util.List;

public class MyJakartaWebSocketInitializerServlet extends HttpServlet
{
    @Override
    public void init() throws ServletException
    {
        try
        {
            // 从 ServletContext 属性中检索 ServerContainer。
            ServerContainer container = (ServerContainer)getServletContext().getAttribute(ServerContainer.class.getName());

            // 配置 ServerContainer。
            container.setDefaultMaxTextMessageBufferSize(128 * 1024);



            // 高级注册您的 WebSocket 端点。
            container.addEndpoint(
                ServerEndpointConfig.Builder.create(WebSocketview.class, "/car/user")
                    .subprotocols(List.of("wss"))
                    .build()
            );
        }
        catch (DeploymentException x)
        {
            throw new ServletException(x);
        }
    }
}