import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";

function Login() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        email: "",
        password: ""
    });

    const handleChange = (e) => {

        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });

    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            const response = await api.post(
                "/api/auth/login",
                formData
            );

            localStorage.setItem(
                "token",
                response.data.token
            );

            localStorage.setItem(
                "role",
                response.data.role
            );

            alert("Login successful!");

            navigate("/pets");

        } catch (error) {

            console.error(error);

            alert(
                error.response?.data?.message ||
                "Login failed"
            );

        }

    };

    return (

        <div>

            <h2>Login</h2>

            <form onSubmit={handleSubmit}>

                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                />

                <br /><br />

                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                    required
                />

                <br /><br />

                <button type="submit">
                    Login
                </button>

            </form>

        </div>

    );
}

export default Login;