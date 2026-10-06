const form = document.querySelector('#shorten-form');
const urlInput = document.querySelector('#url');
const submitButton = document.querySelector('#submit-button');
const message = document.querySelector('#form-message');
const result = document.querySelector('#result');
const shortLink = document.querySelector('#short-link');
const originalLink = document.querySelector('#original-link');
const copyButton = document.querySelector('#copy-button');
const buttonLabel = submitButton.querySelector('.button-label');

document.querySelector('#year').textContent = new Date().getFullYear();

function validUrl(value) {
  try {
    const parsed = new URL(value);
    return ['http:', 'https:'].includes(parsed.protocol);
  } catch {
    return false;
  }
}

function showError(text) {
  message.textContent = text;
  result.hidden = true;
}

form.addEventListener('submit', async (event) => {
  event.preventDefault();
  const urlOriginal = urlInput.value.trim();
  message.textContent = '';

  if (!validUrl(urlOriginal)) {
    showError('Informe uma URL válida começando com http:// ou https://');
    urlInput.focus();
    return;
  }

  submitButton.disabled = true;
  submitButton.classList.add('is-loading');
  buttonLabel.textContent = 'Criando link';

  try {
    const response = await fetch('/api/urls', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ urlOriginal })
    });
    if (!response.ok) {
      const error = await response.json().catch(() => ({}));
      throw new Error(error.mensagem || 'Não foi possível encurtar este link agora.');
    }
    const data = await response.json();
    const shortened = new URL(String(data.urlEncurtada).replace(/^\/+/, ''), `${window.location.origin}/`).href;
    shortLink.href = shortened;
    shortLink.textContent = shortened;
    originalLink.href = data.urlOriginal;
    originalLink.textContent = data.urlOriginal;
    result.hidden = false;
  } catch (error) {
    showError(error.message || 'Não foi possível encurtar este link agora. Tente novamente em instantes.');
  } finally {
    submitButton.disabled = false;
    submitButton.classList.remove('is-loading');
    buttonLabel.textContent = 'Encurtar link';
  }
});

copyButton.addEventListener('click', async () => {
  try {
    await navigator.clipboard.writeText(shortLink.href);
    copyButton.querySelector('span').textContent = 'Copiado!';
    setTimeout(() => { copyButton.querySelector('span').textContent = 'Copiar'; }, 1800);
  } catch {
    showError('Não foi possível copiar automaticamente. Selecione o link acima.');
  }
});
