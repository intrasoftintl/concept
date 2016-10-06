<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="language" value="${not empty sessionScope.session_user.language ? sessionScope.session_user.language : pageContext.request.locale}" scope="session" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <jsp:include page="/WEB-INF/jsp/headers.jsp"></jsp:include>
            <script src="http://d3js.org/d3.v3.min.js"></script>
            <script type="text/javascript">

                var MAX_SLIDES = 100;

                $(document).ready(function () {

                    $("#divError").hide();
                    $("#story_name").parent().find('.glyphicon-remove').hide();
                    $("#idNewStoryForm").submit(function (event) {
                        var errorText = "";
                        var error_name = false;
                        if (!$("#story_name").val()) {
                            $("#story_name").parent().addClass('has-error has-feedback');
                            $("#story_name").parent().find('.glyphicon-remove').show();
                            error_name = true;
                        }
                        var error_disorder = false;
                        var finished = false;
                        $("#slidesList").val("");
                        $("div[name*='nameDiv']").each(function (i) {
                            if ($(this).find('img').size() == 1) {
                                if (finished == true) {
                                    error_disorder = true;
                                } else {
                                    var myId = $(this).find('img').attr('id');
                                    if ($("#slidesList").val() != "") {
                                        $("#slidesList").val($("#slidesList").val() + "|");
                                    }
                                    $("#slidesList").val($("#slidesList").val() + myId);
                                }
                            } else {
                                finished = true;
                            }
                        });
                        if (error_name) {
                            errorText += "Error: empty name.";
                        }
                        if (error_disorder) {
                            if (errorText != "") {
                                errorText += "<br>";
                            }
                            errorText += "Error: story with holes.";
                        } else {
                            if ($("#slidesList").val() == "") {
                                if (errorText != "") {
                                    errorText += "<br>";
                                }
                                errorText += "Error: story empty.";
                            }
                        }
                        $("#divError").text("");
                        var valid = true;
                        if (errorText != "") {
                            valid = false;
                            $("#divError").show();
                            $("#divError").append(errorText);
                        }
                        return valid;
                    });

                    $("#idSaveButton").click(function () {
                        $("#idNewStoryForm").attr("action", "save");
                        $("#idNewStoryForm").submit();
                    });
                    
                    $("#idPreviewButton").click(function () {
                        $("#idNewStoryForm").attr("action", "previewNew");
                        $("#idNewStoryForm").attr("target", "_blank");
                        $("#idNewStoryForm").submit();
                    });

            <c:if test="${requestScope.request_story != null}">
                    $("#idStory").val('<c:out value="${requestScope.request_story.id}"/>');
                    $("#story_name").val('<c:out value="${requestScope.request_story.storyName}"/>');
                <c:forEach var="mySlide" items="${requestScope.slidesListOrdered}" varStatus="count">
                    $("#idDiv${count.index}").append($("#${mySlide.id}"));
                </c:forEach>
            </c:if>

                });

                function fCheckEmpty(myInput) {
                    if ($(myInput).val()) {
                        $(myInput).parent().removeClass('has-error has-feedback');
                        $(myInput).parent().find('.glyphicon-remove').hide();
                    }
                }

                function allowDrop(ev) {
                    if (ev.target.id.match(/idDiv/) != null) {
                        ev.preventDefault();
                    } else {
                        return false;
                    }
                }

                function drag(ev) {
                    ev.dataTransfer.setData("text", ev.target.id);
                }

                function drop(ev) {
                    ev.preventDefault();
                    var data = ev.dataTransfer.getData("text");
                    ev.target.appendChild(document.getElementById(data));
                }

        </script>
    </head>
    <body style="height: 100%;">
        <%-- <jsp:include page="/WEB-INF/jsp/bar.jsp"></jsp:include> --%>
            <div class="container" style="margin-top: 30px;">
                <div>
                    <h3>
                    <c:if test="${requestScope.is_update != null}">
                        Edit Storyboard
                    </c:if>
                    <c:if test="${requestScope.is_update == null}">
                        New Storyboard
                    </c:if>
                </h3>
                <form role="form" id="idNewStoryForm" method="post">
                    <input type="hidden" id="idStory" name="idStory"/>
                    <input type="hidden" id="slidesList" name="slidesList"/>
                    <!-- COnCEPT ProjectId -->
                    <input type="hidden" id="pid" name="pid" value="<%= request.getParameter("pid")%>"/>
                    <!-- COnCEPT UserId -->
                    <input type="hidden" id="uid" name="uid" value="<%= request.getParameter("uid")%>"/>

                    <div class="form-group">
                        <label for="story_name">Name</label>
                        <input class="form-control" id="story_name" name="story_name" placeholder="Enter Storyboard name" onchange="fCheckEmpty(this);">
                        <span class="glyphicon glyphicon-remove form-control-feedback"></span>
                    </div>
                    <div id="slidesList" style="height: 290px; overflow: auto; overflow-x: hidden; margin-bottom: 20px;">
                        <div class="col-sm-6 verticalLine_right" id="slidesSelectedListDiv" style="height: 100%; overflow:auto; width: 50%; display:inline-block; ">
                            <label for="story_name">Moodboards selected</label><br>
                            <c:forEach var="i" begin="0" end="99" varStatus="count">
                                <div class="div1">
                                    Scene ${count.index + 1}
                                    <div name="nameDiv" id="idDiv${count.index}" ondrop="drop(event)" ondragover="allowDrop(event)" style="width: 100%; height: 80%">

                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                        <div class="col-sm-6 verticalLine_left" id="slidesListDiv" style="height: 100%; overflow:auto; width: 50%; display:inline-block;">
                            <label for="story_name">Moodboards available</label><br>
                            <c:forEach var="mySlide" items="${request_slides}" varStatus="count">
                                <div class="div2" id="idDivSource${count.index}" style="display:inline-block;" ondrop="drop(event)" ondragover="allowDrop(event)">
                                    <img width="140" id="${mySlide.id}" draggable="true" ondragstart="drag(event)" >
                                    <script type="text/javascript">
                                        var svgContent = '${mySlide.slideText}';
                                        var imgSrc = 'data:image/svg+xml;base64,' + btoa(svgContent);
                                        $("#${mySlide.id}").attr('src', imgSrc);
                                    </script>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-8">
                            <div class="alert alert-danger" id="divError"></div>
                        </div>
                        <div class="col-sm-2">
                            <button id="idPreviewButton" type="button" class="btn btn-primary btn-md btn-block">
                                <span class="glyphicon glyphicon-eye-open"></span> Preview
                            </button>
                        </div>
                        <div class="col-sm-2">                        
                            <button id="idSaveButton" type="button" class="btn btn-danger btn-md btn-block">
                                <span class="glyphicon glyphicon-floppy-disk"></span> Save
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>