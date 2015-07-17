var isDashboard = true;

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
//First Row Content to disable
    $("#firstRowContent").addClass("disabled");
    $("#brDIVbuttons").addClass("disabled");
    $("#brDIVtable").addClass("disabled");
    $("#brBut1").addClass("disabled");
    $("#brBut2").addClass("disabled");
    $("#fmDIVbuttons").addClass("disabled");
    $("#fmDIVtable").addClass("disabled");
    $("#fmDIVtable").empty();
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
    $("#projectSettings").hide();
}

function enableDashboardPage() {
//First Row Content to disable
    $("#firstRowContent").removeClass("disabled");
    $("#brDIVbuttons").removeClass("disabled");
    $("#brDIVtable").removeClass("disabled");
    $("#brBut1").removeClass("disabled");
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
    $("#projectSettings").show();
}


$('#fileupload').fileupload({
    dataType: 'json',
    done: function (e, data) {
        $.each(data.result, function (index, result) {
            $('.files').append('- ' + result.fileName + '<br/>');
            $('.files').css("display", "block");
        });
    }
});
$('#tags').tagsInput({
    'height': '160px',
    'width': '238px',
    'interactive': true,
    'placeholderColor': '#ccc'
});
var data = [
    {
        label: 'Product Category', id: 1,
        children: [
            {label: 'Kitchenware', id: 11},
            {label: 'Exhibition', id: 12},
            {label: 'Lighting', id: 13},
            {label: 'Furniture', id: 14}
        ]
    },
    {
        label: 'Product Domain', id: 2,
        children: [
            {label: 'Medical', id: 21},
            {label: 'Cosumer', id: 22},
            {label: 'Sport', id: 23},
            {label: 'Market Analysis', id: 24},
            {label: 'Technology', id: 25},
            {label: 'Usability', id: 26}
        ]
    },
    {
        label: 'Product Language', id: 3,
        children: [
            {
                label: 'Style', id: 31,
                children: [
                    {
                        label: 'Period Style', id: 311,
                        children: [
                            {label: 'Classic', id: 3112},
                            {label: 'Chic', id: 3113},
                            {label: 'Modern', id: 3113},
                            {label: 'Artdeco', id: 3115}
                        ]
                    },
                    {
                        label: 'Partial Style', id: 312,
                        children: [
                            {label: 'National', id: 3121},
                            {label: 'Corporate', id: 3122},
                            {label: 'Target Style', id: 3123}
                        ]
                    }
                ]
            },
            {
                label: 'Material', id: 32,
                children: [
                    {label: 'Steel', id: 321},
                    {label: 'Stone', id: 322}
                ]
            },
            {
                label: 'Associations and Feelings', id: 33,
                children: [
                    {label: 'Cold', id: 331},
                    {label: 'Warm', id: 332},
                    {label: 'Aggressive', id: 333}
                ]
            }
        ]
    }
];
$(function () {
    var $tree = $('#tree');
    $tree.tree({
        data: data,
        autoOpen: true,
        saveState: true
    });
    $tree.bind(
            'tree.click',
            function (e) {
                // Disable single selection
                e.preventDefault();
                var selected_node = e.node;
                if (selected_node.id == undefined) {
                    console.log('The multiple selection functions require that nodes have an id');
                }

                if ($tree.tree('isNodeSelected', selected_node)) {
                    $tree.tree('removeFromSelection', selected_node);
                }
                else {
                    $tree.tree('addToSelection', selected_node);
                }
            }
    );
});
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
