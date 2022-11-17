$(function () {

    // Defaults
    Dropzone.autoDiscover = false;

    // Removable thumbnails
    $("#dropzone_remove").dropzone({
        paramName: "file", // The name that will be used to transfer the file
        dictDefaultMessage: 'Drop files to upload <span>or CLICK</span>',
        maxFilesize: 100, // MB
        addRemoveLinks: true
    });

});
