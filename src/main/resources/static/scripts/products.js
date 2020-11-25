//a script to validate all the inputs when they get clicked out
$('input').blur(function(evt) {
    evt.target.checkValidity();
});

//function called when the "Delete" button is pressed
function deleteProduct(productId)
{
    $.ajax({
        type: 'DELETE',
        url: '/products/' + productId,
        success: function (result) {
            $("body").html(result);
        },
        error: function (e) {
            $("body").html(e.responseText);
        }
    });
}

//function to send a PUT request to save the changes for a product
function saveProduct(productId)
{
    let inputValidationsPassed = 0;

    $('input').each(function(index, item) {
        if(item.checkValidity())
            inputValidationsPassed++;
    });

    if(inputValidationsPassed === $('input').length)
    {
        const dataToBeSent = {
            name: document.getElementById("name").value,
            quantity: document.getElementById("quantity").value,
            criticalQuantity: document.getElementById("criticalQuantity").value,
            pricePerItem: document.getElementById("pricePerItem").value
        };

        $.ajax({
            type: 'PUT',
            url: '/products/' + productId,
            data: JSON.stringify(dataToBeSent),
            contentType: "application/json",
            success: function (result) {
                $("body").html(result);
            },
            error: function (e) {
                $("body").html(e.responseText);
            }
        });
    }
}