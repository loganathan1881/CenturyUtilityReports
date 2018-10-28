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
        </c:if><br>
        <h5>RCA Tracker Report - <kbd>${rcaTrackerList[0].executionTime}</kbd></h5> <br>

        <table class="table table-striped">
            <thead>
                <tr>

                    <th>Incident Number</th>
                    <th>Incident Requestor</th>
                    <th>Incident Owner</th>
                    <th>Incident Subject</th>
                    <th>Incident Reqestor Name</th>
                    <th>Incident Solved By</th>
                    <th>Incident Description</th>
                    <th>Incident Status</th>
                    <th>Incident Created Date</th>
                    <th>Incident Assigned Date</th>
                    <th>Incident Modified Date</th>
                    <th>Incident Resolved Date</th>
                    <th>Priority</th>
                </tr>                
            </thead>

            <c:forEach var="rcaTracker" items="${rcaTrackerList}">
                <tr>

                    <td>${rcaTracker.incidentTicketNumber}</td>
                    <td>${rcaTracker.incidentRequestor}</td>
                    <td>${rcaTracker.incidentOwner}</td>
                    <td>${rcaTracker.incidentSubject}</td>
                    <td>${rcaTracker.incidentRequestorName}</td>
                    <td>${rcaTracker.incidentResolvedBy}</td>
                    <td>${rcaTracker.incidentDescription}</td>
                    <td>${rcaTracker.incidentStatus}</td>
                    <td>${rcaTracker.incidentCreatedDate}</td>
                    <td>${rcaTracker.incidentAssignedDate}</td>
                    <td>${rcaTracker.incidentModifiedDate}</td>
                    <td>${rcaTracker.incidentResolvedDate}</td>
                    <td>${rcaTracker.incidentPriority}</td>
                </tr>
            </c:forEach> 
        </table>
    </div>
    </body>
</html>