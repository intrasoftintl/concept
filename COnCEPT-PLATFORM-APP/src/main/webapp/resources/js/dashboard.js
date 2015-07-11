//After html is rendered do the following...
$(document).ready(function () {
    //On page load disable dashboard content
    disableDashboardPage();
    //Trigger event when project list is changed
    $('#projectSelect').change(function () {
        disableDashboardPage();
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
        } else {
            disableDashboardPage();
        }
    });
    //
    //TODO: A more efficient way to disable/enable dashboard widgets (DOM iter.)
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
            {label: 'Kitchenware', id: 2},
            {label: 'Exhibition', id: 3},
            {label: 'Lighting', id: 4},
            {label: 'Furniture', id: 5}
        ]
    },
    {
        label: 'Product Domain', id: 10,
        children: [
            {label: 'Medical', id: 11},
            {label: 'Cosumer', id: 12},
            {label: 'Sport', id: 13},
            {label: 'Market Analysis', id: 14},
            {label: 'Technology', id: 15},
            {label: 'Usability', id: 16}
        ]
    },
    {
        label: 'Product Language', id: 10,
        children: [
            {
                label: 'Style', id: 10,
                children: [
                    {
                        label: 'Period Style', id: 10,
                        children: [
                            {label: 'Classic', id: 11},
                            {label: 'Chic', id: 12},
                            {label: 'Modern', id: 13},
                            {label: 'Artdeco', id: 14}
                        ]
                    },
                    {
                        label: 'Partial Style', id: 10,
                        children: [
                            {label: 'National', id: 11},
                            {label: 'Corporate', id: 12},
                            {label: 'Target Style', id: 13}
                        ]
                    }
                ]
            },
            {
                label: 'Material', id: 10,
                children: [
                    {label: 'Steel', id: 11},
                    {label: 'Stone', id: 12}
                ]
            },
            {
                label: 'Associations and Feelings', id: 10,
                children: [
                    {label: 'Cold', id: 11},
                    {label: 'Warm', id: 12},
                    {label: 'Aggressive', id: 12}
                ]
            }
        ]
    }
];

$('#tree').tree({
    data: data
});