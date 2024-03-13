package ua.dlc.chscbackend.service;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GoogleChatServiceImpl implements GoogleChatService {

    private final String projectId;
    private final String location;
    private final String modelName;

    public GoogleChatServiceImpl(@Value("${spring.cloud.gcp.project-id}") String projectId,
                                 @Value("${google.project.location}") String location,
                                 @Value("${google.model.name}") String modelName) {
        this.projectId = projectId;
        this.location = location;
        this.modelName = modelName;
    }

    @Override
    public Mono<String> getChatResponse(String userMessage) {
        return Mono.fromCallable(() -> {
            VertexAI vertexAI = new VertexAI(projectId, location);
            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
            GenerateContentResponse response;
            try {
                response = model.generateContent(ContentMaker.fromMultiModalData(userMessage));
                // Extract the text content from the response
                return extractTextContent(response.toString());
            } catch (IOException e) {
                // Convert the checked exception to a runtime exception
                throw new RuntimeException(e);
            }
        }).subscribeOn(Schedulers.boundedElastic()); // Offload the blocking I/O to a separate thread.
    }

    private String extractTextContent(String response) {
        Pattern pattern = Pattern.compile("text: \"(.+?)\"\\n", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1).replace("\\n", "\n").replace("\\\"", "\"");
        }
        return ""; // Return an empty string if the pattern is not found
    }


}