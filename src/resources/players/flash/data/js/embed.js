if (!window.KorsakowGlobal)
    window.KorsakowGlobal = {};

function KorsakowEmbed(args) {
    var Global = window.KorsakowGlobal;

    document.write("<script type='text/javascript' src='"+args.baseUrl+"/data/js/swfobject.js'></scri"+"pt>");

    if (!Global._korsakowEmbedId)
        Global._korsakowEmbedId = 0;
    var id = "korsakow_film" + (++Global._korsakowEmbedId);

    var Local = Global[id] = {};
    var LocalRef = 'window.KorsakowGlobal["'+id+'"].';

    document.write("<div style='width: "+args.width+"px; height: "+args.height+"px;background-color: black;'><div id='"+id+"' style=''>Korsakow Film</div></div>");

    var _prevLoad = window.onload;
    window.onload = function()
    {
        if (_prevLoad) _prevLoad();

        var atts = {};
        var params = {};
        var flashvars = {};
        atts.name =
        atts.id = id;
        params.base = args.baseUrl + '/data';
        params.wmode = 'transparent'; // enables the html background to be seen, at a slight performance cost.
        params.allowScriptAccess = 'always';
        params.allowFullScreen = 'true';
        flashvars.externalBindings = 'network';
        flashvars.basePath = args.baseUrl + '/data/';
        flashvars.starter = args.snu;
        flashvars.onReady = LocalRef + 'korsakowReady';
        flashvars.onError = LocalRef + 'korsakowError';

        /*
        No longer sure why I'm bothering with the timeout.
        Sometimes java applets make the embed unhappy.
        */
        setTimeout(function() {
            swfobject.embedSWF(args.baseUrl + '/data/Embed.swf', id, args.width, args.height, '10.0', null, flashvars, params, atts);
        }, Global._korsakowEmbedId*50);
    };

    Local.onLinkButton = function()
    {
        window.location = args.baseUrl + '#?snu=' + args.snu;
    }
    Local.korsakowReady = function()
    {
        var env = document.getElementById(id);
        env.addEventListener("org.korsakow.player.widget.LinkButton.CLICK", LocalRef + 'onLinkButton');
    }
    Local.korsakowError = function(e)
    {
        alert('Error: ' + e);
    }

};
