<%-- 
    Document   : index
    Created on : 4 sept. 2012, 21:39:38
    Author     : rapha
--%>

<jsp:useBean id="database" scope="session" class="FreeTax.Database"/>

<%@taglib uri="/WEB-INF/tlds/Tags.tld" prefix="status" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Connexion</title>
    </head>
    <body>
        <h1>Connexion</h1>
        
        
        <form action="Magasin.jsp" method="get">
            <strong>Langue: </strong>
            <select name="langue">
            <% for(String langue : database.listerLangues()) { %>
                <option value="<% out.print(langue); %>">
                    <% out.print(langue); %>
                </option>
            <% } %>
            </select><br />
            
            <strong>Name: </strong>
            <input type="text" name="nom"/><br />
            
            <strong>Reservation code: </strong>
            <input type="text" name="code"/><br />

            <strong>Email: </strong>
            <input type="text" name="email"/><br />
            
            <input type="hidden" name="start" 
                   value="<%= FreeTax.DateTimeTag.currentMillis() %>"/>
            
            <input type="submit"/>
        </form>
    </body>
    
    <footer>
        <p>
            Date: <status:date-heure />
        </p>
        
        <p>
            Durée de la dernière session: 
            <status:using-time start="<%= request.getParameter("start") %>" />
        </p>
    </footer>
</html>