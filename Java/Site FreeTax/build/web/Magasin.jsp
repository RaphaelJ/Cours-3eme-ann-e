<%-- 
    Document   : Magasin
    Created on : 4 sept. 2012, 22:51:40
    Author     : rapha
--%>

<jsp:useBean id="database" scope="session" class="FreeTax.Database"/>
<jsp:useBean id="achats" scope="session" class="FreeTax.Achats"/>
<%@taglib uri="/WEB-INF/tlds/Tags.tld" prefix="status" %>
<%@page import="FreeTax.Produit"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Magasin</title>
    </head>
    <body>
        <%
            int code_reserv = Integer.parseInt(request.getParameter("code"));
            achats.gererAchats(code_reserv, request);
        %>
        
        <a href="Contexte.jsp">Changer d'utilisateur</a>
        
        <h1>Magasin</h1>
        
        <h2>Caddie</h2>
        
        <ul>
            <%
            boolean vide = true;
            for (Produit p : database.listerProduits(code_reserv)) { %>
                <li>
                    <strong><%= p.getNom() %></strong>
                    (<%= p.getCategorie() %>) - 
                    <%= p.getPrix() %> <%= p.getUnite() %>
                    <a href="Magasin.jsp?code=<%= code_reserv %>&enlever=<%= p.getId() %>&start=<%= request.getParameter("start") %>">
                        Enlever
                    </a>
                </li>
                <% 
                vide = false;
            } %>
        </ul>
        
        <%    
        if (vide) {
        %>
            <strong>Caddie vide</strong>
        <% } else { %>
            <p>
                <a href="Commande.jsp?code=<%= code_reserv %>&start=<%= request.getParameter("start") %>">
                    Commander
                </a>
            <p>
        <% } %>
        
        <h2>Produits</h2>
        
        <ul>
            <% for (Produit p : database.listerProduits()) { %>
                <li>
                    <strong><%= p.getNom() %></strong>
                    (<%= p.getCategorie() %>) - 
                    <%= p.getPrix() %> <%= p.getUnite() %>
                    <a href="Magasin.jsp?code=<%= code_reserv %>&ajout=<%= p.getId() %>&start=<%= request.getParameter("start") %>">
                        Commander
                    </a>
                </li>
            <% } %>
        </ul>
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
