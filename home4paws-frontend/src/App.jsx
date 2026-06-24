import { BrowserRouter, Routes, Route, useLocation } from 'react-router-dom';
import { AnimatePresence, motion } from 'framer-motion';

import Navbar        from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';

import Home            from './pages/Home';
import Login           from './pages/Login';
import Register        from './pages/Register';
import Pets            from './pages/Pets';
import PetDetails      from './pages/PetDetails';
import MyRequests      from './pages/MyRequests';
import ShelterRequests from './pages/ShelterRequests';
import AddPet          from './pages/AddPet';
import MyDogs          from './pages/MyDogs';
import Chat            from './pages/Chat';
import Subscribe       from './pages/Subscribe';

const pageVariants = {
  initial: { opacity: 0, y: 12 },
  animate: { opacity: 1, y: 0, transition: { duration: 0.35, ease: [0.16, 1, 0.3, 1] } },
  exit:    { opacity: 0, y: -8,  transition: { duration: 0.2 } },
};

function AnimatedRoutes() {
  const location = useLocation();
  return (
    <AnimatePresence mode="wait">
      <motion.div key={location.pathname} variants={pageVariants} initial="initial" animate="animate" exit="exit">
        <Routes location={location}>
          <Route path="/"  element={<Home />} />
          <Route path="/login"    element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/pets" element={<Pets />} />
          <Route path="/pets/:id" element={<ProtectedRoute><PetDetails /></ProtectedRoute>} />
          <Route path="/my-requests" element={<ProtectedRoute role="NORMAL_USER"><MyRequests /></ProtectedRoute>} />
          <Route path="/shelter-requests" element={<ProtectedRoute role="NGO_SHELTER"><ShelterRequests /></ProtectedRoute>} />
          <Route path="/add-pet" element={<ProtectedRoute role="NGO_SHELTER"><AddPet /></ProtectedRoute>} />
          <Route path="/my-dogs" element={<ProtectedRoute role="NGO_SHELTER"><MyDogs /></ProtectedRoute>} />
          <Route path="/seller/add-pet" element={<ProtectedRoute role="SELLER"><AddPet /></ProtectedRoute>} />
          <Route path="/seller/my-pets" element={<ProtectedRoute role="SELLER"><MyDogs /></ProtectedRoute>} />
          <Route path="/chat/:requestId" element={<ProtectedRoute><Chat /></ProtectedRoute>} />
          <Route path="/subscribe" element={<ProtectedRoute><Subscribe /></ProtectedRoute>} />
        </Routes>
      </motion.div>
    </AnimatePresence>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <Navbar />
      <AnimatedRoutes />
    </BrowserRouter>
  );
}
