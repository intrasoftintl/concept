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
    $("#fm_dashboard").load("/filemanagement/" + projectID + "?limit=9");
}

//
//TODO: A more efficient way to disable/enable dashboard widgets (in ex. DOM iter.)
//

//Disable dashboard page widgets

function disableDashboardPage() {
    console.log("Disable dashboard modules...")
    //First Row Content to disable
    $("#firstRowContent").addClass("disabled");
    $("#brDIVbuttons").addClass("disabled");
    $("#brDIVtable").addClass("disabled");
    $("#baBut1").addClass("disabled");
    $("#brBut2").addClass("disabled");
    $("#fmDIVbuttons").addClass("disabled");
    $("#fm_dashboard").addClass("disabled");
    $("#fm_dashboard").empty();
    $("#fmBut1").addClass("disabled");
    $("#fmBut2").addClass("disabled");
    $("#seDIVbuttons").addClass("disabled");
    $("#seBut1").addClass("disabled");
    $("#seBut2").addClass("disabled");
    //Second Row Content to disable
    $("#secondRowContent").addClass("disabled");
    $("#mmDIVbuttons").addClass("disabled");
    $("#mmBut1").addClass("disabled");
    $("#mmBut2").addClass("disabled");
    $("#mbDIVbuttons").addClass("disabled");
    $("#mbBut1").addClass("disabled");
    $("#mbBut2").addClass("disabled");
    $("#skDIVbuttons").addClass("disabled");
    $("#skBut1").addClass("disabled");
    $("#skBut2").addClass("disabled");
    //Hide Project Members List
    $("#projectMembers").hide();
    //$("#projectNew").hide();
    $("#projectView").hide();
    
    $("#ba-placeholder").show();
    $("#fm-placeholder").show();
    $("#se-placeholder").show();
    $("#mm-placeholder").show();
    $("#sb-placeholder").show();
    $("#sk-placeholder").show();
}

function enableDashboardPage() {
//First Row Content to disable
    $("#firstRowContent").removeClass("disabled");
    $("#brDIVbuttons").removeClass("disabled");
    $("#brDIVtable").removeClass("disabled");
    $("#baBut1").removeClass("disabled");
    $("#brBut2").removeClass("disabled");
    $("#fmDIVbuttons").removeClass("disabled");
    $("#fmDIVtable").removeClass("disabled");
    $("#fmBut1").removeClass("disabled");
    $("#fmBut2").removeClass("disabled");
    $("#seDIVbuttons").removeClass("disabled");
    $("#seBut1").removeClass("disabled");
    $("#seBut2").removeClass("disabled");
    //Second Row Content to disable
    $("#secondRowContent").removeClass("disabled");
    $("#mmDIVbuttons").removeClass("disabled");
    $("#mmBut1").removeClass("disabled");
    $("#mmBut2").removeClass("disabled");
    $("#mbDIVbuttons").removeClass("disabled");
    $("#mbBut1").removeClass("disabled");
    $("#mbBut2").removeClass("disabled");
    $("#skDIVbuttons").removeClass("disabled");
    $("#skBut1").removeClass("disabled");
    $("#skBut2").removeClass("disabled");
    //Show Project Members List
    $("#projectMembers").show();
    //$("#projectNew").show();
    $("#projectView").show();
    
    $("#ba-placeholder").hide();
    $("#fm-placeholder").hide();
    $("#se-placeholder").hide();
    $("#mm-placeholder").hide();
    $("#sb-placeholder").hide();
    $("#sk-placeholder").hide();
}



