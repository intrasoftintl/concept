//Static variables
var DEBUG_MODE = true;
var DEBUG_PROMPT = "[DEBUG] ";
var MEMBERSHIPS_REST_URL = "/conceptRest/api/memberships/";
var PROJECTS_REST_URL = "http://concept-pm.euprojects.net/projects/";
var NOTIFICATIONS_REST_URL = "/conceptRest/api/notifications_count/";
var SHARE_REST_URL = "/conceptRest/api/share/";
var MINDMAP_REST_URL = "/conceptRest/api/mm_app/";

var STORYBOARD_REST_URL = "http://concept-sb.euprojects.net/storyboard/";
//var STORYBOARD_REST_URL = "http://192.168.7.196:8080/storyboard/";

var LIKE_REST_URL = "/conceptRest/api/like/";

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
    })

    //If not dashboard page, select current project
    if (!isDashboardPage()) {
        logger("Selecting current project with id: " + $('#projectID').val());
        $('#project-select').val($('#projectID').val()).change();
    }

    if (isFM_app()) {
        //Initialize file-upload form
        $('#fileupload').fileupload({
            dataType: 'json',
            done: function (e, data) {
                $.each(data.result, function (index, result) {
                    if (result.status === 'FAIL') {
                        $('.files').append('- ' + result.fileName + '(ERROR) <br/>');
                        $('.files').css("display", "block");
                    } else {
                        $('.files').append('- ' + result.fileName + '<br/>');
                        $('.files').css("display", "block");
                    }
                });
            }
        });
    }

});

//
// Public API 
//

function setNotifications(project_id) {
    if (project_id > 0) {
        $.ajax({
            url: NOTIFICATIONS_REST_URL + project_id
        }).then(function (data) {
            $("#notifications-count").html(data);

        });
    }
}

function setIsPublic(componentCode, componentID) {
    var isPublic = $("#" + componentCode + componentID).hasClass("icon-active") ? 1 : 0;

    $.ajax({
        url: SHARE_REST_URL + componentID,
        type: 'POST',
        data: {
            "componentCode": componentCode,
            "isPublic": isPublic
        },
    }).then(function (data) {
        if (data == 0) {
            logger('Could not change isPublic state for component with id: ' + componentCode + componentID);
        } else {

            if (isPublic) {
                $("#" + componentCode + componentID).removeClass("icon-active");
            }
            else {
                $("#" + componentCode + componentID).addClass("icon-active");
            }

        }
    });

}

function like(componentCode, componentID) {
    var isLiked = $("#Like" + componentCode + componentID).hasClass("icon-active") ? 1 : 0;
    var itemLikeCountUp = parseInt($("#LikeCount" + componentCode + componentID).text()) + 1;
    var itemLikeCountDown = parseInt($("#LikeCount" + componentCode + componentID).text()) - 1;

    $.ajax({
        url: LIKE_REST_URL + componentID,
        type: 'POST',
        data: {
            "componentCode": componentCode
        },
    }).then(function (data) {
        if (data == 0) {
            logger('Could not change Like state for component with id: ' + componentCode + componentID);
        } else {

            if (isLiked) {
                $("#Like" + componentCode + componentID).removeClass("icon-active");
                $("#LikeCount" + componentCode + componentID).text(itemLikeCountDown);
            }
            else {
                $("#Like" + componentCode + componentID).addClass("icon-active");
                $("#LikeCount" + componentCode + componentID).text(itemLikeCountUp);
            }

        }
    });

}

function createMindMap() {
    var projectID = $("#projectID").val();
    if (projectID > 0) {
        $.ajax({
            url: MINDMAP_REST_URL + projectID,
            type: 'GET'
        }).then(function (data) {
            if ("SUCCESS" === data.code) {
                window.open(data.returnobject, '_blank');
            } else {
                logger(data.message);
            }
        });
    } else {
        logger("Could not create MindMap invalid projectID: " + projectID)
    }
}

