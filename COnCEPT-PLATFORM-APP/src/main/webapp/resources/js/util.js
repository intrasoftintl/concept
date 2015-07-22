//Static variables
var DEBUG_MODE = true;
var DEBUG_PROMPT = "[DEBUG] ";
var MEMBERSHIPS_REST_URL = "http://localhost:8080/concept/rest/memberships/";


//COnCEPT Logger
function logger(message) {
    if (DEBUG_MODE) {
        console.log(DEBUG_PROMPT + message);
    }
}

//After html is rendered do the following...
$(document).ready(function () {

    //Trigger event when project list is changed
    $('#project-select').change(function () {
        var projectID = $(this).val();
        projectSelectedAction(projectID);
    });

    //If not dashboard page, select current project
    if (!isDashboardPage()) {
        console.log("Selecting current project with id: " + $('#projectID').val());
        $('#project-select').val($('#projectID').val()).change();
    }

    if (isFM_app()) {
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
            $("#project-members").empty();
            $("#project-members").html("Project Members<br/><small>" + fullNames.substring(0, fullNames.length - 2) + " </small>");
            $("#project-view").attr("href", "http://192.168.3.5/projects/" + projectID);
        });
        //Trigger only if current page isDashboard
        if (isDashboardPage()) {
            $("#projectID").val(projectID);
            //Enable DashboardPage
            enableDashboardPage();
            //Load Dashboard content
            loadDashboardContent(projectID);
        } else {
            $('#projectID').val(projectID);
        }

        if (isFM_app()) {
            $(".panel-body").show();
            $(".panel-footer").show();
            $("#sort").show();
            //$("#fm-all").removeClass("disabled");

            $("#fm-placeholder").hide();
            $("#project-members").show();
            $("#project-view").show();
        }

        if (isFM_all()) {
            $("#sort").show();

            $("#fm-add").attr("href", "/fm_app?projectID=" + projectID);
            $("#fm-add").show();
            $("#fm-all").load("/filemanagement_all/" + projectID + "?limit=200");


            $("#fm-placeholder").hide();

            $("#project-members").show();
            $("#project-view").show();
        }

        if (isBA_app()) {
            $("#project-members").show();
            $("#project-view").show();
        }

    } else if (isDashboardPage()) {
        disableDashboardPage();
        $("#ba-app-link").removeAttr(href);
        $("#fm-app-link").removeAttr(href);
        $("#se-app-link").removeAttr(href);
        $("#mm-app-link").removeAttr(href);
        $("#sb-app-link").removeAttr(href);
        $("#sk-app-link").removeAttr(href);

    } else if (isFM_app()) {
        $(".panel-body").hide();
        $("#fm-placeholder").show();
        $("#fm-all").addClass("disabled");
        $(".panel-footer").hide();
        $("#project-members").hide();
        $("#project-view").hide();
    } else if (isFM_all()) {
        $(".panel-body").hide();
        $("#sort").hide();
        $("#fm-placeholder").show();
        $(".panel-footer").hide();
        $("#project-members").hide();
        $("#project-view").hide();
        $("#fm-add").hide();
    } else if (isBA_app()) {
        $("#project-members").hide();
        $("#project-view").hide();
    }

}
//Return true if current page is DASHBOARD
function isDashboardPage() {
    return location.pathname === "/dashboard";
}

//Return true if current page is BA APP
function isBA_app() {
    return location.pathname === "/ba_app";
}

//Return true if current page is BA ALL
function isBA_all() {
    return location.pathname === "/ba_all";
}

//Return true if current page is FM APP
function isFM_app() {
    return location.pathname === "/fm_app";
}

//Return true if current page is FM ALL
function isFM_all() {
    return location.pathname === "/fm_all";
}

function saveBAContent() {
    console.log(tinymce.activeEditor.getContent({format: 'raw'}));
    console.log($('#document-title').val());
}

function loadBAContent() {
    tinymce.activeEditor.setContent('<h2>Que onda?</h2><p>Chabon!</p>', {format: 'raw'});
    $('#document-title').val("Document 1");
}

function loadMD() {
    $(".nav-main").hide();
    $(".nav-apps").hide();
    $(".project-selection").hide();
    $(".nav-chat").hide();
    $(".nav-chat-hidden").hide();
    $(".nav-apps").hide();

    $(".nav-main-md").show();
    $(".nav-keywords").show();
    $(".nav-categories").show();
    $(".nav-actions-md").show();
}


//Sample post using jQuery
function deleteWidgetItem(URL) {
    console.log("CRF is: " + $("#_csrf").val());
    console.log("URL is: " + 'http://localhost:8080/' + URL)

    $.ajax({
        url: 'http://localhost:8080/ba_app/2',
        type: 'DELETE',
        data: {
//            "projectID": projectID,
            "_csrf": $("#_csrf").val()
        },
        success: function (data) {
            alert("isSuccess...");
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(xhr.status);
            alert(thrownError);
        }
    });


    function loadMetadata(metadataID) {
        logger("Loading metada for ID: " + metadataID);
        $("#metadata-area").load("/metadata/" + metadataID);
    }


}

