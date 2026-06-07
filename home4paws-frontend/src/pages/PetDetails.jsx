import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import api from "../api/axiosConfig";

import { useNavigate } from "react-router-dom";

function PetDetails() {

    const { id } = useParams();
    const navigate = useNavigate();

    const [pet, setPet] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchPet();
    }, []);

    const fetchPet = async () => {

        try {

            const response = await api.get(
                `/api/pets/${id}`
            );

            setPet(response.data);

        } catch (error) {

            console.error(error);

            alert("Failed to load pet");

        } finally {

            setLoading(false);

        }
    };
    const sendRequest = async () => {

        try {

            await api.post(
                `/api/requests/${pet.id}`
            );

            alert(
                "Adoption request submitted successfully!"
            );

            navigate("/pets");

        } catch (error) {

            console.error(error);

            alert(
                error.response?.data?.message ||
                "Failed to send request"
            );
        }
    };

    if (loading) {
        return <h2>Loading pet...</h2>;
    }

    if (!pet) {
        return <h2>Pet not found</h2>;
    }

    return (

        <div>

            <h1>{pet.name}</h1>

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

            {
                localStorage.getItem("role") === "ADOPTER" && (

                    <button onClick={sendRequest}>
                        Adopt Me
                    </button>

                )
            }
        </div>

    );
}

export default PetDetails;