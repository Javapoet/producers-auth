<%@ include file="/view/session.jsp" %>
<%@ include file="/view/imports.jsp" %>
<%@ include file="/view/logger.jsp" %>

<!doctype html>
<%

    //String contextUrl = InitServlet.properties.getProperty("contextUrl");
    String serverUrl = (String)application.getAttribute("serverUrl");
    String contextUrl = (String)application.getAttribute("contextUrl");
    String pageTitle = "Producers Market Blog";
    String metaAuthor = "Producers Market";
    String metaDescription = "";
    String metaKeywords = "producers market blog";

    Product headerProduct = (Product)request.getAttribute("headerProduct");
    
%>

<html lang="en">

  <%--@ include file="/view/blog/blog-head.jsp" --%>

  <head>
    <meta charset="utf-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <% if(pageTitle != null) { %><title><%= pageTitle %></title><% } %>
    <% if(metaAuthor != null) { %><meta name="author" content="<%= metaAuthor %>" /><% } %>
    <% if(metaDescription != null) { %><meta name="description" content="<%= metaDescription %>" /><% } %>
    <% if(metaKeywords != null) { %><meta name="keywords" content="<%= metaKeywords %>" /><% } %>

    <base href="<%= contextUrl %>" />
    <link rel="stylesheet" href="<%= serverUrl %>/view/css/blog-style.jsp?<%= new java.util.Date().getTime() %>" />
    <link rel="stylesheet" href="<%= contextUrl %>/view/css/blog-style.jsp?<%= new java.util.Date().getTime() %>" />
    <link rel="stylesheet" href="<%= contextUrl %>/view/css/form-style.jsp" />
    <link rel="shortcut icon" sizes="16x16 32x32 64x64" href="favicon.ico" type="image/x-icon" />
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet"> 

    <script defer src="view/js/fontawesome/5.0.9/fontawesome-all.js"></script>

    <script type="text/javascript">
      <%@ include file="/view/js/sha1-min.js" %>
      function filterLogin(){
        alert('filterLogin()');

        var passwordInput=document.getElementById('password');
        var password=passwordInput.value;
        alert('password = '+password);
        var passwordSha1=hex_sha1(password);
        alert('passwordSha1 = '+passwordSha1);
        document.getElementById('hash').value=passwordSha1;
      };
    </script>

    <script type="text/javascript">
        
        function filterResetPassword() {
            console.log('filterResetPassword()');

            var valid=validateResetPassword();
            console.log('valid = '+valid);

            return valid;
        }

        function validateResetPassword(){
            console.log('validateResetPassword()');

            var form = document.getElementById('form-password-reset');
            console.log('form = '+form);

            var password=form['password'].value;
            var passwordSha1=hex_sha1(password);
            console.log('passwordSha1 = "'+passwordSha1+'"');

            //document.getElementById('hash').value=passwordSha1;
            form['hash'].value=passwordSha1;
            //form['password'].value=passwordSha1;
            form['password'].disabled='disabled';  // do not send the password, for security
            //form['confirmPassword'].disabled='disabled'; // do not send the password, for security

            return true;
        };

    </script>

  </head>

  <body>

    <%@ include file="/view/blog/blog-header.jsp" %>

    <%--@ include file="/view/blog/product-categories.jsp" --%>

    <%@ include file="/view/include/password-reset.jsp" %>

    <%@ include file="/view/footer.jsp" %>

  </body>

</html>
