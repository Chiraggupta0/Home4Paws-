import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

function MyDogs() {

    const [dogs, setDogs] = useState([]);

    useEffect(() => {
        fetchDogs();
    }, []);

    const fetchDogs = async () => {

        try {

            const response =
                await api.get(
                    "/api/pets/my-pets"
                );

            setDogs(response.data);

        } catch (error) {

            console.error(error);
        }
    };

    return (

        <div className="pet-container">

            <h1>My Dogs</h1>

            <div className="pet-grid">

                {
                    dogs.map((dog) => (

                        <div
                            key={dog.id}
                            className="pet-card"
                        >

                            <h3>
                                {dog.name}
                            </h3>

                            <p>
                                Breed: {dog.breed}
                            </p>

                            <p>
                                Age: {dog.age}
                            </p>

                            <p>
                                Status: {dog.status}
                            </p>

                        </div>

                    ))
                }

            </div>

        </div>
    );
}

export default MyDogs;