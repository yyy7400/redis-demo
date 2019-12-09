
    $.ajax({
        type: "GET",
        url:  "/redis/get?key=123",
        cache: false,
        dataType: "json",
        success: function (data) {
            $("#get").html(data);
        }
    });

    $.ajax({
        type: "POST",
        url: "/redis/post",
        data: JSON.stringify([1,2,3,4,5]),
        dataType: "json",
        contentType: "application/json",
        success: function (data) {
            $("#post").html(data);
        }
    });