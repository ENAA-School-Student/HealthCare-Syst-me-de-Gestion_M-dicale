const STATUS_MAP = {
  EN_ATTENTE: { label: 'En attente', cls: 'badge-warning' },
  CONFIRME:   { label: 'Confirmé',   cls: 'badge-success' },
  ANNULE:     { label: 'Annulé',     cls: 'badge-danger'  },
  TERMINE:    { label: 'Terminé',    cls: 'badge-muted'   },
};

export default function Badge({ status, text }) {
  const s = STATUS_MAP[status] || { label: text || status, cls: 'badge-accent' };
  return <span className={`badge ${s.cls}`}>{s.label}</span>;
}
