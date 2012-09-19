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
        <title>Commande</title>
    </head>
    <body>
        <%
            int code_reserv = Integer.parseInt(request.getParameter("code"));
            boolean carte_valide = achats.gererCommande(code_reserv, request);
        %>
        
        <a href="Contexte.jsp">Changer d'utilisateur</a>
        
        <h1>Commande</h1>
        
        <%
        if (carte_valide) {
        %>
            <p>
            <strong>Commande validée</strong>
            </p>
        <%
        } else if (request.getParameter("carte") != null) {
        %>
            <p>
            <strong>Carte non valide !</strong>
            </p>
        <%
        }
        %>
        
        <h2>Articles dans le panier</h2>
        
        <ul>
            <%
            
            double total = 0;
            boolean vide = true;
            for (Produit p : database.listerProduits(code_reserv)) { %>
                <li>
                    <strong><%= p.getNom() %></strong>
                    (<%= p.getCategorie() %>) - 
                    <%= p.getPrix() %> <%= p.getUnite() %>
                    
                </li>
                <% 
                total += p.getPrix();
                vide = false;
            }
            %>
        </ul>
        
        <%
        if (vide) { %>
            <p>    
                <strong> Caddie vide</strong><br />
            </p>
                
                <a href="Magasin.jsp?code=<%= code_reserv %>&start=<%= request.getParameter("start") %>">
                    Retour au magasin sans commander
                </a>
            </p>
        <%
        } else {
        %>
            <p>
                <strong>Total: </strong> <%= total %><br />
            </p>

            <p>
                <form action="Commande.jsp" method="get">
                    Code de votre carte bancaire: <input type="text" name="carte" />
                    
                    <input type="hidden" name="code" value="<%= code_reserv %>"/>
                    <input type="hidden" name="start" 
                           value="<%= request.getParameter("start") %>"/>
                    
                    <input type="submit" value="Commander" />
                </form>
            </p>

            <p>
                <a href="Magasin.jsp?code=<%= code_reserv %>&start=<%= request.getParameter("start") %>">
                    Retour au magasin sans commander
                </a>
            </p>
        <%
        }
        %>
        
        <h2>Articles déjà commandés</h2>
        
        <status:list code_reserv="<%= Integer.toString(code_reserv) %>"/>
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
