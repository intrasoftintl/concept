//After html is rendered do the following...
$(document).ready(function () {
    //alert("I am loaded!");
    //On page load disable dashboard content
    disableDashboardPage();
    //Trigger event when project list is changed
    $('#projectSelect').change(function () {
        var projectID = $(this).val();
        if (projectID > 0) {
            enableDashboardPage();
            $.ajax({
                url: "http://localhost:8080/concept/rest/memberships/" + projectID
            }).then(function (data) {
                var fullNames = "";
                for (var i in data)
                {
                    fullNames += data[i].user.firstname + " " + data[i].user.lastname + ", ";
                }
                $("#projectMembers").text("");
                $("#projectMembers").html("");
                $("#projectMembers").html("Project Members<br/><small>" + fullNames.substring(0, fullNames.length - 2) + " </small>");
            });
            $("#fmBut1").attr("href", "/fm_app?projectID=" + projectID)
            //Load Dashboard content
            loadDashboardContent(projectID);

        } else {
            disableDashboardPage();
        }
    });
});    