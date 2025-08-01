async function verifyAccount() {
    const dto = {
        verification: document.getElementById("verification").value
    };

    const response = await fetch(
            "Verification",
            {
                method: "POST",
                body: JSON.stringify(dto),
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
                title: "Verify Your Account",
                showConfirmButton: false,
                timer: 3000
            });
            window.location = "login.html";
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
