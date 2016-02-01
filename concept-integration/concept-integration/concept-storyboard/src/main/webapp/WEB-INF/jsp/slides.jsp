<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <jsp:include page="/WEB-INF/jsp/headers.jsp"></jsp:include>
            <script type="text/javascript">
                $(document).ready(function () {

                });

                function fPreview(idSlide) {
                    $("#idSlide").val(idSlide);
                    $("#editSlideForm").attr("action", "preview");
                    $("#editSlideForm").attr("target", "_blank");
                    $("#editSlideForm").attr("pid", <%= request.getParameter("pid")%>);
                    $("#editSlideForm").attr("pid", <%= request.getParameter("uid")%>);
                    $("#editSlideForm").submit();
                }

                function fRemove(idSlide) {
                    if (confirm("Delete slide, do yo want continue?")) {
                        $("#idSlide").val(idSlide);
                        $("#editSlideForm").attr("target", "_self");
                        $("#editSlideForm").attr("action", "remove");
                        $("#editSlideForm").attr("pid", <%= request.getParameter("pid")%>);
                        $("#editSlideForm").attr("pid", <%= request.getParameter("uid")%>);
                        $("#editSlideForm").submit();
                    }
                }

                function fEdit(idSlide) {
                    $("#idSlide").val(idSlide);
                    $("#editSlideForm").attr("target", "_self");
                    $("#editSlideForm").attr("action", "edit");
                    $("#editSlideForm").attr("pid", <%= request.getParameter("pid")%>);
                    $("#editSlideForm").attr("pid", <%= request.getParameter("uid")%>);
                    $("#editSlideForm").submit();
                }
        </script>
    </head>
    <body>
        <jsp:include page="/WEB-INF/jsp/bar.jsp"></jsp:include>
            <form id="editSlideForm" method="post" action="">
                <input type="hidden" id="idSlide" name="idSlide" />
                <!-- COnCEPT ProjectId -->
                <input type="hidden" id="pid" name="pid" value="<%= request.getParameter("pid")%>"/>
            <!-- COnCEPT UserId -->
            <input type="hidden" id="uid" name="uid" value="<%= request.getParameter("uid")%>"/>
        </form>
        <div class="container">
            <h3>Scenes list</h3>
            <table class="table table-hover" style="width: 70%;">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="mySlide" items="${request_slides}">
                        <tr>
                            <td><c:out value="${mySlide.id}"></c:out></td>
                            <td><c:out value="${mySlide.slideName}"></c:out></td>
                                <td>
                                    <div class="row">
                                        <div class="col-sm-4">
                                            <button type="button" class="btn btn-primary btn-md btn-block" onclick="fEdit('<c:out value="${mySlide.id}"></c:out>');">
                                                <span class="glyphicon glyphicon-edit"></span> edit
                                            </button>
                                        </div>
                                        <div class="col-sm-4">
                                            <button type="button" class="btn btn-primary btn-md btn-block" onclick="fPreview('<c:out value="${mySlide.id}"></c:out>');">
                                                <span class="glyphicon glyphicon-eye-open"></span> preview
                                            </button>
                                        </div>
                                        <div class="col-sm-4">
                                        	&nbsp;
                                        	<!-- 
                                            <button type="button" class="btn btn-primary btn-md btn-block" onclick="fRemove('<c:out value="${mySlide.id}"></c:out>');">
                                                <span class="glyphicon glyphicon-remove-circle"></span> Remove
                                            </button>
                                             -->
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