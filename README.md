# Level UP

Personal lifting and cardio tracker. Single-page app, no build step, installable on your phone (PWA).
Data is stored in your browser (localStorage). Use Me > Backup your data to export it.

## Deploy
1. Push these files to a GitHub repo.
2. Settings > Pages > Deploy from a branch > main / (root).
3. Open https://YOUR-USERNAME.github.io/REPO-NAME/ on your phone and add it to your home screen.

## Updating
Edit `index.html`, then change `CACHE='levelup-v1'` in `sw.js` to `levelup-v2` so phones pick up the new version.
