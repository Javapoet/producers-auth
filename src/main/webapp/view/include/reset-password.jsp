<section class="form">

  <form action="<%= contextUrl %>/reset-password" method="post">
    <input type="hidden" name="hash" />

    <fieldset>

      <legend>
        Reset Password
      </legend>

<%

    String fieldName = "email";
    String fieldLabel = "Email Address";
    String parameterValue = null;

%>
      <p>

        <label for="<%= fieldName %>">
          <%= fieldLabel %>
        </label>

        <input type="email" name="<%= fieldName %>" id="<%= fieldName %>" placeholder="name@example.com"
<%

    parameterValue = request.getParameter(fieldName);
    if(parameterValue != null) {

%>
          value="<%= parameterValue %>"
<%
    }
%>
        />

      </p>

      <p>
        <input type="submit" value="Reset" />
      </p>

      <p style="padding: 10px 0px;">
        <a href="<%= contextUrl %>/login">Log in</a>
      </p>

    </fieldset>
  </form>
</section>
