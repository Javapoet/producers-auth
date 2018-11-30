<div class="header-left">

  <div class="button-menu">
<%

    //HttpSession session = request.getSession(false);
    //Object userIdObject;

    if(userIdInteger != null) {

        logger.debug("userIdInteger = "+userIdInteger);

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
