//After html is rendered do the following...
$(document).ready(function () {
//On page load disable dashboard content
    disableDashboardPage();
}

);

//Load all widjects of dashboard page based on selected ProjectID
function loadDashboardContent(projectID) {
    console.log("Loading dashboard content...")
    //Load content of FileManagement widget
    $("#fm-dashboard").load("/filemanagement/" + projectID + "?limit=9");
}

//
//TODO: A more efficient way to disable/enable dashboard widgets (in ex. DOM iter.)
//

//Disable dashboard page widgets
function disableDashboardPage() {
    console.log("Disable dashboard modules...")

    $("#row1").addClass("disabled");
    $("#row2").addClass("disabled");
    
    $("#ba-buttons").addClass("disabled");
    $("#ba-content").addClass("disabled");
    $("#ba-add").addClass("disabled");
    //$("#ba-all").addClass("disabled");
    $("#ba-placeholder").show();
    
    $("#fm-buttons").addClass("disabled");
    $("#fm-content").addClass("disabled");
    $("#fm-add").addClass("disabled");
    //$("#fm-all").addClass("disabled");
    $("#fm-placeholder").show();
    
    $("#se-buttons").addClass("disabled");
    $("#se-content").addClass("disabled");
    $("#se-add").addClass("disabled");
    //$("#se-all").addClass("disabled");
    $("#se-placeholder").show();

    $("#mm-buttons").addClass("disabled");
    $("#mm-content").addClass("disabled");
    $("#mm-add").addClass("disabled");
    //$("#mm-all").addClass("disabled");
    $("#mm-placeholder").show();
    
    $("#sb-buttons").addClass("disabled");
    $("#sb-content").addClass("disabled");
    $("#mb-add").addClass("disabled");
    $("#sb-add").addClass("disabled");
    //$("#sb-all").addClass("disabled");
    $("#sb-placeholder").show();
    
    $("#sk-content").addClass("disabled");
    $("#sk-buttons").addClass("disabled");
    $("#sk-add").addClass("disabled");
    //$("#sk-all").addClass("disabled");
    $("#sk-placeholder").show();
    
    // Project Selection
    $("#project-members").hide();
    $("#project-view").hide();    
}

function enableDashboardPage() {

    $("#row1").removeClass("disabled");
    $("#row2").removeClass("disabled");
    
    $("#ba-buttons").removeClass("disabled");
    $("#ba-content").removeClass("disabled");
    $("#ba-add").removeClass("disabled");
    //$("#ba-all").removeClass("disabled");
    $("#ba-placeholder").hide();
    
    $("#fm-buttons").removeClass("disabled");
    $("#fm-content").removeClass("disabled");
    $("#fm-add").removeClass("disabled");
    //$("#fm-all").removeClass("disabled");
    $("#fm-placeholder").hide();
    
    $("#se-buttons").removeClass("disabled");
    $("#se-content").removeClass("disabled");
    $("#se-add").removeClass("disabled");
    //$("#se-all").removeClass("disabled");
    $("#se-placeholder").hide();

    $("#mm-buttons").removeClass("disabled");
    $("#mm-content").removeClass("disabled");
    $("#mm-add").removeClass("disabled");
    //$("#mm-all").removeClass("disabled");
    $("#mm-placeholder").hide();
    
    $("#sb-buttons").removeClass("disabled");
    $("#sb-content").removeClass("disabled");
    $("#mb-add").removeClass("disabled");
    $("#sb-add").removeClass("disabled");
    //$("#sb-all").removeClass("disabled");
    $("#sb-placeholder").hide();
    
    $("#sk-buttons").removeClass("disabled");
    $("#sk-content").removeClass("disabled");
    $("#sk-add").removeClass("disabled");
    //$("#sk-all").removeClass("disabled");
    $("#sk-placeholder").hide();
   
    // Project Selection
    $("#project-members").show();
    $("#project-view").show();
}