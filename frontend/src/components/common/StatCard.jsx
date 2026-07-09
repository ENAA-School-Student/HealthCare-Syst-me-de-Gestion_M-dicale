import { useState, useEffect } from 'react';
import { TrendingUp, TrendingDown, Minus } from 'lucide-react';

function AnimatedValue({ value }) {
  const [display, setDisplay] = useState(0);
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

export default function StatCard({ label, value, icon, color = 'var(--primary)', bg = 'var(--primary-soft)', change, changeLabel }) {
  const changeDir = change > 0 ? 'up' : change < 0 ? 'down' : 'neutral';
  const ChangeIcon = change > 0 ? TrendingUp : change < 0 ? TrendingDown : Minus;

  return (
    <div className="stat-card">
      <div className="stat-icon" style={{ background: bg, color }}>
        <div className="stat-icon-ring" style={{ color }} />
        {icon}
      </div>
      <div style={{ flex: 1 }}>
        <div className="stat-value">
          <AnimatedValue value={value} />
        </div>
        <div className="stat-label">{label}</div>
        {change !== undefined && (
          <div className={`stat-change ${changeDir}`}>
            <ChangeIcon size={11} strokeWidth={2.5} />
            {change > 0 ? '+' : ''}{change}{changeLabel ? ` ${changeLabel}` : ''}
          </div>
        )}
      </div>
    </div>
  );
}
