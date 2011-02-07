<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"      uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt"    uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
    <title>some foo</title>
    <body>
        <table>
        <!--c:forEach var="foo" items="${foos}"-->
            <tr>
                <td>${foo.id}</td>
                <td>${foo.value}</td>
            </tr>
        </table>
        <!--/c:forEach-->

        </body>
</html>
