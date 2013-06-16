

(function() { // namespace

// private
var _uniqueIdSeed = new Date().getTime();
function uniqueId() {
    return ++_uniqueIdSeed;
}
var _ready = false;
var _id;

// public
var $k = window.$k = {
    id: null,
    env: null,
    // methods

    //public
    bindenv: function(type, callback) {
       var name = '$_Korsakow_callback' + uniqueId();
       window[name] = function() {
           try {
               callback.apply(this, arguments);
//               delete window[name];
           } catch (e) {
               alert('Korsakow EXT Event handler error: ' + e);
           }
       };
       this.env.addEventListener(type, name);
    },
    // public
    bind: function(target, method, callback) {
        target[method] = (function(prev) {
            try {
                return function(id) {
                    if (prev)
                        prev.apply(target, arguments);
                    if (callback)
                        callback.apply(target, arguments);
                };
            } catch (e) {
                alert('Korsakow JS Event handler error: ' + e);
            }
        })(target[method]);
    },
    // public
    ready: function(callback) {
        if (_ready)
            callback.apply(window, [_id]);
        else
            this.bind(window, '$_KorsakowReady_', arguments[0]);
    },
    // public
    error: function(callback) {
        this.bind(window, '$_KorsakowError_', arguments[0]);
    },
    /**
        @public
        @param String q & separated list of name=value pairs, posssibly prefixed with a ?
        @return [{
            name,
            value
        }]
    */
    joinQueryString: function(q)
    {
        var array = [];
        if (q.length && q.charAt(0) == '?')
            q = q.substring(1);
        var bits = q.split("&");
        for (var i = 0; i < bits.length; ++i) {
            var nv = bits[i].split('=');
            array.push({
                name: nv[0],
                value: nv[1]
            });
        }
        return array;
    }
} // end $k

// main

$k.ready(function(id) {
    _id = id;
    _ready = true;

    $k.id = id;
    $k.env = $('#swf_container')[0];
    $k.bindenv("org.korsakow.player.widget.ShareButton.CLICK", function() {
        showEmbedOverlay();
    });
    $k.bindenv("org.korsakow.player.engine.event.EnvironmentEvent.SNU_BEGIN_PLAYING", function() {
        var snuId = $k.env.getCurrentSnuId();
        if (window['SWFAddress'])
            SWFAddress.setValue('?snu='+snuId);
    });
});
$k.error(function(msg) {
    alert('Error: ' + e);
});

$(function() {
    $.getScript('data/plugins/korsakow.host.js');
});

})(); // end namespace

$k.EnvironmentEvent = {
    SNU_BEGIN_PLAYING: 'org.korsakow.player.engine.event.EnvironmentEvent.SNU_BEGIN_PLAYING',
    SNU_FINISH_PLAYING: 'org.korsakow.player.engine.event.EnvironmentEvent.SNU_FINISH_PLAYING',
    ENDFILM: 'org.korsakow.player.engine.event.EnvironmentEvent.ENDFILM',
    PAUSE: 'org.korsakow.player.engine.event.EnvironmentEvent.PAUSE',
    RESUME: 'org.korsakow.player.engine.event.EnvironmentEvent.RESUME'
};


$(function() {
    if (window['SWFAddress']) {
        function onSWFAddressChange(event)
        {
            if (!$k.env)
                return;
            if (!SWFAddress.getParameter('snu'))
                return;
            var snu = SWFAddress.getParameter('snu');
            var ret = $k.env.executeSnu(snu);
        }
        SWFAddress.addEventListener('change', onSWFAddressChange);
    }

    $('#embedOverlay').add('#embedOverlay input').add('#embedOverlay textarea').bind('keydown', onEmbedOverlayKeydown);
});
function showEmbedOverlay()
{
    if ($k.env) {
        var snuId = $k.env.getCurrentSnuId();
        var mainMedia = $k.env.getCurrentMainMedia();

        var baseUrl = window.location.toString();

        var baseUrl = (function() {
            var url = $('#korsakow_js').attr('src');
            var index = url.lastIndexOf('/');
            if (index != -1)
                url = url.substring(0, index);
            return url;
        })();

        $('#shareSNU').attr('disabled', false)
            .removeClass('disabled')
            .val(baseUrl + "#?snu=" + snuId);
        $('#shareFilm').val(baseUrl);
        var args = [
            'snu:"'+snuId+'"',
            'width:"'+mainMedia.width+'"',
            'height:"'+mainMedia.height+'"',
            'baseUrl:"'+(baseUrl)+'"'
        ];
        $('#embedCode').attr('disabled', false)
            .removeClass('disabled')
            .text('<script type="text/javascript" src="'+baseUrl+'/data/js/embed.js"></script>\n<script type="text/javascript">KorsakowEmbed({'+args.join(',')+'});</script>');

        $k.env.pauseEngine();
    } else {
        $('#shareSNU').attr('disabled', true)
            .addClass('disabled')
            .val('');
        $('#embedCode').attr('disabled', true)
            .addClass('disabled')
            .text('');
    }
    $('#embedOverlay').show();
    $('#embedOverlayBackground').show();
    $('#embedOverlay input').eq(0).focus();
}
function closeEmbedOverlay()
{
    $('#embedOverlay').hide();
    $('#embedOverlayBackground').hide();
    if ($k.env) $k.env.resumeEngine();
}
function onEmbedOverlayKeydown(event)
{
    switch (event.which)
    {
    case 27: closeEmbedOverlay();
        break;
    }
}
