<%@ include file="/view/session.jsp" %>
<%@ include file="/view/logger.jsp" %>
<%@ include file="/view/imports.jsp" %>
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
<!doctype html>
<html lang="en">

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
    <link rel="shortcut icon" sizes="16x16 32x32 64x64" href="favicon.ico" type="image/x-icon" />
    <link href="https://fonts.googleapis.com/css?family=Roboto" rel="stylesheet"> 

    <script defer src="view/js/fontawesome/5.0.9/fontawesome-all.js"></script>

    <style type="text/css">

        section.notification
        {
            width: var(--sitewidth);
            margin: auto;
            font-family: Roboto;
        }

        section.notification header
        {
          /*
          font-family: BarlowCondensed-Medium;
          font-family: BarlowCondensed-SemiBold;
          */
          font-family: Roboto;
          font-weight: bold; 
          text-transform: uppercase;
          font-size: 20px;
          text-align: left;
          font-weight: bold;
          margin-top: 20px;
          /*
          margin-bottom: 20px;
          border-bottom: var(--mediumgray);
          */
        }

        section.notification > div
        {
            max-width: 400px;
            border: 0px;
            margin: auto;
        }

    </style>

  </head>

  <body>

    <%--@ include file="/view/header/blog-header.jsp" --%>
    <%@ include file="/view/blog/blog-header.jsp" %>

    <%--@ include file="/view/blog/product-categories.jsp" --%>

    <%@ include file="/view/include/email-sent.jsp" %>

    <%@ include file="/view/footer.jsp" %>

  </body>

</html>
