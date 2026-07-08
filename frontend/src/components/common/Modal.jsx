import { useState, useEffect, useCallback } from 'react';
import { X } from 'lucide-react';

export default function Modal({ isOpen, onClose, title, children, footer }) {
  const [visible, setVisible] = useState(false);
  const [animating, setAnimating] = useState(false);

  useEffect(() => {
    if (isOpen) {
      setVisible(true);
      requestAnimationFrame(() => setAnimating(true));
    } else {
      setAnimating(false);
      const timer = setTimeout(() => setVisible(false), 200);
      return () => clearTimeout(timer);
    }
  }, [isOpen]);

  const handleOverlayClick = useCallback((e) => {
    if (e.target === e.currentTarget) onClose();
  }, [onClose]);

  const handleEscape = useCallback((e) => {
    if (e.key === 'Escape') onClose();
  }, [onClose]);

  useEffect(() => {
    if (isOpen) {
      document.addEventListener('keydown', handleEscape);
      document.body.style.overflow = 'hidden';
    }
    return () => {
      document.removeEventListener('keydown', handleEscape);
      document.body.style.overflow = '';
    };
  }, [isOpen, handleEscape]);

  if (!visible) return null;

  return (
    <div
      className="modal-overlay"
      onClick={handleOverlayClick}
      style={{
        opacity: animating ? 1 : 0,
        transition: 'opacity 0.2s ease',
      }}
    >
      <div
        className="modal"
        style={{
          transform: animating ? 'scale(1) translateY(0)' : 'scale(0.95) translateY(10px)',
          opacity: animating ? 1 : 0,
          transition: 'transform 0.25s cubic-bezier(0.34, 1.56, 0.64, 1), opacity 0.2s ease',
        }}
      >
        <div className="modal-header">
          <h2 className="modal-title">{title}</h2>
          <button className="btn btn-ghost btn-sm" onClick={onClose} style={{ borderRadius: '50%', padding: 6, width: 32, height: 32 }}>
            <X size={16} />
          </button>
        </div>
        {children}
        {footer && <div className="modal-footer">{footer}</div>}
      </div>
    </div>
  );
}
