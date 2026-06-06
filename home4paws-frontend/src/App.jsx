import { BrowserRouter, Routes, Route } from "react-router-dom";

import Navbar from "./components/Navbar";

import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Pets from "./pages/Pets";

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
              element={<Pets />}
          />

        </Routes>

      </BrowserRouter>
  );
}

export default App;