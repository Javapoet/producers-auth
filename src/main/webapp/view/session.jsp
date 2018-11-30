<%@ page
    language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    session="false"
%>
<%

    HttpSession httpSession = request.getSession(false);
    Integer userIdInteger = null;

    if(
      httpSession != null
      && (userIdInteger = (Integer)httpSession.getAttribute("userId")) != null
    ) {

        logger.debug("userIdInteger = "+userIdInteger);
    }

%>
