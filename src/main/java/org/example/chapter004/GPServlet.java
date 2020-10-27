package org.example.chapter004;

public abstract class GPServlet {
//    public void service(GPRequest request, GPResponse response) throws Exception {
//        if ("GET".equalsIgnoreCase(request.getMethod())) {
//            doGet(request, response);
//        } else {
//            doPost(request, response);
//        }
//    }

    public void service(GPNettyRequest request, GPNettyResponse response) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    public abstract void doGet(GPNettyRequest request, GPNettyResponse response) throws Exception;

    public abstract void doPost(GPNettyRequest request, GPNettyResponse response) throws Exception;
}
