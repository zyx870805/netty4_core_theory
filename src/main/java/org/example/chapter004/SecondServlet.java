package org.example.chapter004;

public class SecondServlet extends GPServlet{
    @Override
    public void doGet(GPNettyRequest request, GPNettyResponse response) throws Exception {
        doPost(request, response);
    }

    @Override
    public void doPost(GPNettyRequest request, GPNettyResponse response) throws Exception {
        response.write("this is second servlet");
    }
}
