package io.github.mohammeddaniyal.nframework.server;
public interface LogHandler
{
    public void onOpen(String clientId,String log);
    public void setLog(String clientId,String type,String log);
    public void onClose(String clientId,String log);
}
