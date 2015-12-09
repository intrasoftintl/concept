<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/WEB-INF/jsp/headers.jsp"></jsp:include>
<style type="text/css">
.error-template {
	padding: 40px 15px;
	text-align: center;
}

.error-actions {
	margin-top: 15px;
	margin-bottom: 15px;
}

.error-actions .btn {
	margin-right: 10px;
}
</style>
<script type="text/javascript">
	$(document).ready(function(){
		$("#idShowTrace").click(function(){			
			//$("#idMyDiv").show();
			$("#idMyDiv").toggle();
			
		});
	});
</script>

</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="error-template">
					<h1>Oops!</h1>
					<h2>Sorry, something wrong happened :(</h2>
					<div class="alert alert-danger">
						<h4>
							${pageContext.exception}
							<button id="idShowTrace" type="button" class="btn btn-danger btn-md">
								Show error trace
							</button>
						 </h4>
						<div id="idMyDiv" style="display: none ;">
							<c:forEach var="trace" items="${pageContext.exception.stackTrace}">
								${trace}<br/>
							</c:forEach> 
						</div>
					</div>
					<br>
					<div class="error-actions">
						<a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg"> <span class="glyphicon glyphicon-home"></span> Take
							Me Home
						</a>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>