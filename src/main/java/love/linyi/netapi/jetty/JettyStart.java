//无法直接使用
//我的jetty启动代码里面直接使用了已经生成applicationContext和webApplicationContext,这些是由tomcat启动时生成的,所以这个类暂时不能直接启动
//package love.linyi.service.camera.http3;
//
//public class JettyStart {
//    public static void main(String[] args) throws Exception {
//        JettyInitializer jettyInitializer = new JettyInitializer();
//        jettyInitializer.startJettyInThread();
//    }
//}
