import "../styles/Navbar.css";
import { Link, useNavigate } from "react-router-dom";

function Navbar() {

    const navigate = useNavigate();

    const token = localStorage.getItem("token");
    const role = localStorage.getItem("role");

    const handleLogout = () => {

        localStorage.removeItem("token");
        localStorage.removeItem("role");

        alert("Logged out successfully!");

        navigate("/login");
    };

    return (
        <nav className="navbar">

            <div className="logo">

                <div className="logo-box">
                    🐾
                </div>

                <h1>Home4Paws</h1>

            </div>

            <div className="nav-links">

                {/* ADOPTER MENU */}
                {
                    role === "ADOPTER" && (
                        <>
                            <Link to="/pets">
                                Pet Dogs
                            </Link>

                            <Link to="/my-requests">
                                My Requests
                            </Link>
                        </>
                    )
                }

                {/* SHELTER MENU */}
                {role === "SHELTER" && (
                    <>
                        <Link to="/add-pet">
                            Add Dog
                        </Link>

                        <Link to="/my-dogs">
                            My Dogs
                        </Link>

                        <Link to="/shelter-requests">
                            Adoption Requests
                        </Link>
                    </>
                )}

                {/* COMMON LINKS */}
                <Link to="/">
                    NGOs
                </Link>

                <Link to="/">
                    Subscribe
                </Link>

            </div>

            {
                token
                    ? (
                        <button
                            className="auth-btn"
                            onClick={handleLogout}
                        >
                            Logout
                        </button>
                    )
                    : (
                        <Link
                            className="auth-btn"
                            to="/login"
                        >
                            Sign In
                        </Link>
                    )
            }

        </nav>
    );
}

export default Navbar;