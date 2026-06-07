import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";
import "../styles/Auth.css";

function Register() {

    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        name: "",
        email: "",
        password: "",
        role: "ADOPTER"
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
                "/api/auth/register",
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

            alert("Registration successful!");

            navigate("/pets");

        } catch (error) {

            console.error(error);

            alert(
                error.response?.data?.message ||
                "Registration failed"
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

                    <h1>
                        Home4Paws
                    </h1>

                </div>

                <h2 className="auth-title">
                    Create your account
                </h2>

                <form onSubmit={handleSubmit}>

                    <div className="form-group">

                        <label>
                            Full Name
                        </label>

                        <input
                            type="text"
                            name="name"
                            placeholder="Enter your name"
                            value={formData.name}
                            onChange={handleChange}
                        />

                    </div>

                    <div className="form-group">

                        <label>
                            Email
                        </label>

                        <input
                            type="email"
                            name="email"
                            placeholder="Enter your email"
                            value={formData.email}
                            onChange={handleChange}
                        />

                    </div>

                    <div className="form-group">

                        <label>
                            Password
                        </label>

                        <input
                            type="password"
                            name="password"
                            placeholder="Enter password"
                            value={formData.password}
                            onChange={handleChange}
                        />

                    </div>

                    <div className="form-group">

                        <label>
                            Role
                        </label>

                        <select
                            name="role"
                            value={formData.role}
                            onChange={handleChange}
                        >
                            <option value="ADOPTER">
                                Adopter
                            </option>

                            <option value="SHELTER">
                                Shelter
                            </option>

                        </select>

                    </div>

                    <button
                        type="submit"
                        className="submit-btn"
                    >
                        Sign Up
                    </button>

                </form>

            </div>

        </div>

    );
}

export default Register;