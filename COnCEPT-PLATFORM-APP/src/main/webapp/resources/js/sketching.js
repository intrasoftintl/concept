//Define global variable
var lc;
localStorage.setItem('drawing', '');
$(document).ready(function () {


    //Load currenct selected Sketch
    loadSKContent($("#document-content").val());


    // Initialization
    var imageSize = {width: 200, height: 200};
    var imageBounds = {
        x: 0, y: 0, width: imageSize.width, height: imageSize.height
    };


    lc = LC.init(document.getElementsByClassName('literally')[0], {
        backgroundColor: 'whiteSmoke',
        imageURLPrefix: '../resources/img/sketching',
        imageSize: 'imageSize'
    });


    // Local Storage
    var localStorageKey = 'drawing'
    if (localStorage.getItem(localStorageKey)) {
        lc.loadSnapshotJSON(localStorage.getItem(localStorageKey));
    }
    lc.on('drawingChange', function () {
        localStorage.setItem(localStorageKey, lc.getSnapshotJSON());
    });

    // Export PNG
    $('.export [data-action=export-as-png]').click(function (e) {
        e.preventDefault();

        // Get canvas image
        window.open(lc.canvasForExport().toDataURL("image/png"));

    });

});

//Include some content and content-thumbnail on form submission
function includeContent() {
    $("#document-content").val(localStorage.getItem("drawing"));
    $("#content-thumbnail").val(lc.canvasForExport().toDataURL("image/png"));
}

function loadSKContent(value) {
    localStorage.setItem('drawing', value);
}