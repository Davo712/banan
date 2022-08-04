var globalData = [];
let username = $('#username').text();
$.ajax({
    url: 'http://localhost:8080/getMessages/' + username,
    dataType: "json",
    method: 'GET',
    success: function s (data) {
        globalData = data;
        for (let i = 0; i < data.length; i++) {
            $('#myDiv').append("<br /><br />&nbsp;&nbsp;" + data[i].messageText + "    :from " + data[i].messageFrom);
        }
    }
});


function worker() {
    $.ajax({
        url: 'http://localhost:8080/getMessages/' + username ,
        dataType: "json",
        method: 'GET',
        success: function s (data) {
            if (globalData.length == data.length) {
            } else {
                for (let i = globalData.length; i < data.length; i++) {
                    $('#myDiv').append("<br /><br />&nbsp;&nbsp;" + data[i].messageText + "    :from " + data[i].messageFrom);
                }
                globalData = data;
            }
        },
        complete: function() {
            setTimeout(worker, 2000);
        }
    });
};
worker();
