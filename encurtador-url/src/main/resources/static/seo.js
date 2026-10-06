(() => {
  const url = new URL(window.location.href);
  url.hash = '';
  url.search = '';

  const canonical = document.createElement('link');
  canonical.rel = 'canonical';
  canonical.href = url.href;
  document.head.append(canonical);

  const ogUrl = document.createElement('meta');
  ogUrl.setAttribute('property', 'og:url');
  ogUrl.content = url.href;
  document.head.append(ogUrl);

  if (url.pathname === '/' || url.pathname.endsWith('/index.html')) {
    const structuredData = document.createElement('script');
    structuredData.type = 'application/ld+json';
    structuredData.text = JSON.stringify({
      '@context': 'https://schema.org',
      '@type': 'WebApplication',
      name: 'CurtaLink',
      applicationCategory: 'UtilitiesApplication',
      operatingSystem: 'Web',
      inLanguage: 'pt-BR',
      description: 'Encurtador de URL gratuito para criar links curtos e fáceis de compartilhar.',
      url: url.origin
    });
    document.head.append(structuredData);
  }
})();
