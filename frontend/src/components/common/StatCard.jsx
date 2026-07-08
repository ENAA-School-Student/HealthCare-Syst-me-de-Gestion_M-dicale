import { useState, useEffect, useRef } from 'react';

function AnimatedValue({ value }) {
  const [display, setDisplay] = useState(0);
  const ref = useRef(null);
  const num = typeof value === 'number' ? value : 0;

  useEffect(() => {
    if (typeof value !== 'number') {
      setDisplay(value);
      return;
    }
    const start = 0;
    const duration = 1200;
    const startTime = performance.now();

    const animate = (now) => {
      const elapsed = now - startTime;
      const progress = Math.min(elapsed / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3);
      const current = Math.round(start + (num - start) * eased);
      setDisplay(current);
      if (progress < 1) requestAnimationFrame(animate);
    };

    requestAnimationFrame(animate);
  }, [value, num]);

  return <>{display}</>;
}

export default function StatCard({ label, value, icon, color = 'var(--accent)', bg = 'var(--accent-soft)' }) {
  return (
    <div className="stat-card">
      <div className="stat-icon" style={{ background: bg, color }}>
        {icon}
      </div>
      <div>
        <div className="stat-value">
          <AnimatedValue value={value} />
        </div>
        <div className="stat-label">{label}</div>
      </div>
    </div>
  );
}
