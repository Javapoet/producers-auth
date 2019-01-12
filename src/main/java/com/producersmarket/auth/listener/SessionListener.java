package com.producersmarket.auth.listener;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SessionListener
  implements HttpSessionListener
{

    private static final Logger logger = LogManager.getLogger();

    private int sessionCount;
    //private Map<String, HttpSession> sessionMap = new HashMap<String, HttpSession>();
    private Map<String, HttpSession> sessionMap = new java.util.concurrent.ConcurrentHashMap<String, HttpSession>();

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        logger.debug("sessionCreated("+httpSessionEvent+")");

        HttpSession httpSession = httpSessionEvent.getSession();

        logger.debug("httpSession.getId() = "+httpSession.getId());
        logger.debug("httpSession.getAttributeNames() = "+httpSession.getAttributeNames());
        logger.debug("httpSession.getCreationTime() = "+httpSession.getCreationTime());
        logger.debug("httpSession.getLastAccessedTime() = "+httpSession.getLastAccessedTime());

        logger.info("Session timeout is "+httpSession.getMaxInactiveInterval()+" seconds.");

        //java.util.Enumeration<java.lang.String>   getAttributeNames()  // Returns an Enumeration of String objects containing the names of all the objects bound to this session.
        //long getCreationTime() // Returns the time when this session was created, measured in milliseconds since midnight January 1, 1970 GMT.
        //java.lang.String getId() // Returns a string containing the unique identifier assigned to this session.
        //long getLastAccessedTime()  // Returns the last time the client sent a request associated with this session, as the number of milliseconds since midnight January 1, 1970 GMT, and marked by the time the container received the request.
        //int getMaxInactiveInterval()  // Returns the maximum time interval, in seconds, that the servlet container will keep this session open between client accesses.

        String sessionId = httpSession.getId();
        logger.debug("sessionId = "+sessionId);

        synchronized(this) {
            sessionCount++;
        }
        logger.debug("sessionCount = "+sessionCount);

        if(!sessionMap.containsKey(sessionId)) {
            try {
                sessionMap.put(sessionId, httpSession);
            } catch(Exception e) {}
        } else {
            logger.warn("sessionCreated(httpSessionEvent:"+httpSessionEvent+"): sessionMap.containsKey("+sessionId+")");
        }

        logger.debug("sessionMap.size() = "+sessionMap.size());
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        logger.debug("sessionDestroyed(httpSessionEvent:"+httpSessionEvent+")");

        HttpSession httpSession = httpSessionEvent.getSession();

        String sessionId = httpSession.getId();
        logger.debug("sessionId = "+sessionId);

        synchronized(this) {
            --sessionCount;
        }
        logger.debug("sessionCount = "+sessionCount);

        if(sessionMap.containsKey(sessionId)) {
            try {
                sessionMap.remove(sessionId);
            } catch(Exception e) {}
        } else {
            logger.warn("sessionCreated(httpSessionEvent:"+httpSessionEvent+"): !sessionMap.containsKey("+sessionId+")");
        }

        logger.debug("sessionMap.size() = "+sessionMap.size());
    }

    public int getSessionCount() {
        return sessionCount;
    }

    public HttpSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public Set getSessionIds() {
        if(sessionMap != null) return sessionMap.keySet();
        return null;
    }

}
