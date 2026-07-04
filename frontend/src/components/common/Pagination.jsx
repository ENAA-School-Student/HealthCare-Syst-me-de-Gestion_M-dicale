import { ChevronLeft, ChevronRight } from 'lucide-react';

export default function Pagination({ page, totalPages, onChange }) {
  if (totalPages <= 1) return null;
  return (
    <div className="pagination">
      <span className="pagination-info">Page {page + 1} sur {totalPages}</span>
      <div className="pagination-controls">
        <button className="page-btn" disabled={page === 0} onClick={() => onChange(page - 1)}>
          <ChevronLeft size={14} />
        </button>
        {[...Array(Math.min(totalPages, 5))].map((_, i) => {
          const p = totalPages <= 5 ? i : Math.max(0, page - 2) + i;
          if (p >= totalPages) return null;
          return (
            <button key={p} className={`page-btn ${p === page ? 'active' : ''}`} onClick={() => onChange(p)}>
              {p + 1}
            </button>
          );
        })}
        <button className="page-btn" disabled={page >= totalPages - 1} onClick={() => onChange(page + 1)}>
          <ChevronRight size={14} />
        </button>
      </div>
    </div>
  );
}
