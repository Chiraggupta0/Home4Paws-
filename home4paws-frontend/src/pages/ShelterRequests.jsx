import { useEffect, useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import api from '../api/axiosConfig';

const STATUS_MAP = {
  PENDING:  { cls: 'badge-pending', label: '⏳ Pending'  },
  APPROVED: { cls: 'badge-success', label: '✅ Approved' },
  REJECTED: { cls: 'badge-error',   label: '❌ Rejected' },
};

export default function ShelterRequests() {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading]   = useState(true);
  const [updating, setUpdating] = useState(null);

  const fetchRequests = () => {
    setLoading(true);
    api.get('/api/requests/my-pets')
      .then(r => setRequests(r.data))
      .catch(console.error)
      .finally(() => setLoading(false));
  };

  useEffect(() => { fetchRequests(); }, []);

  const updateStatus = async (id, status) => {
    setUpdating(id);
    try {
      const ep = status === 'APPROVED' ? `/api/requests/${id}/approve` : `/api/requests/${id}/reject`;
      await api.put(ep);
      fetchRequests();
    } catch (err) {
      console.error(err);
    } finally {
      setUpdating(null);
    }
  };

  return (
    <div className="page-wrapper">
      <div style={{ background: 'linear-gradient(135deg,#2C1810 0%,#6B3422 60%,#9B4E20 100%)', padding: 'clamp(60px,8vw,100px) 0 clamp(40px,5vw,60px)' }}>
        <div className="container">
          <motion.div initial={{opacity:0,y:20}} animate={{opacity:1,y:0}} transition={{duration:.5}}>
            <p className="section-eyebrow" style={{color:'var(--accent)'}}>📬 Manage</p>
            <h1 style={{color:'#fff', fontSize:'clamp(2rem,4vw,2.8rem)', fontFamily:"'Playfair Display',serif", marginTop:8}}>
              Adoption Requests
            </h1>
            <p style={{color:'rgba(255,255,255,.6)', marginTop:8, fontSize:'1rem'}}>
              Review and respond to adoption applications.
            </p>
          </motion.div>
        </div>
      </div>

      <div className="container" style={{paddingTop:40, paddingBottom:80}}>
        {loading ? (
          <div className="loading-screen">
            <div className="paw-loader">🐾</div>
            <p className="loading-text">Loading requests…</p>
          </div>
        ) : requests.length === 0 ? (
          <div className="empty-state">
            <div className="empty-state-icon">📬</div>
            <h3>No requests yet</h3>
            <p>When adopters apply for your pets, you'll see them here.</p>
          </div>
        ) : (
          <div style={{ display:'flex', flexDirection:'column', gap:16 }}>
            <AnimatePresence>
              {requests.map((req, i) => {
                const s = STATUS_MAP[req.status] || STATUS_MAP.PENDING;
                return (
                  <motion.div
                    key={req.id}
                    className="card"
                    initial={{ opacity: 0, y: 20 }}
                    animate={{ opacity: 1, y: 0 }}
                    transition={{ delay: i * 0.07 }}
                    style={{ padding: '24px 28px' }}
                  >
                    <div style={{ display:'flex', alignItems:'center', justifyContent:'space-between', gap:16, flexWrap:'wrap' }}>
                      <div style={{ display:'flex', alignItems:'center', gap:16 }}>
                        <div style={{ fontSize:'1.8rem', width:52, height:52, background:'var(--cream-dark)', borderRadius:'50%', display:'flex', alignItems:'center', justifyContent:'center', flexShrink:0 }}>
                          🐶
                        </div>
                        <div>
                          <h3 style={{ fontSize:'1.05rem', marginBottom:3 }}>{req.pet?.name || 'Pet'}</h3>
                          <p style={{ fontSize:'.85rem', color:'var(--text-muted)' }}>
                            Adopter: <strong style={{color:'var(--text)'}}>{req.adopter?.name || '—'}</strong>
                          </p>
                        </div>
                      </div>

                      <div style={{ display:'flex', alignItems:'center', gap:10, flexWrap:'wrap' }}>
                        <span className={`badge ${s.cls}`}>{s.label}</span>

                        {req.status === 'PENDING' && (
                          <>
                            <motion.button
                              className="btn btn-sm"
                              style={{ background:'var(--success)', color:'#fff', border:'none' }}
                              onClick={() => updateStatus(req.id, 'APPROVED')}
                              disabled={updating === req.id}
                              whileTap={{ scale: 0.95 }}
                            >
                              {updating === req.id ? '…' : '✅ Approve'}
                            </motion.button>
                            <motion.button
                              className="btn btn-sm"
                              style={{ background:'var(--error)', color:'#fff', border:'none' }}
                              onClick={() => updateStatus(req.id, 'REJECTED')}
                              disabled={updating === req.id}
                              whileTap={{ scale: 0.95 }}
                            >
                              {updating === req.id ? '…' : '❌ Reject'}
                            </motion.button>
                          </>
                        )}
                      </div>
                    </div>
                  </motion.div>
                );
              })}
            </AnimatePresence>
          </div>
        )}
      </div>
    </div>
  );
}
