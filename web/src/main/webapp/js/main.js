function addToCartForm(id, url) {

    $.ajax({
        type: "POST",
        url: url,
        data: $("#" + id).serialize(),
        success: function (data, status, jqXHR) {
            document.getElementById("cartState").innerText = data;
            document.getElementById(id + "quantity").value = `1`;
            document.getElementById(id + "error").innerText = ``;
        },
        error: function (error) {
            document.getElementById(id + "error").innerText = error.responseText;
        }
    });
}