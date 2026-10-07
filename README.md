# AudioTranscribe

Using the Java Spring AI, I use the Google Gemini API key for the answer to the user's audio input.

Model used: gemini-1.5-flash

In this project, the .mp3 file will be converted to a .wav file by the Java media package, from where the .wav file will provide the byte input to Gemini for user input, and the Gemini API generates the answer to that question.

To set up your API key and run:

$env:GEMINI_API_KEY="your-real-api-key"
.\mvnw spring-boot:run


```
cd speach-to-text-frontend
npm install
npm run dev
```
