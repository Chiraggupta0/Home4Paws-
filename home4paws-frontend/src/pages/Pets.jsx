import { useEffect, useState } from "react";
import api from "../api/axiosConfig";
import "../styles/Pets.css";

function Pets() {

    const [pets, setPets] = useState([]);

    useEffect(() => {
        fetchPets();
    }, []);

    const fetchPets = async () => {

        try {

            const response =
                await api.get("/api/pets");

            setPets(response.data);

        } catch (error) {

            console.error(error);

        }
    };

    return (

        <div className="pet-container">

            <h1>Available Pets</h1>

            <div className="pet-grid">

                {
                    pets.map((pet) => (

                        <div
                            key={pet.id}
                            className="pet-card"
                        >

                            <h3>{pet.name}</h3>

                            <p>
                                Breed: {pet.breed}
                            </p>

                            <p>
                                Age: {pet.age}
                            </p>

                            <div className="pet-actions">

                                <button
                                    onClick={() =>
                                        window.location.href =
                                            `/pets/${pet.id}`
                                    }
                                >
                                    View Details
                                </button>

                            </div>

                        </div>

                    ))
                }

            </div>

        </div>

    );
}

export default Pets;