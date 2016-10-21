function initMetadata(data) {
    logger("Initializing Metadata tags...");

    $('#tags').tagsInput({
        'height': '160px',
        'width': '238px',
        'interactive': true,
        'placeholderColor': '#ccc'
    });

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