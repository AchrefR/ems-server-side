package com.ppg.ems_server_side_v0.service.core.implementation;

import com.ppg.ems_server_side_v0.domain.CvAnalysis;
import com.ppg.ems_server_side_v0.domain.JobDescription;
import com.ppg.ems_server_side_v0.model.api.request.CvFilterRequest;
import com.ppg.ems_server_side_v0.model.api.response.CvAnalysisResponse;
import com.ppg.ems_server_side_v0.repository.CvAnalysisRepository;
import com.ppg.ems_server_side_v0.repository.JobDescriptionRepository;
import com.ppg.ems_server_side_v0.service.core.CvAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CvAnalysisServiceImpl implements CvAnalysisService {

    private final CvAnalysisRepository cvAnalysisRepository;
    private final JobDescriptionRepository jobDescriptionRepository;
    private final Tika tika = new Tika();

    @Value("${app.cv.upload.dir:uploads/cvs}")
    private String uploadDir;

    @Override
    public List<CvAnalysisResponse> analyzeCvs(List<MultipartFile> cvFiles, CvFilterRequest filterRequest) {
        log.info("Starting CV analysis for {} files", cvFiles.size());
        
        // Get or create job description
        JobDescription jobDescription = getJobDescription(filterRequest);
        
        List<CvAnalysis> analyses = new ArrayList<>();
        
        for (MultipartFile file : cvFiles) {
            try {
                CvAnalysis analysis = analyzeIndividualCv(file, jobDescription, filterRequest);
                analyses.add(analysis);
            } catch (Exception e) {
                log.error("Error analyzing CV: {}", file.getOriginalFilename(), e);
            }
        }
        
        // Sort by overall score and limit results
        analyses = analyses.stream()
                .sorted((a, b) -> Double.compare(b.getOverallScore(), a.getOverallScore()))
                .limit(filterRequest.limit())
                .collect(Collectors.toList());
        
        // Save analyses
        analyses = cvAnalysisRepository.saveAll(analyses);
        
        return analyses.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CvAnalysis analyzeIndividualCv(MultipartFile file, JobDescription jobDescription, CvFilterRequest filterRequest) throws IOException {
        // Extract text from PDF
        String extractedText = extractTextFromPdf(file);
        
        // Calculate scores
        Double keywordScore = calculateKeywordScore(extractedText, jobDescription.getDescription());
        Double semanticScore = calculateSemanticScore(extractedText, jobDescription.getDescription());
        
        // Calculate overall score
        Double overallScore = (keywordScore * filterRequest.keywordWeight()) + 
                             (semanticScore * filterRequest.semanticWeight());
        
        // Save file
        String filePath = saveFile(file);
        
        // Extract keyword matches
        Map<String, Integer> keywordMatches = extractKeywordMatches(extractedText, jobDescription.getDescription());
        List<String> matchedPhrases = extractMatchedPhrases(extractedText, jobDescription.getDescription());
        
        return CvAnalysis.builder()
                .jobDescription(jobDescription)
                .fileName(generateUniqueFileName(file.getOriginalFilename()))
                .originalFileName(file.getOriginalFilename())
                .extractedText(extractedText)
                .overallScore(overallScore)
                .keywordScore(keywordScore)
                .semanticScore(semanticScore)
                .keywordMatches(keywordMatches)
                .matchedPhrases(matchedPhrases)
                .filePath(filePath)
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .build();
    }

    @Override
    public String extractTextFromPdf(MultipartFile file) {
        try {
            return tika.parseToString(file.getInputStream());
        } catch (Exception e) {
            log.error("Error extracting text from PDF: {}", file.getOriginalFilename(), e);
            return "";
        }
    }

    @Override
    public Double calculateKeywordScore(String cvText, String jobDescription) {
        if (cvText == null || jobDescription == null) {
            return 0.0;
        }
        
        // Convert to lowercase and split into words
        Set<String> cvWords = Arrays.stream(cvText.toLowerCase().split("\\W+"))
                .filter(word -> word.length() > 2)
                .collect(Collectors.toSet());
        
        Set<String> jobWords = Arrays.stream(jobDescription.toLowerCase().split("\\W+"))
                .filter(word -> word.length() > 2)
                .collect(Collectors.toSet());
        
        // Calculate intersection
        Set<String> intersection = new HashSet<>(cvWords);
        intersection.retainAll(jobWords);
        
        // Calculate Jaccard similarity
        Set<String> union = new HashSet<>(cvWords);
        union.addAll(jobWords);
        
        return union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
    }

    @Override
    public Double calculateSemanticScore(String cvText, String jobDescription) {
        if (cvText == null || jobDescription == null) {
            return 0.0;
        }

        // Simple TF-IDF based cosine similarity
        Map<CharSequence, Integer> cvVector = createWordVector(cvText);
        Map<CharSequence, Integer> jobVector = createWordVector(jobDescription);

        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        return cosineSimilarity.cosineSimilarity(cvVector, jobVector);
    }

    private Map<CharSequence, Integer> createWordVector(String text) {
        return Arrays.stream(text.toLowerCase().split("\\W+"))
                .filter(word -> word.length() > 2)
                .collect(Collectors.groupingBy(
                        word -> (CharSequence) word,
                        Collectors.summingInt(word -> 1)
                ));
    }

    private Map<String, Integer> extractKeywordMatches(String cvText, String jobDescription) {
        Map<String, Integer> matches = new HashMap<>();
        String[] jobWords = jobDescription.toLowerCase().split("\\W+");
        String cvLower = cvText.toLowerCase();
        
        for (String word : jobWords) {
            if (word.length() > 2) {
                int count = countOccurrences(cvLower, word);
                if (count > 0) {
                    matches.put(word, count);
                }
            }
        }
        
        return matches;
    }

    private List<String> extractMatchedPhrases(String cvText, String jobDescription) {
        List<String> phrases = new ArrayList<>();
        String[] jobSentences = jobDescription.split("[.!?]+");
        String cvLower = cvText.toLowerCase();
        
        for (String sentence : jobSentences) {
            String[] words = sentence.trim().toLowerCase().split("\\W+");
            if (words.length >= 2 && words.length <= 5) {
                String phrase = String.join(" ", words);
                if (cvLower.contains(phrase)) {
                    phrases.add(phrase.trim());
                }
            }
        }
        
        return phrases.stream().distinct().limit(10).collect(Collectors.toList());
    }

    private int countOccurrences(String text, String word) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }
        return count;
    }

    private String saveFile(MultipartFile file) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate unique filename
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(fileName);
        
        // Save file
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return filePath.toString();
    }

    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        return UUID.randomUUID().toString() + extension;
    }

    private JobDescription getJobDescription(CvFilterRequest filterRequest) {
        if (filterRequest.jobDescriptionId() != null) {
            return jobDescriptionRepository.findById(filterRequest.jobDescriptionId())
                    .orElseThrow(() -> new RuntimeException("Job description not found"));
        }

        // Create and save temporary job description for analysis
        JobDescription tempJobDescription = JobDescription.builder()
                .title("Temporary Job Description")
                .description(filterRequest.jobDescriptionText())
                .requirements("")
                .responsibilities("")
                .department("Temporary")
                .location("Remote")
                .salaryRange("TBD")
                .experienceLevel("Any")
                .build();

        // Save the temporary job description to avoid transient entity issues
        return jobDescriptionRepository.save(tempJobDescription);
    }

    @Override
    public List<CvAnalysisResponse> getCvAnalysesByJobDescription(String jobDescriptionId) {
        return cvAnalysisRepository.findByJobDescriptionIdOrderByOverallScoreDesc(jobDescriptionId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] downloadCvFile(String analysisId) {
        CvAnalysis analysis = cvAnalysisRepository.findById(analysisId)
                .orElseThrow(() -> new RuntimeException("CV analysis not found"));
        
        try {
            return Files.readAllBytes(Paths.get(analysis.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Error reading CV file", e);
        }
    }

    private CvAnalysisResponse mapToResponse(CvAnalysis analysis) {
        return new CvAnalysisResponse(
                analysis.getId(),
                analysis.getFileName(),
                analysis.getOriginalFileName(),
                analysis.getOverallScore(),
                analysis.getKeywordScore(),
                analysis.getSemanticScore(),
                analysis.getKeywordMatches(),
                analysis.getMatchedPhrases(),
                analysis.getAnalyzedAt(),
                analysis.getFileSize(),
                analysis.getContentType(),
                "/api/hr/cv-analysis/" + analysis.getId() + "/download"
        );
    }
}
