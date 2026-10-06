const loginCard = document.querySelector('#login-card');
const loginForm = document.querySelector('#login-form');
const passwordInput = document.querySelector('#admin-password');
const loginMessage = document.querySelector('#login-message');
const dashboard = document.querySelector('#dashboard');
const linksCount = document.querySelector('#links-count');
const clicksCount = document.querySelector('#clicks-count');
const linksList = document.querySelector('#links-list');
const emptyLinks = document.querySelector('#empty-links');
let credentials = '';

document.querySelector('#year').textContent = new Date().getFullYear();

function formatDate(value) {
  return new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' }).format(new Date(value));
}

function escapeHtml(value) {
  const element = document.createElement('span');
  element.textContent = value ?? '';
  return element.innerHTML;
}

function renderLinks(links) {
  linksList.innerHTML = links.map((link) => {
    const shortUrl = new URL(link.codigo, `${window.location.origin}/`).href;
    return `<tr><td><a href="${escapeHtml(shortUrl)}" target="_blank" rel="noopener">${escapeHtml(link.codigo)}</a></td><td title="${escapeHtml(link.urlOriginal)}">${escapeHtml(link.urlOriginal)}</td><td>${formatDate(link.criadoEm)}</td><td>${link.cliques ?? 0}</td></tr>`;
  }).join('');
  emptyLinks.hidden = links.length !== 0;
}

async function loadDashboard() {
  const response = await fetch('/api/painel/visao-geral', { headers: { Authorization: credentials } });
  if (response.status === 401) throw new Error('Senha inválida.');
  if (!response.ok) {
    const body = await response.json().catch(() => ({}));
    throw new Error(body.mensagem || 'Não foi possível carregar o painel.');
  }
  const data = await response.json();
  linksCount.textContent = data.linksCriados;
  clicksCount.textContent = data.totalCliques;
  renderLinks(data.links);
  loginCard.hidden = true;
  dashboard.hidden = false;
}

loginForm.addEventListener('submit', async (event) => {
  event.preventDefault();
  loginMessage.textContent = '';
  credentials = `Basic ${btoa(`admin:${passwordInput.value}`)}`;
  try {
    await loadDashboard();
    passwordInput.value = '';
  } catch (error) {
    credentials = '';
    loginMessage.textContent = error.message;
  }
});

document.querySelector('#refresh').addEventListener('click', async () => {
  try { await loadDashboard(); } catch (error) { loginMessage.textContent = error.message; }
});

document.querySelector('#logout').addEventListener('click', () => {
  credentials = '';
  dashboard.hidden = true;
  loginCard.hidden = false;
  passwordInput.focus();
});
