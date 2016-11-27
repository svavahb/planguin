<!DOCTYPE html>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<html lang="">

    <head>
        <%@ page contentType="text/html; charset=UTF-8"%>
            <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet"
                  integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
            <link href="../css/stylesheet.css" rel="stylesheet" type="text/css">
        <title>home Page </title>

            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>jQuery UI Datepicker - Default functionality</title>
            <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
            <link rel="stylesheet" href="/resources/demos/style.css">
            <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
            <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
            <script src="../src/main/resources/static.css/combodate.js"></script>
            <script>
                $( function() {
                    $( "#datepicker" ).datepicker();
                } );
            </script>
    </head>
    <jsp:include page="Header.jsp"/>
    <body>

    <div class="row">

        <div class="col-md-10">
            <h2>Welcome ${loggedInUser}!</h2>
        </div>

        <div class="col-md-2">
            <a class="btn btn-danger" role="button" href="/logout">Log out!</a>
        </div>

    </div>

    <div class="col-md-2">

        <div class="form-group">

        <sf:form method="POST" commandName="scheduleItem" action ="/home">

               <div>
                   <c:if test = "${hasErrors}">
                    <c:forEach var="error" items="${errors}">
                        <p class="alert alert-danger">${error.getCode()}
                    </c:forEach>
                </c:if>
               </div>

                        <label> Title:</label>
                        <sf:input class="form-control" path ="title" type="text" placeholder="Enter title"/>

                        <label> Location:</label>
                        <sf:input class="form-control" path="location" type="text" placeholder="Choose location"/>

                        <label> Description:</label>
                        <sf:input class="form-control" path="description" type="text" placeholder="Enter description"/>

                        <label> Day:</label>
                        <sf:input class="form-control" path="date" type="text" placeholder="Choose date" id="datepicker"/>

                        <label> Start time:</label>
                        <%--<td><sf:input path="startstrin" type="text" placeholder="HH:MM - please pick a time on the 10 min interval" /></td>--%>
                        <sf:select class="btn btn-default dropdown-toggle form-control " type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" path="startstring">
                            <sf:options items="${timeSlots}"/>
                        </sf:select>

                        <label> End time:</label>
                        <%--td><sf:input path="endstring" type="text" placeholder="HH:MM - please pick a time on the 10 min interval" /></td--%>
                        <sf:select class="btn btn-default dropdown-toggle form-control" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" path="endstring">
                            <sf:options items="${timeSlots}"/>
                        </sf:select>

                        <label> Color:</label>
                        <sf:select class="btn btn-default dropdown-toggle form-control" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" path="color">
                            <sf:option value="Red"/>
                            <sf:option value="Orange"/>
                            <sf:option value="Yellow"/>
                            <sf:option value="Green"/>
                            <sf:option value="Blue"/>
                            <sf:option value="Purple"/>
                            <sf:option value="Pink"/>
                            <sf:option value="White"/>
                            <sf:option value="Gray"/>
                        </sf:select>

                        <label> Filter:</label>
                        <sf:select class="btn btn-default dropdown-toggle form-control" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" path="filter">
                            <sf:option value="School"/>
                            <sf:option value="Work"/>
                            <sf:option value="Appointment"/>
                            <sf:option value="Other"/>
                        </sf:select>

                <button class="btn btn-primary" type="submit" value="Create scheduleItem!">Insert scheduleitem!</button>

            </sf:form>
        </div>
    </div>

        <div class="col-md-7">
        <label>Choose filter you want to view</label>
        <form action="/scheduleByFilter">
            <select name="selectedFilter" class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
                <option class="dropdown-menu" aria-labelledby="dropdownMenu1" value="1" label="--Select filter--"/>
                <c:forEach var="filters" items="${filters}">
                    <option value="${filters}" label="${filters}"></option>
                </c:forEach>

            </select>
            <button class="btn btn-primary" type="submit" value="Submit">Submit!</button>
        </form>

        <table class="table-bordered" cellspacing="0">
            <tbody>
            <tr>
                <th align="center" valign="middle" width="80"></th>
                <th align="center" valign="middle" width="100">Monday</th>
                <th align="center" valign="middle" width="100">Tuesday</th>
                <th align="center" valign="middle" width="100">Wednesday</th>
                <th align="center" valign="middle" width="100">Thursday</th>
                <th align="center" valign="middle" width="100">Friday</th>
                <th align="center" valign="middle" width="100">Saturday</th>
                <th align="center" valign="middle" width="100">Sunday</th>
            </tr>
            <c:set var="timespan" value="0"/>
            <c:set var="timespan2" value="0"/>
            <c:set var="timespan3" value="0"/>
            <c:set var="timespan4" value="0"/>
            <c:set var="timespan5" value="0"/>
            <c:set var="timespan6" value="0"/>
            <c:set var="timespan7" value="0"/>
            <c:set var="counter" value="0"/>
            <c:forEach var="slot" items="${timeSlots}">
                <tr>
                    <c:if test="${counter mod 6 == 0}">
                        <td align="center" valign="middle" width="80" height="10" rowspan="6">
                            <c:out value="${slot}"/>
                        </td>
                    </c:if>
                    <c:set var="counter" value="${counter+1}"/>
                    <%-- MÁNUDAGUR --%>
                    <c:set var="foundItem" value="false"/>
                    <c:forEach var="item" items="${scheduleItems}">
                        <c:if test="${item.startstring eq slot && item.weekDay eq 0}">
                            <td title="From: ${item.startstring}&#013;To: ${item.endstring}&#013;${item.description}"
                                align="center" valign="middle" width="100" rowspan="${item.timeSpan}" class="${item.color}">
                                <c:out value="${item.title} ${item.location}"/>
                                <a href="/deleteItem?itemId=${item.id}">x</a>
                                <c:set var="timespan" value="${item.timeSpan+1}"/>
                            </td>
                            <c:set var="foundItem" value="true"/>
                        </c:if>
                    </c:forEach>
                    <c:set var="timespan" value="${timespan-1}"/>
                    <c:if test="${foundItem eq false && timespan<=0}">
                        <td align="center" valign="middle" width="100">

                        </td>
                    </c:if>
                        <%-- ÞRIÐJUDAGUR --%>
                    <c:set var="foundItem2" value="false"/>
                    <c:forEach var="item" items="${scheduleItems}">
                        <c:if test="${item.startstring eq slot && item.weekDay eq 1}">
                            <td title="From: ${item.startstring}&#013;To: ${item.endstring}&#013;${item.description}"
                                align="center" valign="middle" width="100" rowspan="${item.timeSpan}" class="${item.color}">
                                <c:out value="${item.title} ${item.location}"/>
                                <a href="/deleteItem?itemId=${item.id}">x</a>
                                <c:set var="timespan2" value="${item.timeSpan+1}"/>
                            </td>
                            <c:set var="foundItem2" value="true"/>
                        </c:if>
                    </c:forEach>
                    <c:set var="timespan2" value="${timespan2-1}"/>
                    <c:if test="${foundItem2 eq false && timespan2<=0}">
                        <td align="center" valign="middle" width="100"></td>
                    </c:if>
                    <%-- MIÐVIKUDAGUR --%>
                    <c:set var="foundItem3" value="false"/>
                    <c:forEach var="item" items="${scheduleItems}">
                        <c:if test="${item.startstring eq slot && item.weekDay eq 2}">
                            <td title="From: ${item.startstring}&#013;To: ${item.endstring}&#013;${item.description}"
                                align="center" valign="middle" width="100" rowspan="${item.timeSpan}" class="${item.color}">
                                <c:out value="${item.title} ${item.location}"/>
                                <a href="/deleteItem?itemId=${item.id}">x</a>
                                <c:set var="timespan3" value="${item.timeSpan+1}"/>
                            </td>
                            <c:set var="foundItem3" value="true"/>
                        </c:if>
                    </c:forEach>
                    <c:set var="timespan3" value="${timespan3-1}"/>
                    <c:if test="${foundItem3 eq false && timespan3<=0}">
                        <td align="center" valign="middle" width="100"></td>
                    </c:if>
                    <%-- FIMMTUDAGUR --%>
                    <c:set var="foundItem4" value="false"/>
                    <c:forEach var="item" items="${scheduleItems}">
                        <c:if test="${item.startstring eq slot && item.weekDay eq 3}">
                            <td title="From: ${item.startstring}&#013;To: ${item.endstring}&#013;${item.description}"
                                align="center" valign="middle" width="100" rowspan="${item.timeSpan}" class="${item.color}">
                                <c:out value="${item.title} ${item.location}"/>
                                <a href="/deleteItem?itemId=${item.id}">x</a>
                                <c:set var="timespan4" value="${item.timeSpan+1}"/>
                            </td>
                            <c:set var="foundItem4" value="true"/>
                        </c:if>
                    </c:forEach>
                    <c:set var="timespan4" value="${timespan4-1}"/>
                    <c:if test="${foundItem4 eq false && timespan4<=0}">
                        <td align="center" valign="middle" width="100"></td>
                    </c:if>
                    <%-- FÖSTUDAGUR --%>
                    <c:set var="foundItem5" value="false"/>
                    <c:forEach var="item" items="${scheduleItems}">
                        <c:if test="${item.startstring eq slot && item.weekDay eq 4}">
                            <td title="From: ${item.startstring}&#013;To: ${item.endstring}&#013;${item.description}"
                                align="center" valign="middle" width="100" rowspan="${item.timeSpan}" class="${item.color}">
                                <c:out value="${item.title} ${item.location}"/>
                                <a href="/deleteItem?itemId=${item.id}">x</a>
                                <c:set var="timespan5" value="${item.timeSpan+1}"/>
                            </td>
                            <c:set var="foundItem5" value="true"/>
                        </c:if>
                    </c:forEach>
                    <c:set var="timespan5" value="${timespan5-1}"/>
                    <c:if test="${foundItem5 eq false && timespan5<=0}">
                        <td align="center" valign="middle" width="100"></td>
                    </c:if>
                    <%-- LAUGARDAGUR --%>
                    <c:set var="foundItem6" value="false"/>
                    <c:forEach var="item" items="${scheduleItems}">
                        <c:if test="${item.startstring eq slot && item.weekDay eq 5}">
                            <td title="From: ${item.startstring}&#013;To: ${item.endstring}&#013;${item.description}"
                                align="center" valign="middle" width="100" rowspan="${item.timeSpan}" class="${item.color}">
                                <c:out value="${item.title} ${item.location}"/>
                                <a href="/deleteItem?itemId=${item.id}">x</a>
                                <c:set var="timespan6" value="${item.timeSpan+1}"/>
                            </td>
                            <c:set var="foundItem6" value="true"/>
                        </c:if>
                    </c:forEach>
                    <c:set var="timespan6" value="${timespan6-1}"/>
                    <c:if test="${foundItem6 eq false && timespan6<=0}">
                        <td align="center" valign="middle" width="100"></td>
                    </c:if>
                    <%-- SUNNUDAGUR --%>
                    <c:set var="foundItem7" value="false"/>
                    <c:forEach var="item" items="${scheduleItems}">
                        <c:if test="${item.startstring eq slot && item.weekDay eq 6}">
                            <td title="From: ${item.startstring}&#013;To: ${item.endstring}&#013;${item.description}"
                                align="center" valign="middle" width="100" rowspan="${item.timeSpan}" class="${item.color}">
                                <c:out value="${item.title} ${item.location}"/>
                                <a href="/deleteItem?itemId=${item.id}">x</a>
                                <c:set var="timespan7" value="${item.timeSpan+1}"/>
                            </td>
                            <c:set var="foundItem7" value="true"/>
                        </c:if>
                    </c:forEach>
                    <c:set var="timespan7" value="${timespan7-1}"/>
                    <c:if test="${foundItem7 eq false && timespan7<=0}">
                        <td align="center" valign="middle" width="100"></td>
                    </c:if>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="lol col-md-2">

        <a class="btn btn-info" role="button" href="/compare">Compare your schedule with others!</a>

            <form class="form-group" action="/createGroup">
                <label colspan="3">Create a group here. Then go to <a href="/search">search</a> to add members to your group.</label>

                <label>Group name:</label>
                <input class="form-control" type="text" name="grpName" placeholder="Enter group name"/></td>
                <c:if test="${groupFail}">
                    <p class="alert alert-danger">A group with this name already exists. Please choose another!</p>
                </c:if>
                <button class="btn btn-primary" type="submit" value="Create Group!">Create group!</button>
            </form>

            <ul class="list-group">
                <li class="list-group-item list-group-item-info">Your friends:</li>
                <c:forEach var="friend" items="${friends}">
                    <li class="list-group-item"><c:out value="${friend.username}"/><a href="/deleteFriendship?friendId=${friend.userId}"> x</a></li>
                </c:forEach>
            </ul>

            <ul class="list-group">
                <li class="list-group-item list-group-item-info">Your groups:</li>
                <c:forEach var="group" items="${groups}">
                    <li class="list-group-item"><c:out value="${group.grpName}"/><a href="/deleteGroup?grpId=${group.grpId}"> x</a></li>
                </c:forEach>
            </ul>

    </div>

        </body>

</html>