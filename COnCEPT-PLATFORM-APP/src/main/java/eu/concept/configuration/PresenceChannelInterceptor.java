package eu.concept.configuration;

import eu.concept.repository.concept.dao.ChatMessageRepository;
import eu.concept.repository.concept.domain.ChatMessage;
import eu.concept.util.other.Patterns;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.util.HtmlUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.regex.Matcher;

/**
 * Created by nikos on 7/9/2015.
 */
public class PresenceChannelInterceptor extends ChannelInterceptorAdapter {
    private final Log logger = LogFactory.getLog(PresenceChannelInterceptor.class);

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        //StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
        if(message.getHeaders().containsKey("stompCommand") && message.getHeaders().get("stompCommand").equals(StompCommand.SEND) && !message.getHeaders().containsKey("simpSubscriptionId")){
        //if(sha.getCommand().equals(StompCommand.SEND) && sha.getSubscriptionId()==null){
            Message<?> newMessage = new Message<Object>() {
                @Override
                public Object getPayload() {
                    String messageStr = null;
                    try {
                    messageStr = new String((byte[])message.getPayload(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    messageStr = HtmlUtils.htmlEscape(messageStr);
                    Matcher webLink = Patterns.WEB_URL.matcher(messageStr);
                    String tmp = new String(messageStr);
                    while(webLink.find()) {
                        String hrefStr = webLink.group();
                        if(!(hrefStr.startsWith("http://") || hrefStr.startsWith("https://"))) hrefStr = "http://"+hrefStr;
                        tmp = messageStr.replace(webLink.group(), "<a href=\"" + hrefStr + "\" target=\"_blank\">" + webLink.group() + "</a>");
                    }
                    return  tmp.getBytes(Charset.forName("UTF-8"));
                }
                @Override
                public MessageHeaders getHeaders() {
                    return message.getHeaders();
                }
            };
            return super.preSend(newMessage, channel);
        }

        return super.preSend(message, channel);
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {

        //StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
        //System.out.println("!!!!!!!!!!!!!!!! command "+sha+"-- "+ sha.getSessionId()+" --> "+message.toString());
        if(message.getHeaders().containsKey("stompCommand") && message.getHeaders().get("stompCommand").equals(StompCommand.SEND) && !message.getHeaders().containsKey("simpSubscriptionId")) {
            try {
                String messageStr = new String((byte[]) message.getPayload(), "UTF-8");
                StompHeaderAccessor sha = StompHeaderAccessor.wrap(message);
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setContent(messageStr);
                chatMessage.setCreatedDate(new Date());
                String idStr = sha.getDestination().substring(sha.getDestination().lastIndexOf('/') + 1);
                int id = Integer.parseInt(idStr);
                chatMessage.setPid(id);
                chatMessageRepository.save(chatMessage);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }
}
