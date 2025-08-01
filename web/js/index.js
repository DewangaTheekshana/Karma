async function CheckSignin() {

    const response = await fetch(
            "CheckSignin"
            );

    if (response.ok) {
        const json = await response.json();

        const response_DTO = json.response_DTO;

        if (response_DTO.success) {
            const user = response_DTO.content;
            
            let quickLink_custom = document.getElementById("quick-link-custom");

            let New_liTag = document.createElement("li");
//            let New_liTag1 = document.createElement("a");
//            New_liTag1.href = "#";
            New_liTag.innerHTML = "Welcome "+user.firstName;
            quickLink_custom.appendChild(New_liTag);
//            quickLink_custom.appendChild(New_liTag1);

            let login_btn = document.getElementById("st_login");
            login_btn.href = "signOut";
            login_btn.innerHTML = "Sign Out";

        } else {
            console.log("Not Sign in");
        }
        
        
        const productList = json.products;
        let i = 1;
        productList.forEach(product => {
            document.getElementById("st-title-" + i).innerHTML = product.title;
            document.getElementById("st-link-" + i).href = "single-product.html?id=" + product.id;
            document.getElementById("st-image-1").src = "product-images/1/image1.png";
            document.getElementById("st-image-2").src = "product-images/1/image2.png";
            document.getElementById("st-image-3").src = "product-images/1/image3.png";
            document.getElementById("st-price-" + i).innerHTML = new Intl.NumberFormat(
                    "en-US",
                    {
                        minimumFractionDigits: 2
                    }
            ).format(product.price);
            i++;
        });
    }
}


async  function  loadhomepageproduct() {

    let ProductHtml = document.getElementById("product");
    document.getElementById("product-main");

    const response = await fetch("LoadhomeProduct");
    if (response.ok) {
        const json = await response.json();
        
        console.log(json.productList);

        json.productList.forEach(item => {
//
            let ProductCloneHtml = ProductHtml.cloneNode(true);
//
            ProductCloneHtml.querySelector("#product-image1").src = "product-images//"+item.id+"//image1.png";
//            ProductCloneHtml.querySelector("#product-image1").src = "Product_Images/New_Karma_Product_" + item.id + "/New_Karma_Product_" + item.id + "_1.png";
            ProductCloneHtml.querySelector("#product-a1").href = "single-product.html?id=" + item.id;
//            ProductCloneHtml.querySelector("#product-a2").href = "single-product.html?id=" + item.id;
//            ProductCloneHtml.querySelector("#product-a3").href = "details.html?id=" + item.id;
            ProductCloneHtml.querySelector("#product-title").innerHTML = item.title;
//
            ProductCloneHtml.querySelector("#product-price").innerHTML = "Rs." + new Intl.NumberFormat(
                    "en-US", {
                        minimumFractionDigits: 2
                    }).format(item.price);




            ProductCloneHtml.querySelector("#add-to-cart-other").addEventListener(
                    "click", (e) => {
                addToCart(item.id, "1");
            }
            );

            document.getElementById("product-main").appendChild(ProductCloneHtml);

        });
    }else{
        console.log("error");
        
    }

}