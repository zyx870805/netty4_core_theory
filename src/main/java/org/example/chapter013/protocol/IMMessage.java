package org.example.chapter013.protocol;

import lombok.Data;
import org.msgpack.annotation.Message;

@Message
@Data
public class IMMessage {
    private String addr;
    private String cmd;
    private long time;
    private int online;
    private String sender;
    private String receiver;
    private String content;
    private String terminal;

    public IMMessage() {
    }

    public IMMessage(String cmd, long time, int online, String content) {
        this.cmd = cmd;
        this.time = time;
        this.online = online;
        this.content = content;
    }

    public IMMessage(String cmd, String terminal, long time, String sender) {
        this.cmd = cmd;
        this.time = time;
        this.terminal = terminal;
        this.sender = sender;
    }

    public IMMessage(String cmd, long time, String sender, String content) {
        this.cmd = cmd;
        this.time = time;
        this.sender = sender;
        this.content = content;
    }

    @Override
    public String toString() {
        return "IMMessage{" +
                "addr='" + addr + '\'' +
                ", cmd='" + cmd + '\'' +
                ", time=" + time +
                ", online=" + online +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                ", terminal='" + terminal + '\'' +
                '}';
    }
}