<!-- $Id: propsmain.jsp 333 2007-05-21 15:13:27Z sweiss $ -->
<%
    response.setHeader( "Cache-Control", "no-cache" );
    response.setHeader( "Pragma", "No-cache" );
    response.setDateHeader( "Expires", 0 );
%>

<%@ page buffer="500kb" autoFlush="false" %>
<%@ page session="true" %>

<!-- $Id: propsmain.jsp 333 2007-05-21 15:13:27Z sweiss $ -->
<html>
<head>
<title>Properties Administration</title>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=iso-8859-1">
<link type="text/css" rel="stylesheet" href="styles.css">
</head>
<body LEFTMARGIN=0 TOPMARGIN=0 MARGINWIDTH=0 MARGINHEIGHT=0>

<!--main body-->
<%
    try {
        String html = request.getAttribute( "html" ).toString();
        out.println( html );
    } catch ( Exception ex ) {
        ex.printStackTrace();
        out.println( "Could not retrieve dynamic content!" );
    }
%>
Go <a href="..">back...</a>
</body>
</html>

