// COnCEPT-Chat Mdoule v0.2 
//  Using SockJS & STOMP prorocol                                                                                                                                                                                                                                                                                             


//Define Stopm Client Variables
var stompClient;
var SOCKET_ENDPOINT = "/concept-chat";
var TOPIC_NAME;

/**
 * 
 * @param project_id The id of the project to connect to the chat
 * @returns void
 */
function activateChat() {
    var project_id = $("#projectID").val();
    if (project_id > 0) {
        logger("Activate Chat session for project with id: " + project_id)
        var messageList = $("#chat-list");
        // Define a connection to a new socket endpoint
        var socket = new SockJS(SOCKET_ENDPOINT);
        // Define topic name
        TOPIC_NAME = "/topic/project/" + project_id;
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            // Subscribe to the /topic/message endpoint
            stompClient.subscribe(TOPIC_NAME, function (data) {
                var message = data.body;
                message_parts = message.split("$@$");
                if (message_parts[0] === $("#chat-user").text()) {
                    messageList.append("<li  class=\"chat-message\">" + "<span class=\"chat-user-you\">" + message_parts[0] + " </span>" + message_parts[1] + "</li>");
                } else {
                    messageList.append("<li  class=\"chat-message\">" + "<span class=\"chat-user\">" + message_parts[0] + " </span>" + message_parts[1] + "</li>");
                }
            });
        });
    }
}

/*
 * Deactivate current session to COnCEPT-Chat
 * 
 * @returns {undefined}
 */
function deactivateChat() {
    if (undefined !== stompClient) {
        stompClient.disconnect(function () {
            logger("Disconnected from COnCEPT-Chat");
        });
    }
}

/**
 * 
 * @returns {undefined}
 */
function sendMessage() {
    var message = $("#chat-user").text() + "$@$" + $("#chat-message").val();
    logger("Sending message: " + message);
    $("#chat-message").val("");
    stompClient.send(TOPIC_NAME, {priority: 9}, message);
}



//Function to trigger Serach process when a user press enter on a text field   
function keyPressedOnTextField(e) {
    var key = e.keyCode || e.which;
    //On enter pressed send message
    if (key == 13) {
        sendMessage();
    }
}


function enableChat() {
    //Deactivated previous chat session
    deactivateChat();
    logger("Enabling COnCEPT-Chat...");
    $("#concept-chat").removeClass("disabled");
    $("#chat-list").empty();
    activateChat();
}

function disableChat() {
    logger("Disabling COnCEPT-Chat...");
    $("#concept-chat").addClass("disabled");
    $("#chat-list").empty();
    deactivateChat();
}