var brandList;

async  function loadFeatues() {


    const response = await  fetch(
            "LoadFeaturs"

            );

    if (response.ok) {
        const json = await  response.json();
        
        const categoryList = json.categoryList;
        brandList = json.brandList;
        const sizeList = json.sizeList;


        loadSelect(categoryList, "category-select", ["id", "name"]);
        loadSelect(brandList, "brand-select", ["id", "name"]);
        loadSelect(sizeList, "size-select", ["id", "name"]);


    } else {
        console.log("error");
    }


}


function loadSelect(list, elementId, propertyArray) {
    const element = document.getElementById(elementId);
    list.forEach(item => {
        let optionTag = document.createElement("option");
        optionTag.value = item[propertyArray[0]];
        optionTag.innerHTML = item[propertyArray[1]];
        element.appendChild(optionTag);
    });
}


async  function productListing() {
    const categorySelect = document.getElementById("category-select");
    const brandSelect = document.getElementById("brand-select");
    const titleTag = document.getElementById("title");
    const descriptiomTag = document.getElementById("description");
    const sizeSelect = document.getElementById("size-select");
    const priceTag = document.getElementById("price");
    const qtyTag = document.getElementById("qty");
    const image1Tag = document.getElementById("image1");
    const image2Tag = document.getElementById("image2");
    const image3Tag = document.getElementById("image3");

    const data = new FormData();
    data.append("categoryId", categorySelect.value);
    data.append("brandId", brandSelect.value);
    data.append("titleId", titleTag.value);
    data.append("descriptionId", descriptiomTag.value);
    data.append("sizeId", sizeSelect.value);
    data.append("price", priceTag.value);
    data.append("qty", qtyTag.value);
    data.append("image1", image1Tag.files[0]);
    data.append("image2", image2Tag.files[0]);
    data.append("image3", image3Tag.files[0]);

    const response = await fetch(
            "ProductListing",
            {
                method: "POST",
                body: data
            }
    );

    if (response.ok) {

        const json = await response.json();
        console.log(json.content);
        if (json.success) {

        } else {
            if (json.success) {
                categorySelect.value = 0;
                titleTag.value = "";
                image1Tag.value = null;
            } else {

                Swal.fire({
                    title: 'Error!',
                    text: json.content,
                    icon: 'error',
                    confirmButtonText: 'Cool'
                });

            }
        }

    } else {
        console.log("Error");
    }
}