<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Build A Quick Jar</title>
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
                console.append(string + '\n');
                console.scrollTop(console.prop('scrollHeight'));
            };

            this.clear();

        }

        function BSE() {

            var console = new Console();

            this.control = undefined;
            this.start = function () {
            };
            this.end = function () {
            };

            BSE.prototype.run = function () {
                this.disableControl();

                var source = new EventSource('jar/console');

                source.onmessage = function (message) {
                    console.append(message.data);
                };

                source.addEventListener("start", (function (bse) {
                    function start() {
                        bse.start();
                    }

                    return start;
                })(this));

                source.addEventListener("end", (function (bse) {
                    function end() {
                        source.close();
                        bse.end();
                        bse.enableControl();
                    }

                    return end;
                })(this));
            };

            this.enableControl = function () {
                if (this.control) this.control.disabled = false;
            };

            this.disableControl = function () {
                if (this.control) this.control.disabled = true;
            }

        }

        function makeJar(button) {
            var bse = new BSE();
            bse.control = button;
            bse.start = function () {
                $.post('jar/make/', { code: $('#code').val() });
            };
            bse.end = reFetchJars;

            bse.run();
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
            bse.control = button;
            bse.start = function () {
                $.get('jar/run/' + name)
            };

            bse.run();
        }

        function reFetchJars() {
            $('#jar-list').flexReload();
        }

    </script>
</head>
<body>
<div style="text-align: center;">
    <div style="width: 30%; float: left;">
        <h3>Your Jars</h3>

        <table id="jar-list"></table>
        <script type="text/javascript">
            $('#jar-list').flexigrid({
                url: 'jar/table',
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

    <div style="width: 70%; float: left;">
        <div>
            <h3>New Jar</h3>
            <textarea id="code" style="width: 900px; height: 250px;">
                package com.prettybit.quickjar.test;

                /**
                * @author Pavel Mikhalchuk
                */
                public class PAPAPAP {

                public static void main(String[] args) {
                System.out.println("Loading...");
                System.out.println("First Quick Jar");
                System.out.println("Done!");
                }

                }
            </textarea>

            <div>
                <input type="button" value="Make"
                       onclick="makeJar(this)"/>
            </div>
        </div>

        <div>
            <h3>Console</h3>
            <textarea id="console" style="font-size: 10px; width: 900px; height: 200px;"></textarea>
        </div>
    </div>
</div>
</body>
</html>