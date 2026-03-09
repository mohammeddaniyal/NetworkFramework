package io.github.mohammeddaniyal.nframework.server;
import java.net.*;
import io.github.mohammeddaniyal.nframework.server.annotations.*;
import java.util.*;
import java.lang.reflect.*;
public class NFrameworkServer
{
private ServerSocket serverSocket;
private int port;
private Set<Class> tcpNetworkServiceClasses;
private Map<String,TCPService> tcpServices;
private LogHandler logHandler;
public NFrameworkServer(int port)
{
    this.port=port;
this.tcpNetworkServiceClasses=new HashSet<>();
this.tcpServices=new HashMap<>();
}
public void registerClass(Class c)
{
this.tcpNetworkServiceClasses.add(c);
Path pathOnType;
Path pathOnMethod;
TCPService tcpService;
String servicePath;
pathOnType=(Path)c.getAnnotation(Path.class);
if(pathOnType==null)return;
Method methods[]=c.getMethods();
for(Method method:methods)
{
pathOnMethod=(Path)method.getAnnotation(Path.class);
if(pathOnMethod==null)continue;
servicePath=pathOnType.value()+pathOnMethod.value();
tcpService=new TCPService();
tcpService.c=c;
tcpService.method=method;
tcpService.path=servicePath;
tcpServices.put(servicePath,tcpService);
}
}
public void setLogHandler(LogHandler logHandler)
{
    this.logHandler=logHandler;
}
public TCPService getTCPService(String path)
{
return this.tcpServices.get(path);
/*
Path pathOnType;
Path pathOnMethod;
Method methods[];
String fullPath;
TCPService tcpService=null;
for(Class c:tcpNetworkServiceClasses)
{
pathOnType=(Path)c.getAnnotation(Path.class);
if(pathOnType==null) continue;
methods=c.getMethods();
for(Method method:methods)
{
pathOnMethod=(Path)method.getAnnotation(Path.class);
if(pathOnMethod==null) continue;
fullPath=pathOnType.value()+pathOnMethod.value();
if(path.equals(fullPath))
{
tcpService=new TCPService();
tcpService.c=c;
tcpService.method=method;
tcpService.path=path;
return tcpService;
}
}
}
return null;
*/
}
public void start()
{
    if (this.port <= 0 || this.port > 65535) {
        throw new IllegalArgumentException("Invalid Configuration: Server port must be between 1 and 65535.");
    } 
try
{
this.serverSocket=new ServerSocket(this.port);
Socket socket;
RequestProcessor requestProcessor;
while(true)
{
socket=serverSocket.accept();
if(this.logHandler!=null)
    {
        String timeStamp=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        String clientId=socket.getInetAddress().getHostAddress()+":"+socket.getPort();
        this.logHandler.onOpen(clientId,"["+timeStamp+"]"+" Client Connected : "+clientId);
    }
    requestProcessor=new RequestProcessor(this,socket,logHandler);
}
}catch(Exception Exception)
{
}
}
}
