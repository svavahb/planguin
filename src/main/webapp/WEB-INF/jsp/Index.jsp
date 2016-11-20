<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html lang="">

    <head>
        <%@ page contentType="text/html; charset=UTF-8"%>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"

              integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
        <link href="../css/stylesheet.css" rel="stylesheet" type="text/css">
        <title>Planguin</title>
    </head>

        <jsp:include page="Header.jsp"/>

    <body>

    <main>
        <section class="col-md-8">

            <ul>
                <li>
                    <span class="glyphicon glyphicon-calendar"/>  Your own personal planner </span>
                </li>
                <ul>
                <li>
                    <span class="glyphicon glyphicon-user">  Create your own schedule </span>
                </li>
                <li>
                    <span class="glyphicon glyphicon-link">  Create groups with friends </span>
                </li>
                <li>
                    <span class="glyphicon glyphicon-transfer">  Compare schedules within groups</span>
                </li>
                </ul>
            </ul>

        </section>

        <section class="col-md-4">
            <a role="button" class="btn btn-primary btn-lg" href="/login">Log In!</a>
            <a role="button" class="btn btn-danger btn-lg" href="/signup">Sign Up!</a>
        </section>

    </main>

    </body>
</html>
