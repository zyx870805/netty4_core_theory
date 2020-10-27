//package org.example.chapter004;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//public class GPTomcat {
//
//    private int port = 8080;
//
//    private ServerSocket server;
//
//    private Map<String, GPServlet> servletMapping = new HashMap<>();
//
//    private Properties webxml = new Properties();
//
//    private void init() {
//        try {
//            String WEB_INF = this.getClass().getResource("/").getPath();
//            FileInputStream fileInputStream = new FileInputStream(WEB_INF + "web.properties");
//
//            webxml.load(fileInputStream);
//
//            for (Object k : webxml.keySet()) {
//                String key = k.toString();
//                if (key.endsWith(".url")) {
//                    String servletName = key.replaceAll("\\.url$", "");
//                    String url = webxml.getProperty(key);
//                    String className = webxml.getProperty(servletName + ".className");
//                    GPServlet servlet = (GPServlet) Class.forName(className).newInstance();
//                    servletMapping.put(url, servlet);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void start() {
//        init();
//
//        try {
//            server = new ServerSocket(this.port);
//            System.out.println("GPTomcat 已启动, 监听的端口是： " + this.port);
//
//            while (true) {
//                Socket client = server.accept();
//                process(client);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void process(Socket client) throws Exception {
//        InputStream inputStream = client.getInputStream();
//        OutputStream outputStream = client.getOutputStream();
//
//        GPRequest request = new GPRequest(inputStream);
//        GPResponse response = new GPResponse(outputStream);
//
//        String url = request.getUrl();
//
//        if (servletMapping.containsKey(url)) {
//            servletMapping.get(url).service(request, response);
//        } else {
//            response.write("404 - Not Found");
//        }
//
//        outputStream.flush();
//        outputStream.close();
//
//        inputStream.close();
//
//        client.close();
//    }
//
//    public static void main(String[] args) {
//        new GPTomcat().start();
//    }
//}