function createMoodboard() {
    var projectID = $("#projectID").val();
    var userID = $("#userID").val();
    var url = MOODBOARD_REST_URL + "scene/new?pid=" + projectID + "&uid=" + userID;
    window.open(url, '_blank');
}

function createStoryboard() {
    var projectID = $("#projectID").val();
    var userID = $("#userID").val();
    var url = STORYBOARD_REST_URL + "storyboard/new?pid=" + projectID + "&uid=" + userID;
    window.open(url, '_blank');
}

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
            $("#project-view").attr("href", PROJECTS_REST_URL + projectID);
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

        if (isBA_all()) {
            $("#sort").show();

            $("#ba-add").attr("href", "/ba_app?projectID=" + projectID);
            $("#ba-add").show();
            $("#ba-all").load("/briefanalysis_all/" + projectID + "?limit=200");

            $("#ba-placeholder").hide();

            $("#project-members").show();
            $("#project-view").show();
        }

        if (isMM_all()) {
            $("#sort").show();

            $("#mm-add").attr("href", "/mm_app?projectID=" + projectID);
            $("#mm-add").show();
            $("#mm-all").load("/mindmaps_all/" + projectID + "?limit=200");

            $("#mm-placeholder").hide();

            $("#project-members").show();
            $("#project-view").show();
        }

        if (isSB_all()) {
            $("#sort").show();

            $("#sb-add").attr("href", "/sb_app?projectID=" + projectID);
            $("#sb-add").show();
            $("#sb-all").load("/storyboards_all/" + projectID + "?limit=200");

            $("#sb-placeholder").hide();

            $("#project-members").show();
            $("#project-view").show();
        }

        if (isSK_app()) {
            $("#project-members").show();
            $("#project-view").show();
        }

        if (isSK_all()) {
            $("#sort").show();

            $("#sk-add").attr("href", "/sk_app?projectID=" + projectID);
            $("#sk-add").show();
            $("#sk-all").load("/sketches_all/" + projectID + "?limit=200");

            $("#sk-placeholder").hide();
            $("#project-members").show();
            $("#project-view").show();
        }

        if (isNF_app()) {
            $("#project-members").show();
            $("#project-view").show();
            $("#nf-button").show();
            $("#nf-placeholder").show();
        }

        //Enable Chat Session     
        enableChat();

        //Set Notifications Number
        setNotifications(projectID);

    } else {
        //Disable Chat Session
        disableChat();

        if (isDashboardPage()) {
            disableDashboardPage();
            if ($('#projectID').val() > 0) {
                $("#ba-app-link").removeAttr(href);
                $("#fm-app-link").removeAttr(href);
                $("#se-app-link").removeAttr(href);
                $("#mm-app-link").removeAttr(href);
                $("#sb-app-link").removeAttr(href);
                $("#sk-app-link").removeAttr(href);
            }

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
        } else if (isBA_all()) {
            $(".panel-body").hide();
            $("#sort").hide();
            $("#ba-placeholder").show();
            $(".panel-footer").hide();
            $("#project-members").hide();
            $("#project-view").hide();
            $("#ba-add").hide();
        } else if (isMM_all()) {
            $(".panel-body").hide();
            $("#sort").hide();
            $("#mm-placeholder").show();
            $(".panel-footer").hide();
            $("#project-members").hide();
            $("#project-view").hide();
            $("#mm-add").hide();
        } else if (isSK_app()) {
            $("#project-members").hide();
            $("#project-view").hide();
        } else if (isSK_all()) {
            $(".panel-body").hide();
            $("#sort").hide();
            $("#sk-placeholder").show();
            $(".panel-footer").hide();
            $("#project-members").hide();
            $("#project-view").hide();
            $("#sk-add").hide();
        } else if (isSB_all()) {
            $(".panel-body").hide();
            $("#sort").hide();
            $("#sb-placeholder").show();
            $(".panel-footer").hide();
            $("#project-members").hide();
            $("#project-view").hide();
            $("#sb-add").hide();
        } else if (isNF_app()) {
            $("#nf-placeholder").show();
            $(".panel-body").hide();
            $(".panel-footer").hide();
            $("#project-members").hide();
            $("#project-view").hide();
            $("#nf-button").hide();
        }

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

//Return true if current page is FM APP
function isSK_app() {
    return location.pathname === "/sk_app";
}

//Return true if current page is FM ALL
function isSK_all() {
    return location.pathname === "/sk_all";
}

//Return true if current page is NF ALL
function isNF_app() {
    return location.pathname === "/notifications_app";
}

//Return true if current page is MM ALL
function isMM_all() {
    return location.pathname === "/mm_all";
}

//Return true if current page is SB ALL
function isSB_all() {
    return location.pathname === "/sb_all";
}

function saveBAContent() {
    console.log(tinymce.activeEditor.getContent({format: 'raw'}));
    console.log($('#document-title').val());
}

function loadMD() {
    $("#metadata-area").show();
    $(".nav-main").hide();
    $(".nav-apps").hide();
    $(".project-selection").hide();
    $(".nav-chat").hide();
    $(".nav-chat-hidden").hide();

    $(".content").addClass("content-disabled");
}

function unloadMD() {
    logger("Unloading metadata sidebar...");
    $("#metadata-area").hide();
    $(".nav-main").show();
    $(".nav-apps").show();
    $(".project-selection").show();
    $(".nav-chat").show();
    $("#ba_table tr ").removeClass('warning');
    $("#fm_table tr ").removeClass('warning');
    $("#mm_table tr ").removeClass('warning');
    $("#sb_table tr ").removeClass('warning');
    $("#sk_table tr ").removeClass('warning');

    $(".content").removeClass("content-disabled");
}

//Sample post using jQuery
function deleteWidgetItem(URL) {
    console.log("CRF is: " + $("#_csrf").val());
    console.log("URL is: " + 'http://localhost:8080/' + URL)

    $.ajax({
        url: 'http://localhost:8080/ba_app/2',
        type: 'DELETE',
        data: {
            "projectID": projectID,
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
}

function loadMetadata(componentID, component, project_id, event) {
    $(event).parent().parent().addClass('warning');
    logger("Loading metadata for component: " + component + " with id: " + componentID + " for project with id: " + project_id);
    $("#metadata-area").load("/metadata?cid=" + componentID + "&component=" + component + "&project_id=" + project_id);
    //Load metadata content
    logger("Switching Dashboard sidebar to Metadata sidebar");
    loadMD();
}

function deleteBAItem(ba_id, project_id) {
    $("#ba-all").load("/ba_app_delete_all?ba_id=" + ba_id + "&project_id=" + project_id + "&limit=200");
}

function deleteFMItem(fm_id, project_id) {
    console.log("i am in....")
    $("#fm-all").load("/fm_app_delete_all?fm_id=" + fm_id + "&project_id=" + project_id + "&limit=200");
}

function deleteSKItem(sk_id, project_id) {
    $("#sk-all").load("/sk_app_delete_all?sk_id=" + sk_id + "&project_id=" + project_id + "&limit=200");
}

function addProjectIDToForm(formName) {
    $("#" + formName).submit(function (eventObj) {
        $('<input />').attr('type', 'hidden')
                .attr('name', "projectID")
                .attr('value', $("#projectID").val())
                .appendTo("#" + formName);
        return true;
    });
}

$(".chat-list").scrollTop($(".chat-list")[0].scrollHeight);

$('#chat-button').attr('disabled', true);
$('#chat-message').keyup(function () {
    if ($(this).val().length != 0)
        $('#chat-button').attr('disabled', false);
    else
        $('#chat-button').attr('disabled', true);
});

function chatScrollDown() {
    var $cont = $('.chat-list');
    $cont[0].scrollTop = $cont[0].scrollHeight;
    $cont[0].scrollTop = $cont[0].scrollHeight;
}

$('#chat-message').keyup(function (e) {
    if (e.keyCode == 13) {
        chatScrollDown();
        $(this).val('');
    }
}).focus();

$("#notification").ready(
    function () {
        setTimeout(function () {
            $("#notification").hide()
        }, 4000);
    }
);