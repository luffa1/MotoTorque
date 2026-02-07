const CACHE_STATIC  = 'static-v13';
const CACHE_DYNAMIC = 'dynamic-v5';
const OFFLINE_PAGE  = '/index.html';

const STATIC_ASSETS = [
    './',
    './index.html',
    './app.js',
    './data/spec-pack.json',
    './manifest.json',
    './icons/icon-192.png',
    './icons/icon-512.png',
    './screenshots/home-narrow.png',
    './screenshots/home-wide.png'
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
  event.waitUntil((async () => {
    const cache = await caches.open(CACHE_STATIC);
    for (const url of STATIC_ASSETS) {
      const request = new Request(url, { cache: 'reload' }); // omija 304
      try {
        const response = await fetch(request);
        if (!response.ok) throw new Error(`${response.status} ${response.statusText}`);
        await cache.put(request, response);
      } catch (err) {
        console.error('Nie mogę dodać do cache:', url, err);
        throw err; // przerwie instalację i pokaże konkretny adres
      }
    }
  })());
});

/*self.addEventListener('install', event => {
  event.waitUntil(caches.open(CACHE_STATIC).then(cache => cache.addAll(STATIC_ASSETS)));
  self.skipWaiting();
});
*/
self.addEventListener('activate', event => {
  const keep = [CACHE_STATIC, CACHE_DYNAMIC];
  event.waitUntil(
    caches.keys().then(keys =>
      Promise.all(
        keys.filter(k => !keep.includes(k)).map(k => caches.delete(k))
      )
    )
  );
  self.clients.claim();
});

self.addEventListener('fetch', event => {
  const request = event.request;
  if (request.method !== 'GET') return;

  const url = new URL(request.url);
  if (url.protocol !== 'http:' && url.protocol !== 'https:') return;

  if (request.mode === 'navigate' ||
      (request.headers.get('accept') || '').includes('text/html')) {
    event.respondWith(
      fetch(request).catch(() => caches.match(OFFLINE_PAGE))
    );
    return;
  }

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