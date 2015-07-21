//Static variables
//
var MEMBERSHIPS_REST_URL = "http://localhost:8080/concept/rest/memberships/";
//

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
            $("#ba-add").attr("href", "/ba_app/"  + projectID);
            $("#fm-add").attr("href", "/fm_app?projectID=" + projectID);
            $("#sk-add").attr("href", "/sk_app?projectID=" + projectID);

            //Enable DashboardPage
            enableDashboardPage();
            //Load Dashboard content
            loadDashboardContent(projectID);
        } else {
            $('#projectID').val(projectID);
        }

        if (isFM_app()) {
            $("#fm-placeholder").hide();

            $(".panel-body").show();
            $(".panel-footer").show();
            $("#sort").show();

            $("#fm-all").removeClass("disabled");

            $("#project-members").show();
            $("#project-view").show();
        }

        if (isFM_all()) {
            $("#fm-placeholder").hide();
            $("#sort").show();
            $("#fm-add").attr("href", "/fm_app?projectID=" + projectID);
            $("#fm-all").load("/filemanagement_all/" + projectID + "?limit=200");

            $("#project-members").show();
            $("#project-view").show();
        }

        if (isBA_App()) {
            $("#project-members").show();
            $("#project-view").show();
        }

    } else if (isDashboardPage()) {
        disableDashboardPage();
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
    } else if (isBA_App) {
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

function saveBAContent() {
    console.log(tinymce.activeEditor.getContent({format: 'raw'}));
    console.log($('#document-title').val());
}

function loadBAContent() {
    tinymce.activeEditor.setContent('<h2>Que onda?</h2><p>Chabon!</p>', {format: 'raw'});
    $('#document-title').val("Document 1");
}

function saveSKContent() {
    console.log(localStorage.getItem('drawing'));
    console.log($('#document-title').val());
}

function loadSKContent() {
    localStorage.setItem('drawing', '{"shapes":[{"className":"LinePath","data":{"order":3,"tailSize":3,"smooth":true,"pointCoordinatePairs":[[219,196],[222,199],[226,205],[239,226],[246,240],[250,248],[255,254],[256,257],[257,257],[257,254],[257,234],[255,225],[253,218],[253,214],[253,213],[253,215],[260,231],[267,244],[273,256],[276,263],[277,266],[278,268],[278,263],[274,237],[268,197],[268,151],[268,126],[269,114],[276,107],[281,105],[285,105],[289,112]],"pointSize":5,"pointColor":"#000"},"id":"8b8b1403-91ae-3245-9fba-8bc0b8714d00"},{"className":"LinePath","data":{"order":3,"tailSize":3,"smooth":true,"pointCoordinatePairs":[[285,207],[284,212],[284,218],[284,227],[284,233],[289,238],[293,241],[301,244],[307,244],[310,244],[315,232],[315,215],[314,199],[308,187],[302,179],[296,175],[290,173],[286,173],[281,189],[281,203],[281,220],[283,234],[288,241],[293,245],[300,248],[304,249],[308,245],[311,239],[313,230],[313,217],[312,203],[307,195],[302,188],[297,185],[292,184],[290,186],[290,191],[290,200],[294,207],[301,213],[306,214],[311,214],[314,211],[316,201],[316,193],[316,190],[315,190]],"pointSize":5,"pointColor":"#000"},"id":"fef6df83-226e-1cc2-70d5-c16f96bc61ff"},{"className":"LinePath","data":{"order":3,"tailSize":3,"smooth":true,"pointCoordinatePairs":[[332,178],[332,177],[331,177],[329,177],[328,178],[327,193],[327,208],[327,221],[330,230],[336,233],[349,235],[354,234],[362,208],[362,191],[355,175],[346,165],[334,159],[323,157],[315,157],[310,159],[309,169],[309,181],[317,194],[321,199],[331,205],[338,206],[346,204],[348,193],[348,184],[348,169],[344,163],[341,158],[339,158],[337,158]],"pointSize":5,"pointColor":"#000"},"id":"d645212a-f6ff-e4b1-ee01-c958d54a1930"},{"className":"LinePath","data":{"order":3,"tailSize":3,"smooth":true,"pointCoordinatePairs":[[351,96],[367,142],[383,193],[388,221],[392,235],[394,246],[395,250],[397,251],[397,247],[396,241],[391,200],[391,181],[399,163],[405,162],[411,162],[420,170],[424,182],[426,189],[427,197],[428,201],[428,202],[429,202],[431,200],[436,194],[450,182],[463,169],[474,157],[483,144],[486,134]],"pointSize":5,"pointColor":"#000"},"id":"a0767013-864f-6ec7-2875-7ec001a1dc0d"},{"className":"LinePath","data":{"order":3,"tailSize":3,"smooth":true,"pointCoordinatePairs":[[453,143],[451,142],[451,147],[451,154],[451,158],[453,162],[457,166],[460,166],[463,166],[467,161],[469,154],[470,139],[468,127],[464,119],[458,112],[451,112],[447,114],[439,143],[439,156],[440,159],[444,164],[452,166],[456,166],[462,165],[464,158],[467,144],[467,131],[465,123],[459,121],[454,121],[452,121],[451,126],[451,131],[451,135],[455,138],[460,139],[465,139],[468,135],[469,126],[469,115],[469,111],[466,108],[460,108],[458,111]],"pointSize":5,"pointColor":"#000"},"id":"7c63a192-7af4-b94b-420d-22904c0cfeaa"},{"className":"LinePath","data":{"order":3,"tailSize":3,"smooth":true,"pointCoordinatePairs":[[486,106],[483,107],[483,113],[482,119],[482,124],[482,126],[486,129],[492,129],[498,129],[504,122],[509,113],[509,102],[509,98],[507,91],[500,88],[496,87],[490,87],[488,89],[487,96],[487,107],[492,115],[497,119],[502,121],[505,122],[508,122],[509,118],[509,111],[508,104],[503,100],[497,99],[494,99]],"pointSize":5,"pointColor":"#000"},"id":"51223693-bb7c-864b-69ce-ef7645cfdad3"}],"colors":{"primary":"#000","secondary":"#fff","background":"whiteSmoke"}}');
    location.reload();
    $('#document-title').val("Sketch 1");
}

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
