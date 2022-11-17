
$(function () {

    // Setup all runtimes
    $(".file-uploader").pluploadQueue({
        runtimes: 'html5, html4',
        url: '/inserirDados',
        chunk_size: '386Kb',
        unique_names: true,
        multipart_params: {
            one: '1',
            two: '2',
            three: '3',
            object: {
                four: '4',
                five: '5'
            },
            array: ['6', '7', '8']
        },
        filters: {
            max_file_size: '131072Kb',
            mime_types: [{
                title: "Upload de documentos",
                extensions: "jpg,gif,png,pdf,txt,doc,docx,xls,xlsx"
            }]
        },
        resize: {
            width: 320,
            height: 240,
            quality: 90
        }
    });

    // Setup html5 version
    $(".html5-uploader").pluploadQueue({
        // General settings
        runtimes: 'html5',
        url: '/inserirDados',
        chunk_size: '386Kb',
        unique_names: true,
        multipart_params: {
            one: '1',
            two: '2',
            three: '3',
            object: {
                four: '4',
                five: '5'
            },
            array: ['6', '7', '8']
        },
        filters: {
            max_file_size: '131072Kb',
            mime_types: [{
                title: "Upload de documentos",
                extensions: "jpg,gif,png,pdf,txt,doc,docx,xls,xlsx"
            }]
        },
        // Resize images on clientside if we can
        resize: {
            width: 320,
            height: 240,
            quality: 90
        }
    });

    // Setup html4 version
    $(".html4-uploader").pluploadQueue({
        runtimes: 'html4',
        url: '/inserirDados',
        unique_names: true,
        multipart_params: {
            one: '1',
            two: '2',
            three: '3',
            object: {
                four: '4',
                five: '5'
            },
            array: ['6', '7', '8']
        },
        filters: {
            max_file_size: '131072Kb',
            mime_types: [{
                title: "Upload de documentos",
                extensions: "jpg,gif,png,pdf,txt,doc,docx,xls,xlsx"
            }]
        }
    });
});