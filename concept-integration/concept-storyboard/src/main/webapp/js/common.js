/** common.js --> javascript functions used along application. */

/** Select element on the navigator bar. */
function selectMenuElement(idElement) {
	$(".nav").find(".active").removeClass("active");
	$("#" + idElement).addClass("active");
}


function escapeHtml(text) {
	return text
    .replace(/&/g, "&amp;")
    .replace(/</g, "&lt;")
    .replace(/>/g, "&gt;")
    .replace(/"/g, "&quot;")
    .replace(/'/g, "&#039;");
}

function unEscapeHtml(text) {
	return text
    .replace("&amp;", "&")
    .replace("&lt;", "<")
    .replace("&gt;", ">")
    .replace("&quot;", "\"")
    .replace("&#039;", "'");
}