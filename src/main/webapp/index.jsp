<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quick Jar</title>
    <link rel="stylesheet" type="text/css" href="flexigrid-1.1/css/flexigrid.css"/>
    <script type="text/javascript" src="jquery-1.8.3/js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="flexigrid-1.1/js/flexigrid.js"></script>
    <script type='text/javascript'>
        function Console() {

            var console = $('#console');

            this.clear = function () {
                console.html('');
            };

            this.append = function (string) {
                console.append(string);
                console.scrollTop(console.prop('scrollHeight'));
            };

            this.clear();

        }

        function BSE() {

            var console = new Console();

            var start;
            var end;

            this.run = function (button) {
                button.disabled = true;

                var source = new EventSource('jar/console');

                source.onmessage = function (message) {
                    console.append(message.data);
                };

                source.addEventListener("start", start);

                source.addEventListener("end", function () {
                    source.close();
                    end();
                    button.disabled = false;
                });
            };

        }

        function makeJar(pkg, className, code, button) {
            var bse = new BSE();
            bse.start = function () {
                $.post('jar/make/', { pkg: pkg, class: className, code: code });
            };
            bse.end = reFetchJars;

            bse.run(button);
        }

        function deleteJar(name, button) {
            button.disabled = true;
            $.get('jar/delete/' + name, function () {
                reFetchJars();
                button.disabled = false;
            });
        }

        function runJar(name, button) {
            var bse = new BSE();
            bse.start = function () {
                $.get('jar/run/' + name)
            };

            bse.run(button);
        }

        function reFetchJars() {
            $('#jar-list').flexReload();
        }

        //        function run(button, start, end) {
        //            button.disabled = true;
        //
        //            console.html('');
        //
        //            var source = new EventSource('jar/console');
        //
        //            source.onmessage = function (message) {
        //                $('#console').append(message.data);
        //                $('#console').scrollTop($('#console').prop('scrollHeight'));
        //            };
        //
        //            source.addEventListener("start", function () {
        //                start();
        //            });
        //
        //            source.addEventListener("end", function () {
        //                source.close();
        //                end();
        //                button.disabled = false;
        //            });
        //        }
    </script>
</head>
<body>
<div style="text-align: center;">
    <h1>Build A Jar Quickly</h1>

    <div style="width: 35%; float: left;">
        <h3>New Jar</h3>
        <textarea id="code" style="width: 330px; height: 295px;">
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

        <div>
            <input type="button" value="Make"
                   onclick="makeJar('com.prettybit.quickjar', 'Test', $('#code').val(), this)"/>
        </div>
    </div>

    <div style="width: 30%; float: left;">
        <h3>Your Jars</h3>

        <table id="jar-list"></table>
        <script type="text/javascript">
            $('#jar-list').flexigrid({
                url: 'jar/list',
                dataType: 'json',
                colModel: [
                    {display: 'Name', width: 218},
                    {display: 'Modified Date', width: 73, align: 'center'},
                    {display: 'Action', width: 71, align: 'center'}
                ],
                title: 'Jars',
                width: 400,
                height: 241
            });
        </script>
    </div>

    <div style="width: 35%; float: left; text-align: center;">
        <h3>Console</h3>
        <textarea id="console" style="font-size: 6px; width: 330px; height: 295px;">2323</textarea>
    </div>
</div>
</body>
</html>