package AudioTranscribe.example.sono_phir_jawab;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/audio")
public class AudioController {

    private final ChatClient chatClient;


    public AudioController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping
    public ResponseEntity<String> processAudio(
            @RequestParam("file") MultipartFile file
    ) {

        try {

            // Check if file was uploaded
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Audio file is empty");
            }

            // Get the MIME type of the uploaded file
            String contentType = file.getContentType();

            // Check if MIME type is available
            if (contentType == null || contentType.isBlank()) {
                return ResponseEntity.badRequest()
                        .body("Could not determine audio file type");
            }

            // Check whether the uploaded file is actually an audio file
            if (!contentType.startsWith("audio/")) {
                return ResponseEntity.badRequest()
                        .body("Please upload an audio file");
            }

            // Read the uploaded audio file into bytes
            byte[] audioBytes = file.getBytes();

//            ByteArrayResource audioResource = new ByteArrayResource(audioBytes);

            Media audioMedia = Media.builder()
                    .mimeType(MimeTypeUtils.parseMimeType("audio/wav"))
                    .data(audioBytes)
                    .build();

            // Send audio + instruction to Gemini
            String transcription = chatClient.prompt()
                    .user(user -> user
                            .text("""
                                    Listen to this audio carefully.
                                    Understand what the speaker is saying.
                                    Answer the speaker's question clearly.
                                    Give only the answer in text.
                        """)
                            .media(audioMedia)
                    )
                    .call()
                    .content();

            System.out.println("=================================");
            System.out.println("TRANSCRIPTION:");
            System.out.println(transcription);
            System.out.println("=================================");

            return ResponseEntity.ok(transcription);

        } catch (IOException e) {

            // Error while reading uploaded file
            return ResponseEntity.internalServerError()
                    .body("Could not read the uploaded audio file: "
                            + e.getMessage());

        } catch (Exception e) {

            e.printStackTrace();

            Throwable cause = e;

            while (cause != null) {
                System.out.println("CAUSE: " + cause.getClass().getName());
                System.out.println("MESSAGE: " + cause.getMessage());
                cause = cause.getCause();
            }

            return ResponseEntity.internalServerError()
                    .body("Error while processing audio: " + e.getMessage());

//
//            e.printStackTrace();
//
//            return ResponseEntity.internalServerError()
//                    .body("Error while processing audio: " + e.getMessage());
            // Any other unexpected error
//            return ResponseEntity.internalServerError()
//                    .body("Error while processing audio: "
//                            + e.getMessage());
        }
    }
}