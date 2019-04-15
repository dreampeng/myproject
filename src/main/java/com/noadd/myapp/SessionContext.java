package com.noadd.myapp;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class SessionContext {
    private static HashMap<String, Object> mymap = new HashMap<>();

    static synchronized void addSession(HttpSession session) {
        if (session != null) {
            mymap.put(session.getId(), session);
        }
    }

    static synchronized void delSession(HttpSession session) {
        if (session != null) {
            mymap.remove(session.getId());
            session.invalidate();
        }
    }

    private static synchronized HttpSession getSession(String session_id) {
        if (session_id == null)
            return null;
        return (HttpSession) mymap.get(session_id);
    }

    public static synchronized void delSession(String session_id) {
        if (session_id == null)
            return;
        delSession(getSession(session_id));
    }
}
