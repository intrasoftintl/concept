<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <jsp:include page="/WEB-INF/jsp/headers.jsp"></jsp:include>
        <script src="${pageContext.request.contextPath}/ckeditor/ckeditor.js"></script>
        <script src="${pageContext.request.contextPath}/js/common.js"></script>
        <script type="text/javascript">

            function fCheckEmpty(myInput) {
                if ($(myInput).val()) {
                    $(myInput).parent().removeClass('has-error has-feedback');
                }
            }

            $(document).ready(function () {
                $("#slide_name").parent().find('.glyphicon-remove').hide();
                $("#idPreviewButton").click(function () {
                    $("#newSlideForm").attr("action", "preview");
                    $("#newSlideForm").attr("target", "_blank");
                    $("#newSlideForm").submit();
                });

                $("#idSaveButton").click(function () {
                    if (!$("#slide_name").val()) {
                        $("#slide_name").parent().addClass('has-error has-feedback');
                    } else {
                        var myVal = document.getElementById("idIframe").contentWindow.svgcontent.innerHTML;
                        myVal = '<svg width="640" height="480" xmlns="http://www.w3.org/2000/svg">' + myVal + '</svg>';
                        $("#content").val(myVal);
                        $("#newSlideForm").attr("action", "save");
                        $("#newSlideForm").submit();
                    }
                });
            <c:if test="${requestScope.request_slide != null}">
                $("#slide_name").val('<c:out value="${requestScope.request_slide.slideName}"/>');
                $("#idSlide").val('<c:out value="${requestScope.request_slide.id}"/>');
            </c:if>
            });
            function initEditor(myEditor) {
                var myNewDraw = '<svg width="640" height="480" xmlns="http://www.w3.org/2000/svg"></svg>';
            <c:if test="${requestScope.request_slide != null}">
                myNewDraw = '${requestScope.request_slide.slideText}';
                console.log(myNewDraw);
            </c:if>
                myEditor.loadFromString(myNewDraw);
            }
        </script>
    </head>
    <body>
        <jsp:include page="/WEB-INF/jsp/bar.jsp"></jsp:include>
            <div class="container">
                <div>
                    <div class="row">
                        <div class="col-sm-5">
                            <h3>
                            <c:if test="${requestScope.is_update != null}">
                                <fmt:message key="newslide.title.update" />
                            </c:if>
                            <c:if test="${requestScope.is_update == null}">
                                <fmt:message key="newslide.title.new" />
                            </c:if>
                        </h3>
                    </div>
                    <div class="col-sm-5" style="margin-top: 20px; margin-bottom: 10px;">
                        <form role="form" id="newSlideForm" method="post">
                            <input type="hidden" id="idSlide" name="idSlide"/>
                            <input type="hidden" id="content" name="content"/>
                            <!-- COnCEPT ProjectId -->
                            <input type="hidden" id="pid" name="pid" value="<%= request.getParameter("pid")%>"/>
                            <!-- COnCEPT UserId -->
                            <input type="hidden" id="uid" name="uid" value="<%= request.getParameter("uid")%>"/>
                            <div class="form-inline">
                                <label for="slidename">Name&nbsp;&nbsp;</label>
                                <input class="form-control" id="slide_name" name="slide_name" placeholder="Enter moodname name" onchange="fCheckEmpty(this);">
                            </div>
                        </form>
                    </div>
                    <div class="col-sm-2" style="margin-top: 20px; margin-bottom: 10px;">
                        <button id="idSaveButton" type="button" class="btn btn-primary btn-md btn-block">
                            <span class="glyphicon glyphicon-floppy-disk"></span> Save
                        </button>
                    </div>
                </div>
                <div>
                    <iframe src="/storyboard/svg-edit-2.8.1/svg-editor.html" id="idIframe" width="100%" height="600"></iframe>
                </div>
                <c:if test="${requestScope.request_new_slide_error != null}">
                    <div class="alert alert-danger" id="divError"><c:out value="${requestScope.request_new_slide_error}"></c:out></div>
                </c:if>
            </div>
        </div>
    </body>
</html>
