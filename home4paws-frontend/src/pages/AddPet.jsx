import { useState } from 'react';
import { motion } from 'framer-motion';
import api from '../api/axiosConfig';

const EMPTY = { name: '', breed: '', age: '', species: '', description: '' };

export default function AddPet() {
  const [pet, setPet]       = useState(EMPTY);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError]     = useState('');

  const handleChange = e => setPet({ ...pet, [e.target.name]: e.target.value });

  const handleSubmit = async e => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess(false);
    try {
      await api.post('/api/pets', pet);
      setSuccess(true);
      setPet(EMPTY);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to add pet. Try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-wrapper">
      <div style={{ background: 'linear-gradient(135deg,#2C1810 0%,#6B3422 60%,#9B4E20 100%)', padding: 'clamp(60px,8vw,100px) 0 clamp(40px,5vw,60px)' }}>
        <div className="container">
          <motion.div initial={{opacity:0,y:20}} animate={{opacity:1,y:0}} transition={{duration:.5}}>
            <p className="section-eyebrow" style={{color:'var(--accent)'}}>➕ Shelter</p>
            <h1 style={{color:'#fff', fontSize:'clamp(2rem,4vw,2.8rem)', fontFamily:"'Playfair Display',serif", marginTop:8}}>
              Add a New Pet
            </h1>
            <p style={{color:'rgba(255,255,255,.6)', marginTop:8}}>
              List a pet for adoption and help them find a forever home.
            </p>
          </motion.div>
        </div>
      </div>

      <div className="container" style={{paddingTop:48, paddingBottom:80, maxWidth:640}}>
        <motion.div
          className="card"
          style={{ padding: 'clamp(28px,5vw,48px)' }}
          initial={{ opacity: 0, y: 24 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.1 }}
        >
          {error && <div className="alert alert-error">⚠️ {error}</div>}
          {success && (
            <motion.div
              className="alert alert-success"
              initial={{ opacity: 0, scale: 0.95 }} animate={{ opacity: 1, scale: 1 }}
            >
              🎉 Pet added successfully! Ready for adoption.
            </motion.div>
          )}

          <form onSubmit={handleSubmit} style={{ display:'flex', flexDirection:'column', gap:4 }}>
            <div className="form-group">
              <label className="form-label" htmlFor="name">Pet Name *</label>
              <input id="name" name="name" type="text" className="form-input" placeholder="e.g. Buddy" value={pet.name} onChange={handleChange} required />
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="species">Species *</label>
              <select id="species" name="species" className="form-input form-select" value={pet.species} onChange={handleChange} required>
                <option value="">Select species</option>
                <option value="Dog">Dog</option>
                <option value="Cat">Cat</option>
                <option value="Other">Other</option>
              </select>
            </div>

            <div style={{display:'grid', gridTemplateColumns:'1fr 1fr', gap:16}}>
              <div className="form-group">
                <label className="form-label" htmlFor="breed">Breed</label>
                <input id="breed" name="breed" type="text" className="form-input" placeholder="e.g. Labrador" value={pet.breed} onChange={handleChange} />
              </div>
              <div className="form-group">
                <label className="form-label" htmlFor="age">Age (years)</label>
                <input id="age" name="age" type="number" min="0" max="30" className="form-input" placeholder="e.g. 2" value={pet.age} onChange={handleChange} />
              </div>
            </div>

            <div className="form-group">
              <label className="form-label" htmlFor="description">Description</label>
              <textarea
                id="description" name="description"
                className="form-input"
                style={{ minHeight: 120, resize: 'vertical' }}
                placeholder="Tell potential adopters about this pet's personality, health, and history…"
                value={pet.description} onChange={handleChange}
              />
            </div>

            <motion.button
              type="submit"
              className="btn btn-primary"
              style={{ width:'100%', marginTop:8, padding:15, fontSize:'1rem' }}
              disabled={loading}
              whileTap={{ scale: 0.97 }}
            >
              {loading ? '🐾 Adding pet…' : 'Add Pet 🐾'}
            </motion.button>
          </form>
        </motion.div>
      </div>
    </div>
  );
}
