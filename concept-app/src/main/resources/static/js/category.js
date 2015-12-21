$(document).ready(function () {

    $("#hasParent").click(function () {
        if (!$("#hasParent").is(':checked')) {
            $("#parentCategory").val('');
            $("#parentCategoryID").val('');
            $("#parentCategory").attr("disabled", true);
        } else {
            $("#parentCategory").removeAttr("disabled");
        }
    });

//    $("#existingHasParent").click(function () {
//        if (!$("#existingHasParent").is(':checked')) {
//            $("#existingParentClass").val('');
//            $("#existingParentClassID").val('');
//            $("#existingParentClass").attr("disabled", true);
//        } else {
//            $("#existingParentClass").removeAttr("disabled");
//        }
//    });

    $("#parentCategory").autocomplete({
        minLength: 1,
        source: function (request, response) {
            jQuery.ajax({
                featureClass: "P",
                style: "full",
                maxRows: 12,
                url: "/conceptRest/api/category/search",
                dataType: "jsonp",
                contentType: "text/html; charset=UTF-8",
                encoding: "UTF-8",
                beforeSend: function (xhr) {
                    xhr.setRequestHeader("Content-Type", "text/html;charset=utf-8");
                },
                data: {
                    format: "json",
                    keyword: request.term,
                    operation: "autocomplete"
                },
                success: function (data) {
                    if (data.total === 0) {
                        // Do nothing
                    } else {

                        response(jQuery.map(jQuery.makeArray(data.values), function (item) {

                            return {
                                label: item.value,
                                value: item.value,
                                id: item.id
                            }
                        }));
                    }

                },
                error: function () {

                }
            });
        },
        select: function (event, ui) {
            jQuery("#parentCategory").val(ui.item.value);
            jQuery("#parentCategoryID").val(ui.item.id);
            return false;
        }

    });

//    $("#existingParentClass").autocomplete({
//        minLength: 1,
//        source: function (request, response) {
//            jQuery.ajax({
//                featureClass: "P",
//                style: "full",
//                maxRows: 12,
//                url: "/paasword/rest/v1/class/search",
//                dataType: "jsonp",
//                contentType: "text/html; charset=UTF-8",
//                encoding: "UTF-8",
//                beforeSend: function (xhr) {
//                    xhr.setRequestHeader("Content-Type", "text/html;charset=utf-8");
//                },
//                data: {
//                    format: "json",
//                    keyword: request.term,
//                    operation: "autocomplete"
//                },
//                success: function (data) {
//                    if (data.total === 0) {
//                        // Do nothing
//                    } else {
//                        response(jQuery.map(jQuery.makeArray(data.values), function (item) {
//
//                            return {
//                                label: item.value,
//                                value: item.value,
//                                id: item.id
//                            }
//                        }));
//                    }
//
//                },
//                error: function () {
//
//                }
//            });
//        },
//        select: function (event, ui) {
//            jQuery("#existingParentClass").val(ui.item.value);
//            jQuery("#existingParentClassID").val(ui.item.id);
//            return false;
//        }
//
//    });

    showAllClasses();

});

var initTreegrid = function initTreegrid() {
    $('.tree').treegrid({
        initialState: 'collapsed'
    });
}

function showAllClasses() {
    // Load hierarchy of classes into treeGrid DIV
    $("#treeGrid").load("/paasword/rest/v1/classes", initTreegrid);
}


