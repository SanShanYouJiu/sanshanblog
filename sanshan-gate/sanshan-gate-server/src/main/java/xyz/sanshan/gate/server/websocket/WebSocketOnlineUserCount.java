package xyz.sanshan.gate.server.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * something
 *
 */
@ServerEndpoint(value = "/websocket/{userId}")
@Component
@Slf4j
public class WebSocketOnlineUserCount {

    private static AtomicLong onlineCount = new AtomicLong(0);

    private static CopyOnWriteArraySet<WebSocketOnlineUserCount> webSocketSet = new CopyOnWriteArraySet<>();

    private Session session;

    private Long userId;

    @OnOpen
    public void onOpen(@PathParam("userId")Long userId, Session session) {
        for (WebSocketOnlineUserCount entity : webSocketSet){
            if (userId.equals(entity.userId)){
                return;
            }
        }
        this.session = session;
        this.userId = userId;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新连接加入！当前在线人数位" + getOnlineCount());
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有连接断开！当前在线人数为" + getOnlineCount());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("来自客户端的消息：" + message + this.userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误：" + error.getMessage());
    }

    public void sendMessage(String message, List<Long> userIds) {
        log.info("推送对象：{},推送内容：{}", userIds, message);
        for (WebSocketOnlineUserCount entity : webSocketSet){
            if (userIds.contains(entity.userId)){
                log.info("webSocket对象ID:{}", entity.userId);
                try {
                    entity.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    log.error("推送消息失败：{}", e.getMessage());
                }
            }
        }
    }

    public void sendObject(Object data, List<Long> userIds) throws IOException, EncodeException {
        for (WebSocketOnlineUserCount entity : webSocketSet){
            if (userIds.contains(entity.userId)){
                entity.session.getBasicRemote().sendObject(data);
            }
        }
    }

    public static  long getOnlineCount(){
        return onlineCount.get();
    }

    public static  long addOnlineCount(){
      return   WebSocketOnlineUserCount.onlineCount.incrementAndGet();
    }

    public static  long subOnlineCount(){
        return  WebSocketOnlineUserCount.onlineCount.getAndDecrement();
    }

    public Session getSession(){
        return this.session;
    }
}
