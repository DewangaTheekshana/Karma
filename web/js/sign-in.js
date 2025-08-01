async function signIn() {

    const user_dto = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
    };


    const response = await fetch(
            "SignIn",
            {
                method: "POST",
                body: JSON.stringify(user_dto),
                headers: {
                    "Content-Type": "application/json"
                }
            }
    );

    if (response.ok) {
        const json = await response.json();

        if (json.success) {
            Swal.fire({
                position: "center",
                icon: "success",
                showConfirmButton: false,
                timer: 1500
            });

            window.location = "index.html";

        } else {
            if (json.content === "Unverified") {
                Swal.fire({
                    title: "You need to Verify your Account",
                    icon: "info",
                    timer: 10000
                });
                window.location = "verify-account.html";

            } else {
                Swal.fire({
                    title: "Oops..",
                    text: json.content,
                    icon: "error"
                });
            }
        }

    } else {
        Swal.fire({
            title: "Please try again Later ",
            icon: "error"
        });
    }
}
