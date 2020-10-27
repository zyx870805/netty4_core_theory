package org.example.chapter004;

import java.io.OutputStream;

public class GPResponse {

    private OutputStream out;

    public GPResponse(OutputStream out) {
        this.out = out;
    }

    public void write(String s) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.1 200 OK\n");
        sb.append("Content-Type: text/html;\n");
        sb.append("\r\n");
        out.write(sb.toString().getBytes());
    }
}
