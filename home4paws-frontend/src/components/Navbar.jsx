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

                <h2>Home4Paws</h2>

            </div>

            <div className="nav-links">

                {/* ADOPTER MENU */}
                {
                    role === "NORMAL_USER" && (
                        <>
                            <Link to="/pets">
                                Browse Pets
                            </Link>

                            <Link to="/my-requests">
                                My Requests
                            </Link>
                        </>
                    )
                }

                {/* SHELTER MENU */}
                {role === "NGO_SHELTER" && (
                    <>
                        <Link to="/add-pet">
                            Add Pet
                        </Link>

                        <Link to="/my-dogs">
                            My Pets
                        </Link>

                        <Link to="/shelter-requests">
                            Adoption Requests
                        </Link>
                    </>
                )}


                {/*<Link to="/">*/}
                {/*    NGOs*/}
                {/*</Link>*/}

                {/*<Link to="/">*/}
                {/*    Subscribe*/}
                {/*</Link>*/}

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