<%-- 
    Document   : list
    Created on : 5 sept. 2012, 08:42:36
    Author     : rapha
--%>

<jsp:useBean id="database" scope="session" class="FreeTax.Database"/>
<%@tag import="FreeTax.Produit"%>

<%@tag description="put the tag description here" pageEncoding="UTF-8"%>

<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="code_reserv"%>

<%-- any content can be specified here e.g.: --%>
<ul>
    <%
    boolean vide = true;
for (Produit p : database.listerProduitsCommandes(Integer.parseInt(code_reserv))) { %>
        <li>
            <strong><%= p.getNom() %></strong>
            (<%= p.getCategorie() %>)
        </li>
        <% 
        vide = false;
    } %>
</ul>

<%    
if (vide) {
%>
    <strong>Aucun article commandé</strong>
<% 
}
%>