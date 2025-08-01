async function signUP() {

    const user_dto = {
        firstName: document.getElementById("firstName").value,
        lastName: document.getElementById("lastName").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
    };


    const response = await fetch(
            "SignUp",
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
        console.log(json);
        if (json.success) {
            Swal.fire({
                position: "center",
                icon: "success",
                title: "Verify Your Account",
                showConfirmButton: false,
                timer: 3000
            });
            window.location = "verify-account.html";

        } else {
            Swal.fire({
                title: "Registration failed",
                text: json.content,
                icon: "error"
            });
        }

    } else {
        Swal.fire({
            title: "Please try again Later ",
            text: json.content,
            icon: "error"
        });
    }
}
