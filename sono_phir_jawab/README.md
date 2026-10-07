# AudioTranscribe

using the java Spring AI i use the Google Gemini Api key for the answer to the user's audio input.

model I use in this is gemini-3.8-flash

in this the mp3 file will converted to the .wav file by the java media package from where the .wav file will give the byte input to the Gemini for the user Input and from the Gemini API it generates the answer to that question.  



to use your API key use these commands in the same terminal 

$env:GEMINI_API_KEY="your-real-api-key"

.\mvnw spring-boot:run
