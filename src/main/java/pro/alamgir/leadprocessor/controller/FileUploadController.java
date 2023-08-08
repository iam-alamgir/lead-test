package pro.alamgir.leadprocessor.controller;

import lombok.AllArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import pro.alamgir.leadprocessor.dto.LeadDTO;
import pro.alamgir.leadprocessor.entity.Lead;
import pro.alamgir.leadprocessor.service.LeadService;
import pro.alamgir.leadprocessor.utils.LeadParser;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class FileUploadController {
	private static final Logger INFO_LOGGER = LoggerFactory.getLogger("INFO_LOGGER");
	private static final Logger ERROR_LOGGER = LoggerFactory.getLogger("ERROR_LOGGER");

	private final LeadParser parser;
	private final LeadService leadService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		INFO_LOGGER.info(file.getOriginalFilename());
		INFO_LOGGER.info(String.valueOf(file.getSize()));

		try {
			List<LeadDTO> leadsDto = parser.parse(file.getInputStream());

			ExecutorService executor = Executors.newFixedThreadPool(5);

			final int batchSize = 10_000;
			List<List<LeadDTO>> batchedLists = ListUtils.partition(leadsDto, batchSize);

			INFO_LOGGER.info("Total chunk created: " + batchedLists.size());

			for (List<LeadDTO> batch : batchedLists) {
				executor.submit(() -> {
					List<Lead> leads = batch.stream().map(dto -> {
						Lead lead = new Lead();
						lead.setMsisdn(dto.getMsisdn());
						return lead;
					}).collect(Collectors.toList());

					leadService.saveBatch(leads);

					INFO_LOGGER.info("Chunk processed");
				});
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			ERROR_LOGGER.error(ex.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload the file");
		}

		return ResponseEntity.accepted().body("File uploaded and processing has started");
	}
}
