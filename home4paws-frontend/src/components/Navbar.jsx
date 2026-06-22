import { useState, useEffect } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { motion, AnimatePresence } from 'framer-motion';
import { supabase } from '../lib/supabaseClient';
import '../styles/Navbar.css';

export default function Navbar() {
  const navigate   = useNavigate();
  const location   = useLocation();
  const [scrolled, setScrolled] = useState(false);
  const [open,     setOpen]     = useState(false);

  const token = localStorage.getItem('token');
  const role  = localStorage.getItem('role');

  useEffect(() => {
    const fn = () => setScrolled(window.scrollY > 40);
    window.addEventListener('scroll', fn, { passive: true });
    fn();
    return () => window.removeEventListener('scroll', fn);
  }, []);

  useEffect(() => { setOpen(false); }, [location.pathname]);
  useEffect(() => { document.body.style.overflow = open ? 'hidden' : ''; return () => { document.body.style.overflow = ''; }; }, [open]);

  const handleLogout = async () => {
    await supabase.auth.signOut();
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    navigate('/login');
  };

  const links = role === 'NORMAL_USER'
    ? [{ to: '/pets', label: 'Browse Pets' }, { to: '/my-requests', label: 'My Requests' }]
    : role === 'NGO_SHELTER'
    ? [{ to: '/add-pet', label: 'Add Pet' }, { to: '/my-dogs', label: 'My Pets' }, { to: '/shelter-requests', label: 'Requests' }]
    : [{ to: '/pets', label: 'Browse Pets' }, { to: '#ngo', label: 'For NGOs' }];

  return (
    <>
      <nav className={`nav ${scrolled ? 'nav--solid' : 'nav--glass'}`}>
        <div className="nav__inner container">
          <Link to="/" className="nav__logo">
            <div className="nav__logo-icon">🐾</div>
            <span className="nav__logo-text">Home4Paws</span>
          </Link>

          <div className="nav__links hide-mobile">
            {links.map(l => (
              <Link key={l.to} to={l.to}
                className={`nav__link ${location.pathname === l.to ? 'nav__link--active' : ''}`}>
                {l.label}
              </Link>
            ))}
          </div>

          <div className="nav__auth hide-mobile">
            {token ? (
              <motion.button className="btn btn-primary btn-sm" onClick={handleLogout} whileTap={{scale:.96}}>
                Log out
              </motion.button>
            ) : (
              <>
                <Link to="/login"    className="nav__signin">Sign In</Link>
                <Link to="/register" className="btn btn-primary btn-sm">Sign up</Link>
              </>
            )}
          </div>

          <button className="nav__burger hide-desktop" onClick={() => setOpen(v => !v)} aria-label="Menu">
            <span className={`burger-bar ${open ? 'bb-top-open' : ''}`} />
            <span className={`burger-bar ${open ? 'bb-mid-open' : ''}`} />
            <span className={`burger-bar ${open ? 'bb-bot-open' : ''}`} />
          </button>
        </div>
      </nav>

      <AnimatePresence>
        {open && (
          <>
            <motion.div key="ov" className="drawer-overlay"
              initial={{opacity:0}} animate={{opacity:1}} exit={{opacity:0}} transition={{duration:.2}}
              onClick={() => setOpen(false)} />
            <motion.aside key="dr" className="drawer"
              initial={{x:'100%'}} animate={{x:0}} exit={{x:'100%'}}
              transition={{type:'spring',stiffness:320,damping:30}}>
              <div className="drawer__head">
                <div className="nav__logo-icon" style={{width:32,height:32,fontSize:'.9rem'}}>🐾</div>
                <span className="nav__logo-text">Home4Paws</span>
                <button className="drawer__close" onClick={() => setOpen(false)}>✕</button>
              </div>
              <nav className="drawer__nav">
                {links.map((l,i) => (
                  <motion.div key={l.to} initial={{opacity:0,x:20}} animate={{opacity:1,x:0}} transition={{delay:i*.05+.05}}>
                    <Link to={l.to} className={`drawer__link ${location.pathname===l.to?'drawer__link--active':''}`}>{l.label}</Link>
                  </motion.div>
                ))}
              </nav>
              <div className="drawer__foot">
                {token ? (
                  <button className="btn btn-primary" style={{width:'100%'}} onClick={handleLogout}>Log out</button>
                ) : (
                  <>
                    <Link to="/login"    className="btn btn-ghost-dark" style={{justifyContent:'center'}}>Sign In</Link>
                    <Link to="/register" className="btn btn-primary"    style={{justifyContent:'center'}}>Sign up free 🐾</Link>
                  </>
                )}
              </div>
            </motion.aside>
          </>
        )}
      </AnimatePresence>
    </>
  );
}
