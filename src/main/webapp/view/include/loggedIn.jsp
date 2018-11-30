<%

    int userId = -1;

    if(userIdInteger != null) {
        userId = userIdInteger.intValue();
        logger.debug("userId = "+userId);
    }

%>    
<section class="confirmation">

  <div>

    <header style="text-align: left; font-weight: bold; font-size: 18px; margin-top: 20px; margin-bottom: 20px;">
      <%--= header --%>
      Logged In
    </header>

    <div class="table" style="display: table; margin: auto;">

      <div style="display: table-row;">
        <div class="cell" style="display: table-cell; margin-left: 0px; text-align: left;">
          userId = <%= userId %>
        </div>
      </div>

    </div>

  </div>

</section>
