package love.linyi.controller;

import java.nio.file.Paths;

public class Code {
    public static final Integer SAVE_OK=20011;
    public static final Integer DELETE_OK=20021;
    public static final Integer UPDATE_OK=20031;
    public static final Integer GET_OK=20041;

    public static final Integer SAVE_ERR=20010;
    public static final Integer DELETE_ERR=20020;

    public static final Integer UPDATE_ERR=20030;
    public static final Integer GET_ERR=20040;
//    public static final String host="http://linyi.love:25565/";
//    public static final String host="http://localhost/";
//    public static final String host="http://linyi.love/";
    //public static final String root="C:/uploads";
    //要创建user.home目录并且把权限给当前用户
    public static final String root= Paths.get(System.getProperty("user.home"), "uploads").toString();
    public static final int tcpserverPort=25567;
    public static final int udpreceiveAndThenSendport=25570;
    //public static final int receivePort=25570;
    public static final int UdpRecvAndH3SendPort=25571;
    public static final String KeyStorePath= Paths.get(System.getProperty("user.home"), "linyi.love_nginx").toString();
    public static final String KeyStorePassword="liao";
    public static final int jettyhttp3Andhttp2Port=8443;
    //public static final String jettyProgramPath = "F:\\apache-tomcat-9.0.106\\webapps\\ROOT" ;//本来是告诉jetty项目的部署路径的，但是不需要，它自己知道
    //public static final String jettyProgramPath = "/opt/apache-tomcat-11.0.11/webapps/ROOT/" ;
}
