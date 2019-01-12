<section class="form">

    <form id="form-login" action="<%= contextUrl %>/login" method="post">

      <input type="hidden" name="hash" />

      <fieldset>

        <legend>
          Login
        </legend>
<%

    String fieldName = "username";
    String fieldLabel = "Username";
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
<%

    fieldName = "password";
    fieldLabel = "Password";
    parameterValue = null;

%>
        <p>

          <label for="<%= fieldName %>">
            <%= fieldLabel %>
          </label>

          <input type="password" name="<%= fieldName %>" id="<%= fieldName %>" placeholder="********"
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
          <input type="submit" value="Login" onclick="valid=filterLogin();return valid;" />
        </p>

        <p style="padding: 10px 0px 0px 0px;">
          <a href="<%= contextUrl %>/reset-password">Reset Password</a>
        </p>

        <p style="padding: 0px;">
          <a href="<%= contextUrl %>/register">Sign Up</a>
        </p>

<%--
          <div style="display: table-row;">
            <div class="cell" style="display: table-cell; margin-left: 0px; text-align: left;">

              <div style="width: 100%; text-align: center; margin: auto; padding-top: 10px;">
                <div onclick="clickFacebookButton()" style="display:table;border-radius:3px;width:100%;text-align:left;margin:0px;background-color:#3B5998;padding:0px;cursor:pointer;">
                  <div style="display:table-row;">
                    <div style="display:table-cell;width:50px;height:50px;">
                      <img src="<%= contextUrl %>/images/logos/facebook-logo.png" style="margin:0px;padding:0px;vertical-align:bottom;height:50px;">
                    </div>
                    <div style="display:table-cell;color:#fff;font-size:18px;vertical-align:middle;padding-left:20px;">Login with Facebook</div>
                  </div>  
                </div>
              </div>

            </div>
          </div>

          <div style="display: table-row;">
            <div class="cell" style="display: table-cell; margin-left: 0px; text-align: left;">
          
              <div style="border:0px solid;width:100%;text-align:center;margin:auto;padding-top:10px;">
                <div onclick="clickGoogleButton()" data-onsuccess="onSignIn" style="display:table;border-radius:3px;width:100%;text-align:left;background-color:#4285F4;cursor:pointer;">
                  <div style="display:table-row;">
                    <div style="border:0px solid;display:table-cell;width:50px;height:50px;padding:2px;">
                      <div style="display:table-cell;border:0px solid;width:50px;height:50px;padding:2px;border-radius:2px;background-color:#fff;margin:auto;vertical-align:middle;text-align:center;">
                        <img src="<%= contextUrl %>/images/logos/google-logo.png" style="vertical-align:middle;margin:auto auto auto auto;text-align:center;width:25px;height:25px;border:0px solid;" />
                      </div>
                    </div>
                    <div style="display:table-cell;color:#fff;font-size:18px;vertical-align:middle;padding-left:20px;">Sign in with Google</div>
                  </div>  
                </div>
              </div>          

            </div>
          </div>
--%>


    </fieldset>

  </form>

</section>
