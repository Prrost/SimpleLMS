export default function Pagination({ pageData, onPageChange }) {
  if (!pageData) return null;

  const currentPage = pageData.number ?? 0;
  const totalPages = pageData.totalPages ?? 0;

  return (
    <div className="pagination">
      <button className="btn btn-secondary" onClick={() => onPageChange(currentPage - 1)} disabled={currentPage <= 0}>
        Previous
      </button>
      <span className="pagination-info">
        Page {totalPages === 0 ? 0 : currentPage + 1} of {totalPages}
      </span>
      <button
        className="btn btn-secondary"
        onClick={() => onPageChange(currentPage + 1)}
        disabled={totalPages === 0 || currentPage >= totalPages - 1}
      >
        Next
      </button>
    </div>
  );
}
