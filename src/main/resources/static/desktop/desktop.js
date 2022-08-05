window.onload = function() {
    const socket = new SockJS("/jrd");
    stompClient = Stomp.over(socket);
    stompClient.debug = null;

    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const monIndex = urlParams.get("monIndex");
    var edit = true;


    /**
     *  Connects to the server and sends connect message -> initiates screen sharing protocol
     */
    stompClient.connect({},function(frame) {
        console.log("CONNECTED!");
        stompClient.subscribe("/user/desktop/screen", function(greet) {
            var gr = JSON.parse(greet.body);
            document.getElementById("screen").src = "data:image/jpg;base64," + gr.encodedMessage;
        });
        stompClient.send("/app/connect",{},JSON.stringify({monIndex:monIndex}))
    })

    window.onbeforeunload = function() {
        stompClient.disconnect()
    }

    document.getElementById("editBtn").onclick = function() {
        document.getElementById("editBtn").classList.add("hidden");
        document.getElementById("noEditBtn").classList.remove("hidden");
        edit = !edit;
    }

    document.getElementById("noEditBtn").onclick = function() {
        document.getElementById("editBtn").classList.remove("hidden");
        document.getElementById("noEditBtn").classList.add("hidden");
        edit = !edit;
    }

    var lastClick;

    document.getElementById("screen").ontouchstart= function(e) {
        e.preventDefault();

        if(!edit) {
            return;
        }

        var now = new Date().getTime();
        var timePassed = now - lastClick; 
        var count = 0;
        if((timePassed < 600) && (timePassed > 0)) {
            if(count == 0) {
                var element = document.getElementById("screen");
                var rect = element.getBoundingClientRect(); 
    
                var width = rect.right - rect.left;
                var height = rect.bottom - rect.top;
                stompClient.send("/app/mouse",{},JSON.stringify({type:"MOUSE_DOWN", percentageX: (e.touches[0].clientX-rect.left)/width , percentageY: (e.touches[0].clientY-rect.top)/height, index:monIndex}));
                count++;
            } else if(count == 1) {
                count = 0;
                {
                    var element = document.getElementById("screen");
                    var rect = element.getBoundingClientRect(); 
    
                    var width = rect.right - rect.left;
                    var height = rect.bottom - rect.top;
                    stompClient.send("/app/mouse",{},JSON.stringify({type:"MOUSE_UP", percentageX: (e.touches[0].clientX-rect.left)/width , percentageY: (e.touches[0].clientY-rect.top)/height, index:monIndex}));
                }
                {
                    var element = document.getElementById("screen");
                    var rect = element.getBoundingClientRect(); 
    
                    var width = rect.right - rect.left;
                    var height = rect.bottom - rect.top;
                    stompClient.send("/app/mouse",{},JSON.stringify({type:"MOUSE_DOWN", percentageX: (e.touches[0].clientX-rect.left)/width , percentageY: (e.touches[0].clientY-rect.top)/height, index:monIndex}));
                }
            }
        } else {
            var element = document.getElementById("screen");
            var rect = element.getBoundingClientRect(); 
    
            var width = rect.right - rect.left;
            var height = rect.bottom - rect.top;
            stompClient.send("/app/mouse",{},JSON.stringify({type:"MOVE", percentageX: (e.touches[0].clientX-rect.left)/width , percentageY: (e.touches[0].clientY-rect.top)/height, index:monIndex}));
            count = 0;
        }

        lastClick = new Date().getTime();
    }

    document.getElementById("screen").ontouchmove= function(e) {
        e.preventDefault();

        if(!edit) {
            return;
        }

        var element = document.getElementById("screen");
        var rect = element.getBoundingClientRect(); 

        var width = rect.right - rect.left;
        var height = rect.bottom - rect.top;
        stompClient.send("/app/mouse",{},JSON.stringify({type:"MOVE", percentageX: (e.touches[0].clientX-rect.left)/width , percentageY: (e.touches[0].clientY-rect.top)/height, index:monIndex}));
    }

    document.getElementById("screen").ontouchend = function(e) {
        e.preventDefault();

        if(!edit) {
            return;
        }

        stompClient.send("/app/mouse",{},JSON.stringify({type:"MOUSE_UP", percentageX: 0 , percentageY: 0, index:monIndex}));
    }
}