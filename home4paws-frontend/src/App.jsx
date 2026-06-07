import { BrowserRouter, Routes, Route } from "react-router-dom";

import Navbar from "./components/Navbar";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Pets from "./pages/Pets";
import PetDetails from "./pages/PetDetails";
import MyRequests from "./pages/MyRequests";
import ShelterRequests from "./pages/ShelterRequests";
import ProtectedRoute from "./components/ProtectedRoute";
import AddPet from "./pages/AddPet";
import MyDogs from "./pages/MyDogs";

function App() {

  return (
      <BrowserRouter>

        <Navbar />

        <Routes>

          <Route
              path="/"
              element={<Home />}
          />

          <Route
              path="/login"
              element={<Login />}
          />

          <Route
              path="/register"
              element={<Register />}
          />

            <Route
                path="/pets"
                element={
                    <ProtectedRoute>
                        <Pets />
                    </ProtectedRoute>
                }
            />

            <Route
                path="/pets/:id"
                element={
                    <ProtectedRoute>
                        <PetDetails />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/my-requests"
                element={
                    <ProtectedRoute role="ADOPTER">
                        <MyRequests />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/shelter-requests"
                element={
                    <ProtectedRoute role="SHELTER">
                        <ShelterRequests />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/add-pet"
                element={
                    <ProtectedRoute role="SHELTER">
                        <AddPet />
                    </ProtectedRoute>
                }
            />
            <Route
                path="/my-dogs"
                element={
                    <ProtectedRoute role="SHELTER">
                        <MyDogs />
                    </ProtectedRoute>
                }
            />

        </Routes>

      </BrowserRouter>
  );
}

export default App;