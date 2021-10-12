
# Fezzel

Add yours google-services.json 

Change Authorization:key with your key from firebase project:


Implementation Guide
1 - Project
1 - Open the Project in your android studio;
2 - IMPORTANT Change the Package Name. (https://stackoverflow.com/questions/16804093/android-studio-rename-package)


2 - Firebase Panel
- Create Firebase Project (https://console.firebase.google.com/);
- Import the file google-service.json into your project
- Connect to firebase console authentication and database from your IDE
- in firebase Storage Rules, change value of "allow read, write:" from "if request.auth != null" to "if true;"
- For sending notification, paste your Firebase project key into your project APIService.java
- When you change database settings, you likely will need to uninstall and reinstall apps to avoid app crashes due to app caches.

