package org.example.chapter004;

public class FirstServlet extends GPServlet {
    @Override
    public void doGet(GPNettyRequest request, GPNettyResponse response) throws Exception {
        this.doPost(request, response);
    }

    @Override
    public void doPost(GPNettyRequest request, GPNettyResponse response) throws Exception {
        response.write("this is first servlet ");
    }
}
