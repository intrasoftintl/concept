//After html is rendered do the following...
$(document).ready(function () {
//On page load disable dashboard content
    disableDashboardPage();
    console.log("Selecting current project with id: " + $('#projectID').val());
    $('#project-select').val($('#projectID').val()).change();
}

);

//Load all widjects of dashboard page based on selected ProjectID
function loadDashboardContent(projectID) {
    logger("Loading dashboard content...");
    //Load content of BriefAnalysis widget
    $("#ba-dashboard").load("/briefanalysis/" + projectID + "?limit=5");
    //Load content of FileManagement widget
    $("#fm-dashboard").load("/filemanagement/" + projectID + "?limit=9");
    //Load content of Sketch widget
    $("#sk-dashboard").load("/sketch/" + projectID + "?limit=4");
}



//Delete a Brief Analysis object
function deleteBA(ba_id,project_id) {
    $("#ba-dashboard").load("/ba_app_delete?ba_id=" + ba_id + "&project_id="+project_id+"&limit=5");
}


//
//TODO: A more efficient way to disable/enable dashboard widgets (in ex. DOM iter.)
//
//Disable dashboard page widgets
function disableDashboardPage() {
    logger("Disabling dashboard modules...")

    $('.nav-apps ul').addClass("disabled");
    $('.app-link').addClass("disabled");

    $("#row1").addClass("disabled");
    $("#row2").addClass("disabled");

    $("#ba-buttons").addClass("disabled");
    $("#ba-content").addClass("disabled");
    $('#ba-dashboard').hide();
    $("#ba-add").addClass("disabled");
    $("#ba-placeholder").show();

    $("#fm-buttons").addClass("disabled");
    $("#fm-content").addClass("disabled");
    $('#fm-dashboard').hide();
    $("#fm-add").addClass("disabled");
    $("#fm-placeholder").show();

    $("#se-buttons").addClass("disabled");
    $("#se-content").addClass("disabled");
    $('#se-dashboard').hide();
    $("#se-add").addClass("disabled");
    $("#se-placeholder").show();

    $("#mm-buttons").addClass("disabled");
    $("#mm-content").addClass("disabled");
    $('#mm-dashboard').hide();
    $("#mm-add").addClass("disabled");
    $("#mm-placeholder").show();

    $("#sb-buttons").addClass("disabled");
    $("#sb-content").addClass("disabled");
    $('#sb-dashboard').hide();
    $("#mb-add").addClass("disabled");
    $("#sb-add").addClass("disabled");
    $("#sb-placeholder").show();

    $("#sk-buttons").addClass("disabled");
    $("#sk-content").addClass("disabled");
    $('#sk-dashboard').hide();
    $("#sk-add").addClass("disabled");
    $("#sk-placeholder").show();

    // Project Selection
    $("#project-members").hide();
    $("#project-view").hide();

    $(".nav-main-md").hide();
    $(".nav-keywords").hide();
    $(".nav-categories").hide();
    $(".nav-actions-md").hide();
}

function enableDashboardPage() {

    $('.nav-apps ul').removeClass("disabled");
    $('.app-link').removeClass("disabled");

    $("#row1").removeClass("disabled");
    $("#row2").removeClass("disabled");

    $("#ba-buttons").removeClass("disabled");
    $("#ba-content").removeClass("disabled");
    $('#ba-dashboard').show();
    $("#ba-add").removeClass("disabled");
    $("#ba-placeholder").hide();

    $("#fm-buttons").removeClass("disabled");
    $("#fm-content").removeClass("disabled");
    $('#fm-dashboard').show();
    $("#fm-add").removeClass("disabled");
    $("#fm-placeholder").hide();

    $("#se-buttons").removeClass("disabled");
    $("#se-content").removeClass("disabled");
    $('#se-dashboard').show();
    $("#se-add").removeClass("disabled");
    $("#se-placeholder").hide();

    $("#mm-buttons").removeClass("disabled");
    $("#mm-content").removeClass("disabled");
    $('#mm-dashboard').show();
    $("#mm-add").removeClass("disabled");
    $("#mm-placeholder").hide();

    $("#sb-buttons").removeClass("disabled");
    $("#sb-content").removeClass("disabled");
    $('#sb-dashboard').show();
    $("#mb-add").removeClass("disabled");
    $("#sb-add").removeClass("disabled");
    $("#sb-placeholder").hide();

    $("#sk-buttons").removeClass("disabled");
    $("#sk-content").removeClass("disabled");
    $('#sk-dashboard').show();
    $("#sk-add").removeClass("disabled");
    $("#sk-placeholder").hide();

    // Project Selection
    $("#project-members").show();
    $("#project-view").show();

    $(".nav-main-md").hide();
    $(".nav-keywords").hide();
    $(".nav-categories").hide();
    $(".nav-actions-md").hide();
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