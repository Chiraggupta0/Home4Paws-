import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

function ShelterRequests() {

    const [requests, setRequests] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchRequests();
    }, []);

    const fetchRequests = async () => {

        try {

            const response =
                await api.get("/api/requests/shelter");

            setRequests(response.data);

        } catch (error) {

            console.error(error);

            alert("Failed to load requests");

        } finally {

            setLoading(false);

        }
    };

    const updateStatus = async (id, status) => {

        try {

            await api.put(
                `/api/requests/${id}`,
                { status }
            );

            fetchRequests();

        } catch (error) {

            console.error(error);

            alert("Failed to update request");

        }
    };

    if (loading) {
        return <h2>Loading requests...</h2>;
    }

    return (

        <div>

            <h2>Shelter Requests</h2>

            {
                requests.length === 0 ? (

                    <p>No requests found.</p>

                ) : (

                    requests.map((request) => (

                        <div
                            key={request.id}
                            style={{
                                border: "1px solid black",
                                padding: "10px",
                                marginBottom: "10px"
                            }}
                        >

                            <h3>
                                {request.pet.name}
                            </h3>

                            <p>
                                <strong>Adopter:</strong>
                                {" "}
                                {request.adopter.name}
                            </p>

                            <p>
                                <strong>Status:</strong>
                                {" "}
                                {request.status}
                            </p>

                            {
                                request.status === "PENDING" && (

                                    <>
                                        <button
                                            onClick={() =>
                                                updateStatus(
                                                    request.id,
                                                    "APPROVED"
                                                )
                                            }
                                        >
                                            Approve
                                        </button>

                                        {" "}

                                        <button
                                            onClick={() =>
                                                updateStatus(
                                                    request.id,
                                                    "REJECTED"
                                                )
                                            }
                                        >
                                            Reject
                                        </button>
                                    </>

                                )
                            }

                        </div>

                    ))

                )
            }

        </div>

    );
}

export default ShelterRequests;