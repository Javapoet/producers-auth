<%

    String code = (String)request.getAttribute("code");

%>
<section class="form">

  <form id="form-password-reset" action="<%= contextUrl %>/password-reset" method="post">
    
    <input type="hidden" name="code" value="<%= code %>" />
    <input type="hidden" name="hash" />

    <fieldset>

      <legend>
        Password Reset
      </legend>

<%
    String fieldName = "password";
    String fieldLabel = "Password";
    String parameterValue = null;
%>
      <p>

        <label for="<%= fieldName %>">
          <%= fieldLabel %>
        </label>

        <input type="password" name="<%= fieldName %>" id="<%= fieldName %>" placeholder="********" />

      </p>

      <p>
        <input type="submit" value="Reset" onclick="valid=filterResetPassword();return valid;" />
      </p>

      <p style="padding: 10px 0px;">
        <a href="<%= contextUrl %>/login">Login</a>
      </p>

    </fieldset>

  </form>
</section>
