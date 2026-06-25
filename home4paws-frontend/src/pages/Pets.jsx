import { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import api from '../api/axiosConfig';
import PetCard from '../components/PetCard';
import '../styles/Pets.css';

const TABS = [
  { key: 'all',      label: 'All' },
  { key: 'adoption', label: '🐾 For Adoption' },
  { key: 'sale',     label: '🏷️ For Sale' },
];

export default function Pets() {
  const [pets, setPets]     = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch]  = useState('');
  const [tab, setTab]        = useState('all');

  useEffect(() => {
    api.get('/api/pets')
      .then(r => setPets(r.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  const filtered = pets.filter(p => {
    // Tab filter: sale = has price, adoption = no price
    if (tab === 'sale'     && p.price == null) return false;
    if (tab === 'adoption' && p.price != null) return false;
    // Search filter
    return !search ||
      p.name?.toLowerCase().includes(search.toLowerCase()) ||
      p.breed?.toLowerCase().includes(search.toLowerCase()) ||
      p.species?.toLowerCase().includes(search.toLowerCase());
  });

  return (
    <div className="page-wrapper">
      {/* Page header */}
      <div className="pets-header">
        <div className="container">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.6 }}
            className="pets-header__inner"
          >
            <div>
              <p className="section-eyebrow">Waiting for you</p>
              <h1 className="pets-header__title">Find Your New<br /><em>Best Friend</em></h1>
              <p className="pets-header__sub">
                Every pet here needs a caring home. Browse and say hello. 🐾
              </p>
            </div>

            {/* Search */}
            <div className="pets-search">
              <span className="pets-search__icon">🔍</span>
              <input
                type="text"
                className="form-input pets-search__input"
                placeholder="Search by name, breed or species…"
                value={search}
                onChange={e => setSearch(e.target.value)}
              />
            </div>
          </motion.div>

          {/* Tabs */}
          <div className="pets-tabs">
            {TABS.map(t => (
              <button
                key={t.key}
                className={`pets-tab ${tab === t.key ? 'pets-tab--active' : ''}`}
                onClick={() => setTab(t.key)}
              >
                {t.label}
              </button>
            ))}
          </div>
        </div>
      </div>

      <div className="container" style={{ paddingBottom: 80 }}>
        {loading ? (
          <div className="loading-screen">
            <div className="paw-loader">🐾</div>
            <p className="loading-text">Fetching furry friends…</p>
          </div>
        ) : filtered.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon">🐶</div>
            <h3>No pets found</h3>
            <p>{search ? `No results for "${search}"` : 'No pets are available right now. Check back soon!'}</p>
          </div>
        ) : (
          <>
            <p className="pets-count">{filtered.length} pet{filtered.length !== 1 ? 's' : ''} available</p>
            <div className="pets-grid">
              {filtered.map((pet, i) => (
                <PetCard key={pet.id} pet={pet} index={i} showStatus />
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  );
}
