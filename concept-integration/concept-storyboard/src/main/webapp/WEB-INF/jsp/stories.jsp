<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
    <head>
        <jsp:include page="/WEB-INF/jsp/headers.jsp"></jsp:include>
            <script type="text/javascript">
                $(document).ready(function () {
                    $("#preview0").click(function () {

                    });
                });

                function fPreview(idStory) {
                    $("#idStory").val(idStory);
                    $("#editStoryForm").attr("action", "preview");
                    $("#editStoryForm").attr("target", "_blank");
                    $("#editStoryForm").submit();
                }

                function fRemove(idStory) {
                    if (confirm("Delete story, do yo want continue?")) {
                        $("#idStory").val(idStory);
                        $("#editStoryForm").attr("target", "_self");
                        $("#editStoryForm").attr("action", "remove");
                        $("#editStoryForm").submit();
                    }
                }

                function fEdit(idStory) {
                    $("#idStory").val(idStory);
                    $("#editStoryForm").attr("target", "_self");
                    $("#editStoryForm").attr("action", "edit");
                    $("#editStoryForm").submit();
                }

            </script>
        </head>
        <body>
        <jsp:include page="/WEB-INF/jsp/bar.jsp"></jsp:include>
            <form id="editStoryForm" method="post" action="">
                <input type="hidden" id="idStory" name="idStory" />
                <!-- COnCEPT ProjectId -->
                <input type="hidden" id="pid" name="pid" value="<%= request.getParameter("pid")%>"/>
            <!-- COnCEPT UserId -->
            <input type="hidden" id="uid" name="uid" value="<%= request.getParameter("uid")%>"/>
        </form>	
        <div class="container">
            <h3>Storyboard list</h3>
            <br>
            <table class="table table-hover" style="width: 70%;">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="myStory" items="${request_stories}">
                        <tr>
                            <td> <c:out value="${myStory.id}"/> </td>
                            <td> <c:out value="${myStory.storyName}"/> </td>
                            <td>
                                <div class="row">
                                    <div class="col-sm-4">
                                        <button type="button" class="btn btn-primary btn-md btn-block" onclick="fEdit('<c:out value="${myStory.id}"></c:out>');">
                                                <span class="glyphicon glyphicon-edit"></span> edit
                                            </button>
                                        </div>
                                        <div class="col-sm-4">
                                            <button type="button" class="btn btn-primary btn-md btn-block" onclick="fPreview('<c:out value="${myStory.id}"></c:out>');">
                                                <span class="glyphicon glyphicon-eye-open"></span> preview
                                            </button>
                                        </div>
                                        <div class="col-sm-4">
                                            <button type="button" class="btn btn-primary btn-md btn-block" onclick="fRemove('<c:out value="${myStory.id}"></c:out>');">
                                                <span class="glyphicon glyphicon-remove-circle"></span> Remove
                                            </button>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>