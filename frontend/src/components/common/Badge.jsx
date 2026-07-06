const STATUS_MAP = {
  EN_ATTENTE: { label: 'En attente', cls: 'badge-warning' },
  CONFIRME:   { label: 'Confirmé',   cls: 'badge-success' },
  ANNULE:     { label: 'Annulé',     cls: 'badge-danger'  },
  TERMINE:    { label: 'Terminé',    cls: 'badge-muted'   },
};

export default function Badge({ status, text }) {
  const key = typeof status === 'string' ? status : '';
  const s = STATUS_MAP[key] || { label: text || key || '—', cls: 'badge-accent' };
  return <span className={`badge ${s.cls}`}>{s.label}</span>;
}
