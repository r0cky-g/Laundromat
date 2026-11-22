// Send requests to the backend. Change BASE_URL if the backend address changes.

const BASE_URL = 'http://localhost:8080';

// Send a request and read the JSON response.
async function apiFetch(path, options = {}) {
  const token = getStoredToken();

  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
    ...(options.headers || {}),
  };

  const response = await fetch(`${BASE_URL}${path}`, {
    ...options,
    headers,
  });

  // A 204 response has no JSON to read
  if (response.status === 204) return null;

  let data;
  try {
    data = await response.json();
  } catch {
    data = null;
  }

  if (!response.ok) {
    const message =
      (data && (data.message || data.error)) ||
      `Request failed with status ${response.status}`;
    throw new ApiError(message, response.status, data);
  }

  return data;
}

// Shortcuts for common HTTP methods.
const api = {
  get:    (path, opts = {})       => apiFetch(path, { ...opts, method: 'GET' }),
  post:   (path, body, opts = {}) => apiFetch(path, { ...opts, method: 'POST',  body: JSON.stringify(body) }),
  put:    (path, body, opts = {}) => apiFetch(path, { ...opts, method: 'PUT',   body: JSON.stringify(body) }),
  patch:  (path, body, opts = {}) => apiFetch(path, { ...opts, method: 'PATCH', body: JSON.stringify(body) }),
  delete: (path, opts = {})       => apiFetch(path, { ...opts, method: 'DELETE' }),
};

// Keep the response status and error details.
class ApiError extends Error {
  constructor(message, status, data) {
    super(message);
    this.name    = 'ApiError';
    this.status  = status;
    this.data    = data;
  }
}

// Read the saved login token.
function getStoredToken() {
  return localStorage.getItem('auth_token');
}

// Let pages call api.get() and api.post()
window.api      = api;
window.ApiError = ApiError;
