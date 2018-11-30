<div class="header-left">

  <div class="button-menu">

<%

    //HttpSession session = request.getSession(false);
    //Object userIdObject;

    if(
      //session != null
      //&& (userIdObject = session.getAttribute("userId")) != null
      userIdObject != null
    ) {

        logger.debug("userIdObject = "+userIdObject);

%>
    <div class="button-cell">
      <div class="button" style=""><a href="<%= contextUrl %>/logout">Logout</a></div>
    </div>              
<%

    } else {

%>      
    <div class="button-cell">
      <div class="button" style=""><a href="<%= contextUrl %>/login">Login</a></div>
    </div>              
<%
    }
%>

  </div>

</div>
