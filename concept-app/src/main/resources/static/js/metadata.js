function initMetadata(data) {
    logger("Initializing Metadata tags...");

    $('#tags').tagsInput({
        'height': '160px',
        'width': '238px',
        'interactive': true,
        'placeholderColor': '#ccc'
    });

    //var data = [{"children":[{"children":[{"children":[{"label":"Order","id":12},{"label":"Complexity","id":13}],"label":"Aesthetic Function","id":11},{"children":[{"children":[{"label":"Brand","id":16},{"label":"Material","id":17}],"label":"Identity","id":15},{"children":[{"label":"Stability","id":19},{"label":"Usability","id":20}],"label":"Practicality","id":18}],"label":"Indication Function","id":14},{"children":[{"children":[{"label":"Classic","id":23},{"label":"Corporate","id":24},{"label":"ArtDeco","id":25},{"label":"National","id":26},{"label":"Pop","id":27},{"label":"Romantic","id":28},{"children":[{"label":"Time","id":30},{"label":"Region","id":31},{"label":"Movement","id":32}],"label":"Character","id":29}],"label":"Style","id":22},{"children":[{"label":"Old","id":35},{"label":"Aggressive","id":36},{"label":"Warm","id":37},{"label":"Cold","id":38},{"label":"Dark","id":39}],"label":"Association","id":34}],"label":"Symbol Function","id":21}],"label":"Product Function","id":10},{"children":[{"label":"Manufacturing","id":41},{"label":"IT","id":42},{"label":"Kitchenware","id":43},{"label":"Automotive","id":44}],"label":"Product Category","id":40}],"label":"Design Product","id":8}];

    logger("Initializing Metadata Categories tree...");
    $(function () {
        var $tree = $('#tree');
        $tree.tree({
            data: data,
            autoOpen: true,
            saveState: false
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
                    } else {
                        $tree.tree('addToSelection', selected_node);
                    }
                }
        );
    });

    console.log('Categories: ' + $("#categories").val());
            $('#tree').tree('setState', JSON.parse($("#categories").val()));
    logger("Finished Metadata initalization...");


    $("#metadataForm").submit(function (event) {
        logger("Setting KeywordsAndCategories...");
        $("#keywords").val($("#tags").val());
        $("#categories").val(JSON.stringify($('#tree').tree('getState')));
    });

    $("#autoannotate").submit(function (e) {
        e.preventDefault();
        var form = this;
        console.log($(this).attr('action'));
        $.ajax({
            type: 'POST',
            url: $(this).attr('action'),
            data: $(this).serialize(),
            success: function (data) {
                $($('#tags')[0]).importTags('');
                $($('#tags')[0]).importTags(data);
                //This does not work
                $($('#autoannotatebtn')[0]).removeClass('btn-danger');
            }
        });
    });
}

function initCategorySearch(data) {

    logger("Initializing Search Categories tree...");
    $(function () {
        var $tree = $('#category-search');
        $tree.tree({
            data: data,
            autoOpen: true,
            saveState: false
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
                    } else {
                        $tree.tree('addToSelection', selected_node);
                    }
                }
        );
    });
    logger("Finished Search Categories tree initalization...");
}