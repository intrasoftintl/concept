//$('#tags').tagsInput({
//    'height': '160px',
//    'width': '238px',
//    'interactive': true,
//    'placeholderColor': '#ccc'
//});
//
//var data = [
//    {
//        label: 'Product Category', id: 1,
//        children: [
//            {label: 'Kitchenware', id: 11},
//            {label: 'Exhibition', id: 12},
//            {label: 'Lighting', id: 13},
//            {label: 'Furniture', id: 14}
//        ]
//    },
//    {
//        label: 'Product Domain', id: 2,
//        children: [
//            {label: 'Medical', id: 21},
//            {label: 'Cosumer', id: 22},
//            {label: 'Sport', id: 23},
//            {label: 'Market Analysis', id: 24},
//            {label: 'Technology', id: 25},
//            {label: 'Usability', id: 26}
//        ]
//    },
//    {
//        label: 'Product Language', id: 3,
//        children: [
//            {
//                label: 'Style', id: 31,
//                children: [
//                    {
//                        label: 'Period Style', id: 311,
//                        children: [
//                            {label: 'Classic', id: 3112},
//                            {label: 'Chic', id: 3113},
//                            {label: 'Modern', id: 3113},
//                            {label: 'Artdeco', id: 3115}
//                        ]
//                    },
//                    {
//                        label: 'Partial Style', id: 312,
//                        children: [
//                            {label: 'National', id: 3121},
//                            {label: 'Corporate', id: 3122},
//                            {label: 'Target Style', id: 3123}
//                        ]
//                    }
//                ]
//            },
//            {
//                label: 'Material', id: 32,
//                children: [
//                    {label: 'Steel', id: 321},
//                    {label: 'Stone', id: 322}
//                ]
//            },
//            {
//                label: 'Associations and Feelings', id: 33,
//                children: [
//                    {label: 'Cold', id: 331},
//                    {label: 'Warm', id: 332},
//                    {label: 'Aggressive', id: 333}
//                ]
//            }
//        ]
//    }
//];
//
//
//
//$(function () {
//    var $tree = $('#tree');
//    $tree.tree({
//        data: data,
//        autoOpen: true,
//        saveState: true
//    });
//    $tree.bind(
//            'tree.click',
//            function (e) {
//                // Disable single selection
//                e.preventDefault();
//
//                var selected_node = e.node;
//
//                if (selected_node.id == undefined) {
//                    console.log('The multiple selection functions require that nodes have an id');
//                }
//
//                if ($tree.tree('isNodeSelected', selected_node)) {
//                    $tree.tree('removeFromSelection', selected_node);
//                }
//                else {
//                    $tree.tree('addToSelection', selected_node);
//                }
//            }
//    );
//});
//
//
//function getData() {
//    //alert( JSON.stringify($('#tree').tree('getState')));
//
//    //  $("#tags").val("test1,test2");
//    $("#tags").addTag('foo22');
//    alert($("#tags").val());
//}