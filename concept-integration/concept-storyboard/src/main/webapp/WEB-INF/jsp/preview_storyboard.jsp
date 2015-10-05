<!doctype html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html lang="en">
	<head>
		<jsp:include page="/WEB-INF/jsp/headers.jsp"></jsp:include>
		<meta charset="utf-8">
		<title>reveal.js - The HTML Presentation Framework</title>

		<meta name="description" content="A framework for easily creating beautiful presentations using HTML">
		<meta name="author" content="Hakim El Hattab">

		<meta name="apple-mobile-web-app-capable" content="yes" />
		<meta name="apple-mobile-web-app-status-bar-style" content="black-translucent" />

		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no, minimal-ui">

		<link rel="stylesheet" href="../reveal/css/reveal.css">
		<link rel="stylesheet" href="../reveal/css/theme/black.css" id="theme">

		<!-- Code syntax highlighting -->
		<link rel="stylesheet" href="../reveal/lib/css/zenburn.css">

		<!-- Printing and PDF exports -->
		<script>
			var link = document.createElement( 'link' );
			link.rel = 'stylesheet';
			link.type = 'text/css';
			link.href = window.location.search.match( /print-pdf/gi ) ? '../reveal/css/print/pdf.css' : '../reveal/css/print/paper.css';
			document.getElementsByTagName( 'head' )[0].appendChild( link );
		</script>

		<!--[if lt IE 9]>
		<script src="lib/js/html5shiv.js"></script>
		<![endif]-->
	</head>

	<body>

		<div class="reveal">

			<!-- Any section element inside of this container is displayed as a slide -->
			<div class="slides">
				<c:forEach var="mySlide" items="${request_slides}">
					<section>
						<div style="background: white;">
							${mySlide.slideText}
						</div>
					</section>
				</c:forEach>
			</div>
		</div>

		<script src="../reveal/lib/js/head.min.js"></script>
		<script src="../reveal/js/reveal.js"></script>

		<script>

			// Full list of configuration options available at:
			// https://github.com/hakimel/reveal.js#configuration
			Reveal.initialize({
				controls: true,
				progress: true,
				history: true,
				center: true,

				transition: 'slide', // none/fade/slide/convex/concave/zoom

				// Optional reveal.js plugins
				dependencies: [
					{ src: '../reveal/lib/js/classList.js', condition: function() { return !document.body.classList; } },
					{ src: '../reveal/plugin/markdown/marked.js', condition: function() { return !!document.querySelector( '[data-markdown]' ); } },
					{ src: '../reveal/plugin/markdown/markdown.js', condition: function() { return !!document.querySelector( '[data-markdown]' ); } },
					{ src: '../reveal/plugin/highlight/highlight.js', async: true, condition: function() { return !!document.querySelector( 'pre code' ); }, callback: function() { hljs.initHighlightingOnLoad(); } },
					{ src: '../reveal/plugin/zoom-js/zoom.js', async: true },
					{ src: '../reveal/plugin/notes/notes.js', async: true }
				]
			});

		</script>

	</body>
</html>
