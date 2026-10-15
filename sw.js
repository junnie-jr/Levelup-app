const CACHE='levelup-v5';
const FILES=['./','index.html','manifest.webmanifest','icon-192.png','icon-512.png'];
self.addEventListener('install',e=>{e.waitUntil(caches.open(CACHE).then(c=>Promise.allSettled(FILES.map(f=>c.add(f)))));self.skipWaiting()});
self.addEventListener('activate',e=>{e.waitUntil(caches.keys().then(k=>Promise.all(k.filter(x=>x!==CACHE).map(x=>caches.delete(x)))));self.clients.claim()});
self.addEventListener('fetch',e=>{if(e.request.method!=='GET')return;
e.respondWith((async()=>{const hit=await caches.match(e.request,{ignoreSearch:true});
try{const r=await fetch(e.request);if(r.ok&&new URL(e.request.url).origin===location.origin){const c=await caches.open(CACHE);c.put(e.request,r.clone())}return r}
catch(err){return hit||(e.request.mode==='navigate'?await caches.match('index.html'):null)||new Response('',{status:504})}})())});
