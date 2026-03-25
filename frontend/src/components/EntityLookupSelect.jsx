import { useEffect, useMemo, useRef, useState } from "react";
import { fetchPage } from "../api.js";

const DEFAULT_PAGE_SIZE = 8;

function normalizeArrayValue(value) {
  return Array.isArray(value) ? value : [];
}

export default function EntityLookupSelect({
  label,
  endpoint,
  value,
  onChange,
  multiple = false,
  buildSearchParams = (query) => ({ name: query }),
  searchFields = null,
  optionLabel = (item) => item.name || `#${item.id}`,
  optionMeta = null,
  optionValue = (item) => item.id,
  pageSize = DEFAULT_PAGE_SIZE,
  sort = "id,asc",
  placeholder = "Select",
  initialSelectedItems = []
}) {
  const rootRef = useRef(null);
  const buildSearchParamsRef = useRef(buildSearchParams);
  buildSearchParamsRef.current = buildSearchParams;
  const hasStructuredSearch = Array.isArray(searchFields) && searchFields.length > 0;
  const searchFieldKey = hasStructuredSearch
    ? searchFields.map((field) => `${field.key}:${field.placeholder || ""}`).join("|")
    : "";
  const [open, setOpen] = useState(false);
  const [query, setQuery] = useState("");
  const [searchValues, setSearchValues] = useState(() =>
    hasStructuredSearch
      ? searchFields.reduce((acc, field) => ({ ...acc, [field.key]: "" }), {})
      : {}
  );
  const [page, setPage] = useState(0);
  const [debouncedQuery, setDebouncedQuery] = useState("");
  const [debouncedSearchValues, setDebouncedSearchValues] = useState(() =>
    hasStructuredSearch
      ? searchFields.reduce((acc, field) => ({ ...acc, [field.key]: "" }), {})
      : {}
  );
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [pageData, setPageData] = useState(null);
  const [cache, setCache] = useState(() => {
    const next = {};
    const selectedItems = Array.isArray(initialSelectedItems) ? initialSelectedItems : [initialSelectedItems];
    selectedItems.filter(Boolean).forEach((item) => {
      next[optionValue(item)] = item;
    });
    return next;
  });

  useEffect(() => {
    const onDocumentClick = (event) => {
      if (!rootRef.current) return;
      if (!rootRef.current.contains(event.target)) {
        setOpen(false);
      }
    };

    document.addEventListener("mousedown", onDocumentClick);
    return () => document.removeEventListener("mousedown", onDocumentClick);
  }, []);

  useEffect(() => {
    if (!open) return;

    let active = true;
    const load = async () => {
      setLoading(true);
      setError("");
      try {
        const params = Array.isArray(searchFields)
          ? Object.entries(debouncedSearchValues).reduce((acc, [key, rawValue]) => {
              const trimmed = String(rawValue || "").trim();
              if (trimmed !== "") {
                acc[key] = trimmed;
              }
              return acc;
            }, {})
          : buildSearchParamsRef.current(debouncedQuery.trim());

        const data = await fetchPage(endpoint, {
          ...params,
          page,
          size: pageSize,
          sort
        });

        if (!active) return;

        setPageData(data);
        if (Array.isArray(data?.content)) {
          setCache((prev) => {
            const next = { ...prev };
            data.content.forEach((item) => {
              next[optionValue(item)] = item;
            });
            return next;
          });
        }
      } catch (err) {
        if (!active) return;
        setError(err.message || "Lookup failed");
      } finally {
        if (active) setLoading(false);
      }
    };

    load();

    return () => {
      active = false;
    };
  }, [open, page, debouncedQuery, debouncedSearchValues, endpoint, pageSize, sort, hasStructuredSearch]);

  useEffect(() => {
    if (!open || hasStructuredSearch) return;
    const timeoutId = setTimeout(() => {
      setDebouncedQuery(query);
      setPage(0);
    }, 500);
    return () => clearTimeout(timeoutId);
  }, [query, open, hasStructuredSearch]);

  useEffect(() => {
    if (!open || !hasStructuredSearch) return;
    const timeoutId = setTimeout(() => {
      setDebouncedSearchValues(searchValues);
      setPage(0);
    }, 500);
    return () => clearTimeout(timeoutId);
  }, [searchValues, open, hasStructuredSearch]);

  useEffect(() => {
    if (!open) {
      setPage(0);
      setQuery("");
      setDebouncedQuery("");
      if (hasStructuredSearch) {
        const resetValues = searchFields.reduce((acc, field) => ({ ...acc, [field.key]: "" }), {});
        setSearchValues(resetValues);
        setDebouncedSearchValues(resetValues);
      }
    }
  }, [open, hasStructuredSearch, searchFieldKey]);

  const selectedText = useMemo(() => {
    if (multiple) {
      const selectedIds = normalizeArrayValue(value);
      if (selectedIds.length === 0) {
        return placeholder;
      }

      const labels = selectedIds
        .slice(0, 2)
        .map((id) => cache[id])
        .filter(Boolean)
        .map((item) => optionLabel(item));

      if (selectedIds.length <= 2 && labels.length === selectedIds.length) {
        return labels.join(", ");
      }

      return `${selectedIds.length} selected`;
    }

    if (value == null || value === "") {
      return placeholder;
    }

    const item = cache[value];
    return item ? optionLabel(item) : `ID ${value}`;
  }, [multiple, value, cache, optionLabel, placeholder]);

  const rows = pageData?.content ?? [];

  const isSelected = (item) => {
    const id = optionValue(item);
    if (multiple) {
      return normalizeArrayValue(value).includes(id);
    }
    return value === id;
  };

  const toggleValue = (item) => {
    const id = optionValue(item);

    if (multiple) {
      const selected = normalizeArrayValue(value);
      if (selected.includes(id)) {
        onChange(selected.filter((current) => current !== id));
      } else {
        onChange([...selected, id]);
      }
      return;
    }

    onChange(id);
    setOpen(false);
  };

  return (
    <div className="lookup" ref={rootRef}>
      {label && <div className="lookup-label">{label}</div>}
      <button type="button" className="lookup-trigger" onClick={() => setOpen((prev) => !prev)}>
        <span>{selectedText}</span>
        <span>{open ? "▲" : "▼"}</span>
      </button>

      {open && (
        <div className="lookup-panel">
          {!multiple && value != null && value !== "" && (
            <button type="button" className="btn btn-secondary lookup-clear" onClick={() => onChange(null)}>
              Clear selection
            </button>
          )}
          {hasStructuredSearch ? (
            <div className="lookup-search-grid">
              {searchFields.map((field) => (
                <input
                  key={field.key}
                  className="lookup-search"
                  value={searchValues[field.key] || ""}
                  onChange={(event) => {
                    setSearchValues((prev) => ({ ...prev, [field.key]: event.target.value }));
                    setPage(0);
                  }}
                  placeholder={field.placeholder || field.key}
                />
              ))}
            </div>
          ) : (
            <input
              className="lookup-search"
              value={query}
              onChange={(event) => {
                setQuery(event.target.value);
                setPage(0);
              }}
              placeholder="Search"
            />
          )}

          <div className="lookup-options">
            {loading && <div className="lookup-state">Loading...</div>}
            {!loading && error && <div className="lookup-state lookup-error">{error}</div>}
            {!loading && !error && rows.length === 0 && <div className="lookup-state">No items</div>}
            {!loading && !error && rows.map((item) => (
              <button
                key={optionValue(item)}
                type="button"
                className={`lookup-option ${isSelected(item) ? "active" : ""} ${multiple ? "multiple" : "single"}`}
                onClick={() => toggleValue(item)}
              >
                <span className="lookup-check">
                  {multiple && <input type="checkbox" readOnly checked={isSelected(item)} />}
                </span>
                <span className="lookup-content">
                  <span className="lookup-title">{optionLabel(item)}</span>
                  {typeof optionMeta === "function" && optionMeta(item) && (
                    <span className="lookup-meta">{optionMeta(item)}</span>
                  )}
                </span>
              </button>
            ))}
          </div>

          <div className="lookup-pagination">
            <button type="button" className="btn btn-secondary" onClick={() => setPage((prev) => Math.max(prev - 1, 0))} disabled={(pageData?.number ?? 0) <= 0}>
              Prev
            </button>
            <span>
              {(pageData?.number ?? 0) + 1} / {Math.max(pageData?.totalPages ?? 1, 1)}
            </span>
            <button
              type="button"
              className="btn btn-secondary"
              onClick={() => setPage((prev) => prev + 1)}
              disabled={(pageData?.totalPages ?? 1) <= ((pageData?.number ?? 0) + 1)}
            >
              Next
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
