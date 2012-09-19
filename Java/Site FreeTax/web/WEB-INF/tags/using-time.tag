<%-- 
    Document   : using-time
    Created on : 5 sept. 2012, 08:56:33
    Author     : rapha
--%>

<%@tag import="FreeTax.DateTimeTag"%>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="start"%>

<%-- any content can be specified here e.g.: --%>
<%
if (start != null) {
%>
    <%= FreeTax.DateTimeTag.usingTime(Long.parseLong(start)) %> 
    secondes d'utilisation
<%
} else {
%>
    Pas d'information
<%
} %>