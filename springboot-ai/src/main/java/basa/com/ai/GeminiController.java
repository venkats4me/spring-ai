package basa.com.ai;


import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class GeminiController {

    private final GeminiService geminiService;

    // Spring injects your service here
    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // You can call this endpoint via a GET request
    @GetMapping("/ask")
    public String askGemini(@RequestParam String prompt) {
        return geminiService.getAiResponse(prompt);
    }
    /*@GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] generateAiImage(@RequestParam String prompt) {
        return geminiService.generateImageAsBytes(prompt);
    }*/
    @GetMapping("/generate-image")
    public ResponseEntity<Map<String, String>> generateImage(@RequestParam String prompt) {
        String imageData = geminiService.generateImage(prompt);
        Map<String, String> response = new HashMap<>();
        response.put("image", imageData);          // base64 data URI → use in <img src="...">
        response.put("prompt", prompt);
        return ResponseEntity.ok(response);
    }
}
