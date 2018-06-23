<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
    <jsp:include page="../include/header.jsp" />

    <body>

    <!-- TODO - Need to write the logic to display reports dynamically -->

    <div class="container">
        <c:if test="${not empty msg}">
                <strong>${msg}</strong>
            </div>
        </c:if>
        <h3>DB Health Check Report for ${date}</h3>

        <table class="table table-striped">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Data Validation Scenario</th>
                    <th>Count</th>
                </tr>                
            </thead>

            <c:forEach var="dbHealthCheck" items="${dbHealthCheckList}">
                <tr>
                    <td>${dbHealthCheck.id}</td>
                    <td>${dbHealthCheck.scenarioName}</td>
                    <td>${dbHealthCheck.count}</td>
                </tr>
            </c:forEach> 
        </table>
    </div>
    </body>
</html>
