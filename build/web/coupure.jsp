<%@page import="mg.sarobidy.prevision.coupure.*"%>
<%@page import="mg.sarobidy.prevision.energie.*"%>
<%
    Coupure coupure = (Coupure) request.getAttribute("coupure");
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!--<link rel="stylesheet" href="assets/css/bootstrap.min.css"/>-->
        <title>JSP Page</title>
    </head>
    <body>
        <div class="container">
            <div class="container">
                <div class="container">
                    <div class="row">
                        <h3 class="text-decoration-underline text-center">
                            Détail de coupure le : <%= coupure.getDateCoupure() %>
                        </h3>
                        <div class="row">
                            <table class="table" border="1">
                                <thead>
                                    <th>Secteur</th>
                                    <th>Date et heure de coupure</th>
                                    <th>Consommation moyenne(w / eleve)</th>
                                    <th>Détails</th>
                                </thead>
                                <tbody>
                                    <%
                                        for( int i = 0; coupure != null && i < coupure.getSecteurs().length ; i++ ){ %>
                                            <tr>
                                                <td>
                                                    <h3> <%= coupure.getSecteurs()[i].getNom() %> </h3>
                                                </td>
                                                <td>
                                                    <%= ( coupure.getSecteurs()[i].getFirstCoupure() != null ) ? coupure.getSecteurs()[i].getFirstCoupure().getTimes() : "coupure a venir" %>
                                                </td>
                                                <td>
                                                    <%= coupure.getSecteurs()[i].getMoyenne() %>
                                                </td>
                                                <td>
                                                    <table class="table" border="1">
                                                        <thead>
                                                            <th>Temps</th>
                                                            <th>Reste Batterie (wh)</th>
                                                            <th>Output panneau (wh)</th>
                                                            <th>Besoin Eleve (wh)</th>
                                                            <th>Presence</th>
                                                            <th> Etat </th>
                                                        </thead>
                                                        <tbody>
                                                            <%
                                                                for( int j = 0; j < coupure.getSecteurs()[i].getEtatAsArray().length ; j++ ){ %>

                                                                    <tr>
                                                                        <td> 
                                                                            <%= coupure.getSecteurs()[i].getEtatAsArray()[j].getTimes() %> 
                                                                        </td>
                                                                         <td>
                                                                            <%= coupure.getSecteurs()[i].getEtatAsArray()[j].getCurrentBatterie() %>
                                                                        </td> 
                                                                        <td>
                                                                            <%= coupure.getSecteurs()[i].getEtatAsArray()[j].getOutputPanneau() %>
                                                                        </td>
                                                                        <td>
                                                                            <%= coupure.getSecteurs()[i].getEtatAsArray()[j].getBesoinEleve() %>
                                                                        </td>
                                                                        <td>
                                                                            <%= coupure.getSecteurs()[i].getEtatAsArray()[j].nbr%>
                                                                        </td>
                                                                        <td class="<%= (coupure.getSecteurs()[i].getEtatAsArray()[j].isTapaka()) ? "text-danger" : "text-success" %>">
                                                                            <%=
                                                                                (coupure.getSecteurs()[i].getEtatAsArray()[j].isTapaka() ) ? "Tapaka" : "Tsy tapaka"
                                                                            %>
                                                                        </td>
                                                                    </tr>

                                                            <%    }
                                                            %>
                                                        </tbody>
                                                    </table>

                                                </td>
                                            </tr>
                                    <%    }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </body>
</html>
