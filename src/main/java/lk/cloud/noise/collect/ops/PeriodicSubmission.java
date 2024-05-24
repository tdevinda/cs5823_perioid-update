package lk.cloud.noise.collect.ops;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.cloud.noise.collect.dto.NoiseSample;
import lk.cloud.noise.collect.repository.SampleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Component
public class PeriodicSubmission {

    private Logger logger = LogManager.getLogger(getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SampleRepository sampleRepository;

    @Value("${noise.collector.url}")
    private String collectorUrl;

    /**
     * Fetches the stored data in the collector service and pushes them towards the redis database
     */
    @Scheduled(fixedRate = 5000)
    public void flushSavedItemsToDatabase() {
        String output = restTemplate.getForObject(collectorUrl, String.class);
        logger.info(output);

        List<NoiseSample> samples = parseOutput(output);
        samples.parallelStream().forEach(s -> {
            logger.info("Saving {}", s);
            sampleRepository.saveSample(s);
        });

    }

    /**
     * Read the string and create a List<NoiseSample> using jackson
     * @param output
     */
    private List<NoiseSample> parseOutput(String output) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode node = mapper.readTree(output);
            List<NoiseSample> samples = mapper.convertValue(node, new TypeReference<List<NoiseSample>>() {
            });

            return samples;
        } catch (JsonProcessingException e) {
            logger.error("Unable to convert", e);
            return null;
        }


    }


}
