$(document).ready(function() {
	
	// Initialization
	var imageSize = { width: 200, height: 200 };
	var imageBounds = {
		x: 0, y: 0, width: imageSize.width, height: imageSize.height
	};
	var lc = LC.init(document.getElementsByClassName('literally')[0], {
		backgroundColor: 'whiteSmoke',
		imageURLPrefix: '../resources/img/sketching',
		imageSize: 'imageSize',
	});
	
	// Local Storage
	var localStorageKey = 'drawing'
	if (localStorage.getItem(localStorageKey)) {
		lc.loadSnapshotJSON(localStorage.getItem(localStorageKey));
	}
	lc.on('drawingChange', function() {
		localStorage.setItem(localStorageKey, lc.getSnapshotJSON());
	});

	// Export PNG
	$('.export [data-action=export-as-png]').click(function(e) {
		e.preventDefault();

		// Get canvas image
		window.open(lc.canvasForExport().toDataURL());

		// Get image
		//window.open(lc.getImage().toDataURL());
		
		// Get bounded image
		//window.open(lc.getImage({rect: imageBounds}).toDataURL());
		
	});

});

