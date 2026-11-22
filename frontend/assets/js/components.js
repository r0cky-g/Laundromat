// Load the shared navbar and footer when the page is ready.

const componentSiteRoot = new URL('../../', document.currentScript.src);

async function loadComponents() {
  await Promise.all([
    loadNavbar(),
    loadFooter(),
  ]);
}

async function loadNavbar() {
  const placeholder = document.getElementById('navbar-placeholder');
  if (!placeholder) return;

  try {
    const res = await fetch(new URL('components/navbar.html', componentSiteRoot));
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const html = await res.text();
    placeholder.innerHTML = html;

    placeholder.querySelectorAll('a[href^="/"]').forEach(link => {
      link.href = new URL(link.getAttribute('href').slice(1), componentSiteRoot);
    });

    _applyAuthState();
    _markActiveLink();
  } catch (err) {
    console.error('[components] Failed to load navbar:', err);
  }
}

async function loadFooter() {
  const placeholder = document.getElementById('footer-placeholder');
  if (!placeholder) return;

  try {
    const res = await fetch(new URL('components/footer.html', componentSiteRoot));
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const html = await res.text();
    placeholder.innerHTML = html;
  } catch (err) {
    console.error('[components] Failed to load footer:', err);
  }
}

// Show the right links for guests or logged-in users.
function _applyAuthState() {
  const guestEl    = document.getElementById('nav-guest');
  const userEl     = document.getElementById('nav-user');
  const avatarEl   = document.getElementById('nav-avatar');
  const usernameEl = document.getElementById('nav-username');
  const navLinks   = document.getElementById('nav-links');

  if (!guestEl || !userEl) return;

  const loggedIn = typeof auth !== 'undefined' && auth.isLoggedIn();
  guestEl.hidden = loggedIn;
  userEl.hidden = !loggedIn;
  if (navLinks) navLinks.hidden = !loggedIn;

  if (loggedIn) {
    const user = auth.getUser();
    const name = user?.name || user?.email;
    if (usernameEl) usernameEl.textContent = name || 'User';
    if (avatarEl) avatarEl.textContent = name ? _initials(name) : '?';
  }
}

// Highlight the link for the current page.
function _markActiveLink() {
  const currentPath = window.location.pathname;
  document.querySelectorAll('.navbar__link[data-nav]').forEach(link => {
    if (link.pathname === currentPath) {
      link.classList.add('is-active');
      link.setAttribute('aria-current', 'page');
    }
  });
}

// Get up to two initials from a name or email.
function _initials(str = '') {
  const parts = str.trim().split(/[\s@._-]+/).filter(Boolean);
  if (parts.length >= 2) return (parts[0][0] + parts[1][0]).toUpperCase();
  return str.slice(0, 2).toUpperCase();
}

// Show a popup message. Set duration to 0 to keep it open until dismissed.
function showToast(message, type = 'info', duration = 4000, title = '') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    container.className = 'toast-container';
    document.body.appendChild(container);
  }

  const icons = { success: '✓', error: '✕', warning: '⚠', info: 'ℹ' };
  const defaultTitles = { success: 'Success', error: 'Error', warning: 'Warning', info: 'Info' };

  const toast = document.createElement('div');
  toast.className = `toast toast--${type}`;
  toast.innerHTML = `
    <span class="toast__icon">${icons[type] || 'ℹ'}</span>
    <div class="toast__body">
      <div class="toast__title">${title || defaultTitles[type]}</div>
      <div class="toast__message">${message}</div>
    </div>
    <span class="toast__close" aria-label="Dismiss">×</span>
  `;

  container.appendChild(toast);

  // Wait for the toast to render before fading it in
  requestAnimationFrame(() => {
    requestAnimationFrame(() => toast.classList.add('is-visible'));
  });

  const dismiss = () => {
    toast.classList.remove('is-visible');
    toast.addEventListener('transitionend', () => toast.remove(), { once: true });
  };

  toast.querySelector('.toast__close').addEventListener('click', dismiss);
  if (duration > 0) setTimeout(dismiss, duration);
}

// Make this available to other pages
window.loadComponents = loadComponents;
window.showToast      = showToast;

// Load the shared components once the page is ready
document.addEventListener('DOMContentLoaded', loadComponents);
