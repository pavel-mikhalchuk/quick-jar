<%@ page import="com.prettybit.quickjar.DB" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quick Jar</title>
    <%--<script type='text/javascript'>--%>
    <%--var source = new EventSource('run');--%>

    <%--source.onmessage = function (event) {--%>
    <%--var console = document.getElementById('console');--%>
    <%--console.innerHTML += event.data + "<br>";--%>
    <%--};--%>

    <%--source.addEventListener("end", function () {--%>
    <%--source.close();--%>
    <%--});--%>
    <%--</script>--%>
</head>
<body>
<div style="text-align: center;">
    <h1>Build A Jar Quickly</h1>

    <form name="new-jar" action="rest/jar" method="POST" style="width: 35%; float: left;">
        <div><input name="pkg" type="text" value="com.prettybit.quickjar"/></div>

        <div><input name="class" type="text" value="Test"/></div>

        <textarea name="code" rows="20" cols="40">
            package com.prettybit.quickjar;

            /**
            * @author Pavel Mikhalchuk
            */
            public class Test {

            public static void main(String[] args) {
            System.out.println("First Quick Jar");
            }

            }
        </textarea>

        <div><input name="code" type="submit" value="Build!"/></div>
    </form>

    <div id="jar-list" style="width: 35%; float: left;">
        <c:choose>
            <c:when test="<%=!DB.list().isEmpty()%>">
                <h3>Your Jars</h3>
                <c:forEach items="<%=DB.list()%>" var="jar">
                    <div>${jar.name}</div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                NO JARS
            </c:otherwise>
        </c:choose>
    </div>

    <div id="console" style="width: 30%; float: left;">
        <h3>Running Jar</h3>
    </div>
</div>
</body>
</html>