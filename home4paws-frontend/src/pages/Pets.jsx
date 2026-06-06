import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

function Pets() {

    const [pets, setPets] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchPets();
    }, []);

    const fetchPets = async () => {

        try {

            const response = await api.get("/api/pets");

            setPets(response.data);

        } catch (error) {

            console.error(error);

            alert("Failed to load pets");

        } finally {

            setLoading(false);

        }
    };

    if (loading) {
        return <h2>Loading pets...</h2>;
    }

    return (

        <div>

            <h2>Available Pets</h2>

            {
                pets.length === 0 ? (

                    <p>No pets available.</p>

                ) : (

                    pets.map((pet) => (

                        <div
                            key={pet.id}
                            style={{
                                border: "1px solid black",
                                padding: "10px",
                                marginBottom: "10px"
                            }}
                        >

                            <h3>{pet.name}</h3>

                            <p>
                                <strong>Species:</strong> {pet.species}
                            </p>

                            <p>
                                <strong>Breed:</strong> {pet.breed}
                            </p>

                            <p>
                                <strong>Age:</strong> {pet.age}
                            </p>

                            <p>
                                <strong>Description:</strong> {pet.description}
                            </p>

                            <p>
                                <strong>Status:</strong> {pet.status}
                            </p>

                        </div>

                    ))

                )
            }

        </div>

    );
}

export default Pets;