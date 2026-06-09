import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import "../styles/Auth.css";
import { Link } from "react-router-dom";

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

            //  the below are used to store them locally so that if the user refreshes page or open close browser then he in still signed in this is a part of jwt (this is view in inspect in network tab)

            localStorage.setItem(
                "token",
                response.data.token
            );

            localStorage.setItem(
                "role",
                response.data.role
            );

            alert("Login successful!");

            if (response.data.role === "SHELTER") {

                navigate("/my-dogs");

            } else {

                navigate("/pets");
            }

        } catch (error) {

            console.error(error);

            alert(
                error.response?.data?.message ||
                "Login failed"
            );

        }

    };

    return (

        <div className="auth-page">

            <div className="auth-card">

                <div className="brand">

                    <div className="brand-icon">
                        🐾
                    </div>

                    <h1>Home4Paws</h1>

                </div>

                <h2 className="auth-title">
                    Sign In
                </h2>

                <form onSubmit={handleSubmit}>

                    <div className="form-group">

                        <label>Email</label>

                        <input
                            type="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            placeholder="Enter email"
                        />

                    </div>

                    <div className="form-group">

                        <label>Password</label>

                        <input
                            type="password"
                            name="password"
                            value={formData.password}
                            onChange={handleChange}
                            placeholder="Enter password"
                        />

                    </div>

                    <button
                        type="submit"
                        className="submit-btn"
                    >
                        Sign In
                    </button>
                    <p className="register-link">
                        Don't have an account?{" "}
                        <Link to="/register">
                            Register
                        </Link>
                    </p>

                </form>

            </div>

        </div>

    );
}

export default Login;