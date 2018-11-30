<%

    String header = (String)request.getAttribute("header");
    String message = (String)request.getAttribute("message");

%>    
<section class="confirmation">

  <div>

    <header style="text-align: left; font-weight: bold; font-size: 18px; margin-top: 20px; margin-bottom: 20px;">
      <%= header %>
    </header>

    <div class="table" style="display: table; margin: auto;">

      <div style="display: table-row;">
        <div class="cell" style="display: table-cell; margin-left: 0px; text-align: left;">
          <%= message %>
        </div>
      </div>

    </div>

  </div>

</section>
