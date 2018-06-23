<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">
    <jsp:include page="../include/header.jsp" />

    <body>

    <!-- TODO - Need to write the logic to display reports dynamically -->

    <div class="container">
        <div class="row">
            <div class="col-md-3">

                <div class="card card-inverse text-center" style="max-width: 202px;">
                    <h6>DB Health Check - ${message}</h6>
                    <img class="card-img-top" src="resources/images/dashboard_chart_1.png" alt="View Reports">
                    <p>
                        <a class="btn btn-primary" href="dbhealthcheck" role="button" >View Report</a>
                    </p>
                </div>
            </div>
            <div class="col-md-3">

                <div class="card card-inverse text-center" style="max-width: 202px;">
                    <h6>MVR Scheduler Report</h6>
                    <img class="card-img-top" src="resources/images/dashboard_chart_2.png" alt="View Reports">
                    <p>
                        <a class="btn btn-primary" href="#" role="button" >View Report</a>
                    </p>
                </div>
            </div>
            <div class="col-md-3">

                <div class="card card-inverse text-center" style="max-width: 202px;">
                    <h6>Exception Log Report</h6>
                    <img class="card-img-top" src="resources/images/dashboard_chart_3.png" alt="View Reports">
                    <p>
                        <a class="btn btn-primary" href="#" role="button" >View Report</a>
                    </p>
                </div>
            </div>
            <div class="col-md-3">

                <div class="card card-inverse text-center" style="max-width: 202px;">
                    <h6>RSM Record Count Check</h6>
                    <img class="card-img-top" src="resources/images/dashboard_chart_2.png" alt="View Reports">
                    <p>
                        <a class="btn btn-primary" href="#" role="button" >View Report</a>
                    </p>
                </div>
            </div>
            <div class="col-md-3">

                <div class="card card-inverse text-center" style="max-width: 202px;">
                    <h6>RCA Tracker</h6>
                    <img class="card-img-top" src="resources/images/dashboard_chart_5.png" alt="View Reports">
                    <p>
                        <a class="btn btn-primary" href="#" role="button" >View Report</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
    </body>
</html>

