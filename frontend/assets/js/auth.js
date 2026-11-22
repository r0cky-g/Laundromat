// Save and read the login details.

const authPages = new URL('../../pages/', document.currentScript.src);

const TOKEN_KEY = 'auth_token';
const USER_KEY  = 'auth_user';

// Read the saved token.
function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

// Save the token after login.
function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

// Read the saved user or get their details from the token.
function getUser() {
  const stored = localStorage.getItem(USER_KEY);
  if (stored) {
    try {
      return JSON.parse(stored);
    } catch {
      // Try reading the token if the saved user is invalid.
    }
  }

  // Read the user details from the token if needed
  const token = getToken();
  if (!token) return null;
  try {
    const payload = JSON.parse(atob(token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
    return {
      id:    payload.sub || payload.id,
      email: payload.email,
      name:  payload.name,
      role:  payload.role,
    };
  } catch {
    return null;
  }
}

// Save the user after login.
function setUser(user) {
  localStorage.setItem(USER_KEY, JSON.stringify(user));
}

// Check if there is a saved token.
function isLoggedIn() {
  return !!getToken();
}

// Clear the login details and go to the login page by default.
function logout(redirectTo = new URL('login.html', authPages)) {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
  window.location.href = redirectTo;
}

// Send guests to login and remember where they were going.
function requireAuth() {
  if (!isLoggedIn()) {
    const next = encodeURIComponent(window.location.pathname + window.location.search);
    window.location.href = new URL(`login.html?next=${next}`, authPages);
  }
}

// Send logged-in users away from the login or sign-up page.
function requireGuest() {
  if (isLoggedIn()) {
    window.location.href = new URL('dashboard.html', authPages);
  }
}

// Make this available to other pages
window.auth = { getToken, setToken, getUser, setUser, isLoggedIn, logout, requireAuth, requireGuest };
