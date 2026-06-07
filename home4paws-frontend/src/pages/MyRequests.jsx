import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

function MyRequests() {

    const [requests, setRequests] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchRequests();
    }, []);

    const fetchRequests = async () => {

        try {

            const response =
                await api.get("/api/requests/my");

            setRequests(response.data);

        } catch (error) {

            console.error(error);

            alert("Failed to load requests");

        } finally {

            setLoading(false);

        }
    };

    if (loading) {
        return <h2>Loading requests...</h2>;
    }

    return (

        <div>

            <h2>My Adoption Requests</h2>

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
                                <strong>Status:</strong>
                                {" "}
                                {request.status}
                            </p>

                        </div>

                    ))

                )
            }

        </div>

    );
}

export default MyRequests;