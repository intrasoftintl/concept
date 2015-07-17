//
//Static variables
//
var MEMBERSHIPS_REST_URL = "http://localhost:8080/concept/rest/memberships/";
//

//After html is rendered do the following...
$(document).ready(function () {
    //Trigger event when project list is changed
    $('#projectSelect').change(function () {
        var projectID = $(this).val();
        projectSelectedAction(projectID);
    });

    //If not dashboard page, select current project
    if (!isDashboardPage()) {
        console.log("Selecting current project with id: " + $('#projectID').val());
        $('#projectSelect').val($('#projectID').val()).change();
    }

    if (isUploadPage()) {
        //Initialize file-upload form
        $('#fileupload').fileupload({
            dataType: 'json',
            done: function (e, data) {
                $.each(data.result, function (index, result) {
                    $('.files').append('- ' + result.fileName + '<br/>');
                    $('.files').css("display", "block");
                });
            }
        });
    }
});


//
// Public API 
//

function projectSelectedAction(projectID) {
    if (projectID > 0) {
        $.ajax({
            url: MEMBERSHIPS_REST_URL + projectID
        }).then(function (data) {
            var fullNames = "";
            for (var i in data)
            {
                fullNames += data[i].user.firstname + " " + data[i].user.lastname + ", ";
            }
            $("#projectMembers").empty();
            $("#projectMembers").html("Project Members<br/><small>" + fullNames.substring(0, fullNames.length - 2) + " </small>");
            $("#projectView").attr("href", "http://192.168.3.5/projects/" + projectID);
        });
        //Trigger only if current page isDashboard
        if (isDashboardPage()) {
            $("#fmBut1").attr("href", "/fm_app?projectID=" + projectID);
            //Enable DashboardPage
            enableDashboardPage();
            //Load Dashboard content
            loadDashboardContent(projectID);
        } else {
            $('#projectID').val(projectID);
        }

    } else if (isDashboardPage()) {
        disableDashboardPage();
    }
}



//Return true if current page is dashboard
function isDashboardPage() {
    return location.pathname === "/dashboard";
}


//Return true if current page is dashboard
function isUploadPage() {
    return location.pathname === "/fm_app";
}


//Sample post using jQuery
//        
//        $.ajax({
//            url: "/dashboard",
//            type: 'POST',
//            data: {
//                "projectID": projectID,
//                "_csrf": $("#_csrf").val()
//            },
//            success: function (data) {
//            }
//        });
