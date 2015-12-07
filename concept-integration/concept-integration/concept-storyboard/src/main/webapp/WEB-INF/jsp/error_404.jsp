<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
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
<jsp:include page="/WEB-INF/jsp/headers.jsp"></jsp:include>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="error-template">
					<h1>Oops 404!</h1>
					<h2>Sorry, something wrong happened :(</h2>
					<div class="alert alert-danger">
					<h4>Resource not found: ${pageContext.errorData.requestURI}</h4>
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