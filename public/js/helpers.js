/**
 * Created by Vladislav Litovka on 12/21/13.
 */

showLoading = function () {
    $('#loadingMessage').css({
        'width': $(document).width() + 'px',
        'height': $(document).height() + 'px',
        'top': '0',
        'left': '0'
    }).show();

    $('#loadingMessage img').css({
        'margin-left': $('#loadingMessage').width() / 2,
        'margin-top':  ($(window).height() / 2) + $(window).scrollTop()
    });
}

hideLoading = function () {
    $('#loadingMessage').delay(500).fadeOut('slow');
}

// alert dialog
$.extend({
    alert:function (message, title, okFunction) {
        var dialog = $("<div></div>");
        dialog.dialog({
            buttons:{
                "OK":function () {
                    if (okFunction) {
                        okFunction();
                    }
                    $(this).dialog("close");
                }
            },
            close:function (event, ui) {
                $(this).remove();
            },
            resizable:false,
            title:title,
            modal:true
        }).text(message);
    }
});

// prompt dialog
$.extend({
    prompt:function (message, title, token, yesFunction, noFunction) {
        var dialog = $("<div></div>");
        dialog.dialog({
            buttons:{
                "Yes":function () {
                    if (yesFunction) {
                        yesFunction();
                    }
                    $(this).dialog("close");
                },
                "No":function () {
                    $(this).dialog("close");
                    if (noFunction) {
                        noFunction();
                    }
                }
            },
            close:function (event, ui) {
                $(this).remove();
            },
            resizable:false,
            title:title,
            modal:true
        }).text(message).append('<input type="text" id="token" value="'+token+'" />').find('input').select().keydown(function(event) {
                if (event.which == 13) {
                    $(this).dialog("close");
                }
            });
    }
});

// prompt window
$.extend({
    yesNoDialog: function (message, title, yesFunction, noFunction, yesLabel, noLabel) {
        var dialog = $("<div></div>");

        var yesLabel = typeof yesLabel !== 'undefined' ? yesLabel : 'Yes';
        var noLabel = typeof noLabel !== 'undefined' ? noLabel : 'No';

        var buttons = new Object();

        buttons[yesLabel] = function () {
            if (yesFunction) {
                yesFunction();
            }

            $(this).dialog("close");
        };

        buttons[noLabel] = function () {
            $(this).dialog("close");

            if (noFunction) {
                noFunction();
            }
        }

        dialog.dialog({
            buttons: buttons,
            close: function (event, ui) {
                $(this).remove();
            },
            resizable: false,
            title: title,
            modal: true
        }).text(message);
    }
});