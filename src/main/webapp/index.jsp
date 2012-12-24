<%@ page import="com.prettybit.quickjar.DB" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quick Jar</title>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.8.3.js"></script>
    <script type='text/javascript'>
        function makeJar(pkg, className, code) {
            document.getElementById('make').disabled = true;

            document.getElementById('console').innerHTML = '';

            var source = new EventSource('jar/console');

            source.onmessage = function (message) {
                document.getElementById('console').innerHTML += message.data;
            };

            source.addEventListener("start", function () {
                document.getElementById('console').innerHTML += 'Open';
                $.post('jar/make/', { pkg: pkg, class: className, code: code });
            });

            source.addEventListener("end", function () {
                document.getElementById('console').innerHTML += 'Close';
                source.close();
                document.getElementById('make').disabled = false;
            });
        }

        function runJar(name) {
            document.getElementById('run').disabled = true;

            document.getElementById('console').innerHTML = '';

            var source = new EventSource('jar/console');

            source.onmessage = function (message) {
                document.getElementById('console').innerHTML += message.data;
            };

            source.addEventListener("start", function () {
                document.getElementById('console').innerHTML += 'Open';
                $.get('jar/run/' + name);
            });

            source.addEventListener("end", function () {
                document.getElementById('console').innerHTML += 'Close';
                source.close();
                document.getElementById('run').disabled = false;
            });
        }
    </script>
</head>
<body>
<div style="text-align: center;">
    <h1>Build A Jar Quickly</h1>

    <div style="width: 35%; float: left;">
        <div><input id="pkg" type="text" value="com.prettybit.quickjar"/></div>

        <div><input id="class" type="text" value="Test"/></div>

        <textarea id="code" rows="20" cols="40">
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

        <div><input type="button" id="make" value="Make"
                    onclick="makeJar(document.getElementById('pkg').value, document.getElementById('class').value, document.getElementById('code').value)"/>
        </div>
    </div>

    <div style="width: 30%; float: left;">
        <c:choose>
            <c:when test="<%=!DB.list().isEmpty()%>">
                <h3>Your Jars</h3>
                <table border="1" style="width: 100%;">
                    <c:forEach items="<%=DB.list()%>" var="jar">
                        <tr>
                            <td>${jar.name}</td>
                            <td>
                                <form action="jar/delete/${jar.name}" method="GET">
                                    <input type="submit" value="X"/>
                                </form>
                                <input type="button" id="run" value="R" onclick="runJar('${jar.name}')"/>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                NO JARS
            </c:otherwise>
        </c:choose>
    </div>

    <div style="width: 35%; float: left; text-align: center">
        <h3>Console</h3>
        <textarea id="console" rows="20" cols="40"></textarea>
    </div>
</div>
</body>
</html>