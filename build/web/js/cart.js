

async  function loadCartItems() {
    const response = await  fetch(
            "LoadCartItems",
            );

    if (response.ok) {
        const json = await response.json();

        console.log(json);

        let cartProduct = document.getElementById("cart-row");
        document.getElementById("cart-table").innerHTML="";
        

        let  sub_total = 0;
        let  sub_sipping = 0;

        json.forEach(item => {

            console.log(item.qty);
            let productClone = cartProduct.cloneNode(true);

            //change other tags
            productClone.querySelector("#cart-product-title").innerHTML = item.product.title;
            productClone.querySelector("#cart-item-id").innerHTML = item.product.id;
            productClone.querySelector("#cart-product-img").src = "product-images//" + item.product.id + "//image1.png";
            productClone.querySelector("#cart-product-price").innerHTML = "Rs." + item.product.price;
            productClone.querySelector("#cart-product-size").innerHTML = item.product.size.name;
            productClone.querySelector("#cart-product-qty").value = item.qty;

            let item_sub_total = item.product.price * item.qty;
            productClone.querySelector("#cart-product-total").innerHTML = "Rs." + item_sub_total;
            sub_total += item_sub_total;
            sub_sipping+= item.product.shipping;



            
//                alert(item.product.price * item.qty);



            document.getElementById("cart-table").appendChild(productClone);
            document.getElementById("redda_asd").innerHTML="Rs."+ sub_total;
            document.getElementById("redda_shi").innerHTML="Rs."+ sub_sipping;
            
            
        });
        document.getElementById("redda_asdss").innerHTML= sub_sipping + sub_total;
//        alert(sub_total);

    } else {
        console.log("error");
    }
}

async  function removefromCart() {

    const id = document.getElementById("cart-item-id").innerText;

    const response = await fetch(
            "removefromCart?id=" + id

            );

    if (response.ok) {

        const json = await response.json();

        if (json.success) {
            Swal.fire({
                title: 'Success!',
                text: json.content,
                icon: 'success',
                confirmButtonText: 'Cool'
            }).then((result) => {
                // If 'Cool' button is clicked, reload the page
                if (result.isConfirmed) {
                    window.location.reload();// Reload the page
                }
            });

        } else {
            Swal.fire({
                title: 'Error!',
                text: json.content,
                icon: 'error',
                confirmButtonText: 'Cool'
            })
        }

    } else {

    }
}
