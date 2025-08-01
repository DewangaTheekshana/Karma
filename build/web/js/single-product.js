async  function loadProduct() {

    const parameters = new URLSearchParams(window.location.search);

    if (parameters.has("id")) {
        const productId = parameters.get("id");

        const response = await  fetch("LoadToSingleProduct?id=" + productId);
        if (response.ok) {
            const json = await response.json();  // Assuming response is fetched correctly
            console.log(json);

            const id = json.Product.id;  // Check if Product and id exist
            if (id) {
                // Construct image paths
                const image1Path = "product-images/" + id + "/image1.png";
                const image2Path = "product-images/" + id + "/image2.png";
                const image3Path = "product-images/" + id + "/image3.png";

                // Debug the paths
                console.log(image1Path);
                console.log(image2Path);
                console.log(image3Path);

                // Update image sources
                document.getElementById("image1").src = image1Path;
                document.getElementById("image2").src = image2Path;
                document.getElementById("image3").src = image3Path;

                // Add fallback in case images don't load
                document.getElementById("image1").onerror = function () {
                    this.src = "path/to/fallback_image1.png";  // Fallback image
                };
                document.getElementById("image2").onerror = function () {
                    this.src = "path/to/fallback_image2.png";
                };
                document.getElementById("image3").onerror = function () {
                    this.src = "path/to/fallback_image3.png";
                };
            } else {
                console.error("Invalid product ID");
            }


            document.getElementById("product-title").innerHTML = json.Product.title;
//            document.getElementById("product-publish-date").innerHTML = json.product.date_time;

            let price = json.Product.price;
            document.getElementById("product-price").innerHTML = "Rs." + new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format(price);

//                    alert(json.);
            document.getElementById("product-category").innerHTML = json.Product.category.name;
            document.getElementById("product-size").innerHTML = json.Product.size.name;
            document.getElementById("product-qty").innerHTML = json.Product.qty;
            document.getElementById("product-shipping").innerHTML = "Rs." + new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format(json.Product.shipping);

            document.getElementById("product-description").innerHTML = json.Product.description;
            document.getElementById("product-description2").innerHTML = json.Product.description;

            if (json.Product.qty == "0") {
                let addToCartBtn = document.getElementById("add-to-cart-main");

                // Disable the button by adding a class or style
                addToCartBtn.classList.add("disabled"); // Optionally, use a class to style it as disabled
                addToCartBtn.style.pointerEvents = "none"; // Prevent clicking
                addToCartBtn.style.opacity = "0.5"; // Dim the button visually
                addToCartBtn.innerHTML = "Out of Stock"; // Update the text if needed
            }


//add to cart main product////////
            document.getElementById("add-to-cart-main").addEventListener(
                    "click", (e) => {
                let qty = document.getElementById("add-to-cart-qty").value;
                alert(qty);
                if (qty >= 1) {
                    addToCart(json.Product.id, qty);
                } else {
                    Swal.fire({
                        title: 'Error!',
                        text: "Quentity can't be minusasdasd",
                        icon: 'error',
                        confirmButtonText: 'Cool'
                    })
                }

            }
            );

            //similer product clone

//            let ProductHtml = document.getElementById("similer-product");
//            document.getElementById("similer-product-main").innerHTML = "";
//
//            json.productList.forEach(item => {
//
//
//                let ProductCloneHtml = ProductHtml.cloneNode(true);
//
//                ProductCloneHtml.querySelector("#similer-product-image1").src = "product_images/" + item.id + "/image.jpg";
//                ProductCloneHtml.querySelector("#similer-product-a1").href = "single-product.html?id=" + item.id;
//                ProductCloneHtml.querySelector("#similer-product-a2").href = "single-product.html?id=" + item.id;
//                ProductCloneHtml.querySelector("#similer-product-title").innerHTML = item.title;
//
//                ProductCloneHtml.querySelector("#similer-product-price").innerHTML = "Rs." + new Intl.NumberFormat(
//                        "en-US", {
//                            minimumFractionDigits: 2
//                        }).format(item.price);
//
//
//
//
//                ProductCloneHtml.querySelector("#add-to-cart-other").addEventListener(
//                        "click", (e) => {
//                    addToCart(item.id, "1");
//                }
//                );
//
//                document.getElementById("similer-product-main").appendChild(ProductCloneHtml);
//
//
//            });

//           


        } else {
//            window.location = "index.html"

        }

    } else {
//        window.location = "index.html"
    }
}

async  function addToCart(id, qty) {



    const response = await fetch(
            "AddToCart?id=" + id + "&qty=" + qty

            );

    if (response.ok) {

        const json = await response.json();

        if (json.success) {
            Swal.fire({
                title: 'Success!',
                text: json.content,
                icon: 'succses',
                confirmButtonText: 'Cool'
            })
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
