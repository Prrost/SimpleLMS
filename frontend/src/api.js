const API_BASE = import.meta.env.VITE_API_BASE || "";

function buildQuery(params = {}) {
  const query = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value === null || value === undefined || value === "") {
      return;
    }
    query.set(key, String(value));
  });
  return query.toString();
}

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, options);
  if (!response.ok) {
    let message = `Request failed with status ${response.status}`;
    try {
      const errorBody = await response.json();
      if (errorBody?.message) {
        message = errorBody.message;
      }
    } catch {
      // Ignore non-json error payloads
    }
    throw new Error(message);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

export function fetchPage(endpoint, params = {}) {
  const query = buildQuery(params);
  const path = query ? `${endpoint}?${query}` : endpoint;
  return request(path);
}

export function fetchAll(endpoint) {
  return request(`${endpoint}/all`);
}

export function createEntity(endpoint, payload) {
  return request(endpoint, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
}

export function updateEntity(endpoint, id, payload) {
  return request(`${endpoint}/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload)
  });
}

export function fetchById(endpoint, id) {
  return request(`${endpoint}/${id}`);
}
