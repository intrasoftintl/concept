<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="language" value="${not empty sessionScope.session_user.language ? sessionScope.session_user.language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" scope="session"/>
<fmt:setBundle basename="text" scope="session" />	
<style>
<!--
.flag {
	width: 32px;
	height: 32px;
	background: url(./images/flags.png) no-repeat
}

.flag.flag-es {
	background-position: -32px 0
}

.flag.flag-gb {
	background-position: 0 -32px
}

.flag.flag-gr {
	background-position: -32px -32px
}
-->
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$(".nav li.disabled a").click(function() {
	    	return false;
		});
		var myLanguage = '<c:out value="${sessionScope.session_user.language}"/>';
		$("#" + myLanguage).parent().addClass("active");
		   
	});

</script>
<nav class="navbar navbar-default" style="margin-bottom: 0px;">
	<div class="container-fluid">
		<div class="navbar-header">
			<div class="navbar-brand">COnCEPT</div>
		</div>
		<div>
			<ul class="nav navbar-nav">
				<li id="storyboard" class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
						<fmt:message key="bar.storyboard"/>
						<span class="caret"></span>
				</a>
					<ul class="dropdown-menu" role="menu">
						<li><a href="${pageContext.request.contextPath}/scene/new?pid=<%=request.getParameter("pid")%>&uid=<%=request.getParameter("uid")%>"><fmt:message key="bar.new_slide"/></a></li>
						<li><a href="${pageContext.request.contextPath}/scene/view?pid=<%=request.getParameter("pid")%>&uid=<%=request.getParameter("uid")%>"><fmt:message key="bar.slides"/></a></li>
						<li><a href="${pageContext.request.contextPath}/storyboard/new?pid=<%=request.getParameter("pid")%>&uid=<%=request.getParameter("uid")%>"><fmt:message key="bar.new_storyboard"/></a></li>
						<li><a href="${pageContext.request.contextPath}/storyboard/view?pid=<%=request.getParameter("pid")%>&uid=<%=request.getParameter("uid")%>"><fmt:message key="bar.storyboards"/></a></li>
					</ul></li>
				<!--  <li id="moodboard"><a href="doMoodboard">Moodboard</a></li> -->
			</ul>
		</div>
	</div>
</nav>