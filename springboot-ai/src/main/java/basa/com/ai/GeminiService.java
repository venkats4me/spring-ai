package basa.com.ai;

import com.google.genai.Client;
import com.google.genai.types.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class GeminiService {
    @Value("${gemini.model}")
    private String modelName;
    private final Client client;
    @Value("${gemini.image.model}")
    private String imageModelName;

    // Spring automatically injects the Client we created in Step 2
    public GeminiService(Client client) {
        this.client = client;
    }

    public String getAiResponse(String prompt) {
        try {
            GenerateContentResponse response = client.models.generateContent(
                    modelName,
                    prompt,
                    null
            );
            System.out.println(response.text());
            return response.text();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate AI response", e);
        }
    }

    public String generateImage(String prompt) {
        try {
            // Request IMAGE output modality
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseModalities(List.of("IMAGE", "TEXT"))
                    .build();

            GenerateContentResponse response = client.models.generateContent(
                    imageModelName,
                    prompt,
                    config
            );

            // Extract base64 image from response parts
            List<Part> parts = response.candidates()
                    .orElseThrow(() -> new RuntimeException("No candidates returned"))
                    .get(0)
                    .content()
                    .orElseThrow(() -> new RuntimeException("No content in response"))
                    .parts()
                    .orElseThrow(() -> new RuntimeException("No parts in content"));

            for (Part part : parts) {
                if (part.inlineData().isPresent()) {
                    Blob blob = part.inlineData().get();
                    String base64Data = Arrays.toString(blob.data()
                            .orElseThrow(() -> new RuntimeException("No image data")));
                    String mimeType = blob.mimeType()
                            .orElse("image/png");

                    // Returns a data URI — directly usable in <img src="...">
                    return "data:" + mimeType + ";base64," + base64Data;
                }
            }

            throw new RuntimeException("No image found in response parts");

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate image: " + e.getMessage(), e);
        }
    }

    /*public byte[] generateImageAsBytes(String prompt) {
        try {
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseModalities(Arrays.asList("IMAGE"))
                    .build();

            GenerateContentResponse response = client.models.generateContent(
                    imageModelName,
                    prompt,
                    config
            );

            // Safely check if we have candidates
            if (response.candidates() == null || response.candidates().isEmpty()) {
                throw new RuntimeException("No candidates returned from AI.");
            }

            Candidate firstCandidate = response.candidates().get().get(0);

            // Safely unwrap the parts list. If parts() returns an Optional, .orElseThrow() gets the List.
            List<Part> parts = firstCandidate.content().wait().orElseThrow(() ->
                    new RuntimeException("No content parts found in the response.")
            );

            if (parts.isEmpty()) {
                throw new RuntimeException("The parts list is empty.");
            }

            // Extract the inline data.
            // (Using .orElseThrow() is the safest way to unwrap an Optional in Java)
            String base64ImageData = parts.get(0).inlineData()
                    .orElseThrow(() -> new RuntimeException("No inline image data found."))
                    .data();

            return Base64.getDecoder().decode(base64ImageData);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate AI image: " + e.getMessage(), e);
        }
    }*/








    /*public byte[] generateImageAsBytes(String prompt) {
        try {
            // 1. Configure the request
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseModalities(Arrays.asList("IMAGE"))
                    .build();

            // 2. Call the AI Model
            GenerateContentResponse response = client.models.generateContent(
                    imageModelName,
                    prompt,
                    config
            );

            // 3. Get the first Candidate from the response list
            Candidate firstCandidate = response.candidates().get().get(0);

            // 4. Unwrap the Content Optional
            var content = firstCandidate.content().get();

            // 5. Unwrap the Parts List Optional, THEN get the first Part
            // (This is the specific line that was causing your error!)
            var partsList = content.parts().get();
            var firstPart = partsList.get(0);

            // 6. Unwrap the InlineData Optional
            var inlineData = firstPart.inlineData().get();

            // 7. Unwrap the final byte[] Optional
            byte[] imageBytes = inlineData.data().get();

            return imageBytes;

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate AI image: " + e.getMessage(), e);
        }
    }*/
   /* public byte[] generateImageAsBytes(String prompt) {
        try {
            // 1. Configure the request for an IMAGE
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseModalities(Arrays.asList("IMAGE"))
                    .build();

            // 2. Call the AI Model
            GenerateContentResponse response = client.models.generateContent(
                    imageModelName,
                    prompt,
                    config
            );

            // 3. Safely check for candidates
            if (response.candidates() == null || response.candidates().isEmpty()) {
                throw new RuntimeException("No candidates returned from AI.");
            }

            Candidate firstCandidate = response.candidates().get(0);

            // 4. Safely unwrap the Optionals to get the final byte[]
            // Using .orElseThrow() cleanly unwraps the Optional or throws a readable error if missing
            return firstCandidate.content().orElseThrow(() -> new RuntimeException("No content found"))
                    .parts().orElseThrow(() -> new RuntimeException("No parts found"))
                    .get(0)
                    .inlineData().orElseThrow(() -> new RuntimeException("No inline data found"))
                    .data().orElseThrow(() -> new RuntimeException("No image byte data found"));

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate AI image: " + e.getMessage(), e);
        }
    }*/
    /*public byte[] generateImageAsBytes(String prompt) {
        try {
            GenerateContentConfig config = GenerateContentConfig.builder()
                    .responseModalities(Arrays.asList("IMAGE"))
                    .build();

            GenerateContentResponse response = client.models.generateContent(
                    imageModelName,
                    prompt,
                    config
            );

            // Safely check if we have candidates
            if (response.candidates() == null || response.candidates().isEmpty()) {
                throw new RuntimeException("No candidates returned from AI.");
            }

            Candidate firstCandidate = response.candidates().get(0);

            // Safely unwrap the parts list. If parts() returns an Optional, .orElseThrow() gets the List.
            List<Part> parts = firstCandidate.content().parts().orElseThrow(() ->
                    new RuntimeException("No content parts found in the response.")
            );

            if (parts.isEmpty()) {
                throw new RuntimeException("The parts list is empty.");
            }

            // Extract the inline data.
            // (Using .orElseThrow() is the safest way to unwrap an Optional in Java)
            String base64ImageData = parts.get(0).inlineData()
                    .orElseThrow(() -> new RuntimeException("No inline image data found."))
                    .data();

            return Base64.getDecoder().decode(base64ImageData);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate AI image: " + e.getMessage(), e);
        }
    }*/
}