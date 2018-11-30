<%@ page
    language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    session="false"
%>
<%

    HttpSession httpSession = request.getSession(false);
    Object userIdObject = null;

    if(
      httpSession != null
      && (userIdObject = httpSession.getAttribute("userId")) != null
    ) {

        logger.debug("userIdObject = "+userIdObject);
    }

%>