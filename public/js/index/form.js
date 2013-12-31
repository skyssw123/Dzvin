/**
 * Created by Vladislav Litovka on 12/21/13.
 */

var map;
var markers = [];

$(function () {
    $('#pushNotificationID').click(onPushClick);
    $('input[name=isSimple]').click(onIsSimpleChange);
    $('input[name=useGeo]').click(onUseGeoChange);
    $('input[name=startDate], input[name=endDate]').datetimepicker();
    $('#resetDateStart').click(function () { $('input[name=startDate]').val(''); });
    $('#resetDateEnd').click(function () { $('input[name=endDate]').val(''); });
});

onPushClick = function () {
    var points = [];
    $('#notification_points tr.marker').each(function () {
        points.push({
            lat: $(this).find('.lat').html(),
            lon: $(this).find('.lon').html(),
            title: $(this).find('.title input').val()
        })
    });
    $('input[name=points]').val($.toJSON(points));

    if ($('input[name=pushText]').val().length) {
        showLoading();
        $('#pushForm').submit();
    } else {
        $('input[name=pushText]').focus();
        $.alert('Please, enter message text', 'Dzvin');
        return false;
    }
}

onIsSimpleChange = function () {
    if (parseInt($('input[name=isSimple]:checked').attr('value')) == 1) {
        $('.extended').fadeOut();
    } else {
        $('.extended').fadeIn();
    }
}

onUseGeoChange = function () {
    if (parseInt($('input[name=useGeo]:checked').attr('value')) == 1) {
        $('#geo').show();
        notificationMapInit();
    } else {
        $('#geo').fadeOut();
    }
}

notificationMapInit = function () {
    var mapOptions = {
        center: new google.maps.LatLng(50.449279, 30.523832),
        zoom: 10,
        disableDoubleClickZoom: true,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    if (!map) {
        map = new google.maps.Map(document.getElementById("notification_map"), mapOptions);
        google.maps.event.addListener(map, 'dblclick', onMapDoubleClick);
    }
}

/* google maps actions*/
onMapDoubleClick = function (event) {
    markers.push(new google.maps.Marker({
        position: event.latLng,
        map: map,
        draggable: true
    }));
    markers[markers.length - 1].domXML = $('<tr class="marker"></tr>');
    markers[markers.length - 1].domXML.append('<td><div class="lat">' + event.latLng.nb + '</div></td>');
    markers[markers.length - 1].domXML.append('<td><div class="lon">' + event.latLng.ob + '</div></td>');
    markers[markers.length - 1].domXML.append('<td class="title"><input type="text" style="width: 160px;" name="title"/></td>');
    markers[markers.length - 1].domXML.append('<td class="del"><a href="javascript:void(0);"><img src="/img/remove.png" width="14" border="0"></a></td>');
    markers[markers.length - 1].removeMarker = removeMarker;
    var that = markers[markers.length - 1];
    markers[markers.length - 1].domXML.find('.del a').click(function () {
        $(this).parent().parent().remove();
        that.removeMarker();
    });
    google.maps.event.addListener(markers[markers.length - 1], 'dragend', onMarkerDragged);
    $('#notification_points tr:first-child').after(markers[markers.length - 1].domXML);
}

onMarkerDragged = function () {
    this.domXML.find('.lat').html(this.position.nb);
    this.domXML.find('.lon').html(this.position.ob);
}

removeMarker = function () {
    this.setMap(null);
}