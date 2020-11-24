function editProduct(productId)
{
    //GET request to a page where the user will edit the product
}

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