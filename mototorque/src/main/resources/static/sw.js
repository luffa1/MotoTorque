const CACHE_STATIC  = 'static-v5';
const CACHE_DYNAMIC = 'dynamic-v3';
const OFFLINE_PAGE  = '/index.html';

const STATIC_ASSETS = [
  '/',
  OFFLINE_PAGE,
  '/manifest.json',
  '/icons/icon-192.png',
  '/icons/icon-512.png'
];

const apiOfflineResponse = () =>
  new Response(JSON.stringify({ error: 'offline' }), {
    status: 503,
    statusText: 'Offline',
    headers: { 'Content-Type': 'application/json' }
  });

const assetOfflineResponse = () =>
  new Response('Offline: resource not cached', {
    status: 504,
    statusText: 'Offline'
  });

self.addEventListener('install', event => {
  event.waitUntil(caches.open(CACHE_STATIC).then(cache => cache.addAll(STATIC_ASSETS)));
  self.skipWaiting();
});

self.addEventListener('activate', event => {
  const keep = [CACHE_STATIC, CACHE_DYNAMIC];
  event.waitUntil(
    caches.keys().then(keys =>
      Promise.all(keys.filter(k => !keep.includes(k)).map(k => caches.delete(k)))
    )
  );
  self.clients.claim();
});

self.addEventListener('fetch', event => {
  const request = event.request;
  if (request.method !== 'GET') return;

  const url = new URL(request.url);
  if (url.protocol !== 'http:' && url.protocol !== 'https:') return;

  // Dokumenty
  if (request.mode === 'navigate' ||
      (request.headers.get('accept') || '').includes('text/html')) {
    event.respondWith(
      fetch(request).catch(() => caches.match(OFFLINE_PAGE))
    );
    return;
  }

  // API
  if (url.pathname.startsWith('/api/')) {
    event.respondWith(
      fetch(request)
        .then(response => {
          const clone = response.clone();
          caches.open(CACHE_DYNAMIC).then(cache => cache.put(request, clone));
          return response;
        })
        .catch(() =>
          caches.match(request).then(cached => cached || apiOfflineResponse())
        )
    );
    return;
  }

  // Pozostałe zasoby (CSS/JS/img)
  event.respondWith(
    caches.match(request).then(cached => {
      if (cached) return cached;

      return fetch(request)
        .then(response => {
          if (response.ok && url.origin === self.location.origin) {
            const clone = response.clone();
            caches.open(CACHE_DYNAMIC).then(cache => cache.put(request, clone));
          }
          return response;
        })
        .catch(() => assetOfflineResponse());
    })
  );
});