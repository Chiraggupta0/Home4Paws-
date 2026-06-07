import { useState } from "react";
import api from "../api/axiosConfig";

function AddPet() {

    const [pet, setPet] = useState({
        name: "",
        breed: "",
        age: "",
        species: "",
        description: ""
    });

    const handleChange = (e) => {

        setPet({
            ...pet,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {

        e.preventDefault();

        try {

            await api.post(
                "/api/pets",
                pet
            );

            alert("Pet added successfully!");

            setPet({
                name: "",
                breed: "",
                age: "",
                species: "",
                description: ""
            });

        } catch (error) {

            console.error(error);

            alert("Failed to add pet");
        }
    };

    return (

        <div className="form-container">

            <h2>Add New Dog</h2>

            <form onSubmit={handleSubmit}>

                <input
                    type="text"
                    name="name"
                    placeholder="Dog Name"
                    value={pet.name}
                    onChange={handleChange}
                />

                <input
                    type="text"
                    name="breed"
                    placeholder="Breed"
                    value={pet.breed}
                    onChange={handleChange}
                />

                <input
                    type="number"
                    name="age"
                    placeholder="Age"
                    value={pet.age}
                    onChange={handleChange}
                />

                <input
                    type="text"
                    name="species"
                    placeholder="Species"
                    value={pet.species}
                    onChange={handleChange}
                />

                <textarea
                    name="description"
                    placeholder="Description"
                    value={pet.description}
                    onChange={handleChange}
                />

                <button type="submit">
                    Add Dog
                </button>

            </form>

        </div>
    );
}

export default AddPet;