package pro.alamgir.leadprocessor.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pro.alamgir.leadprocessor.dto.LeadDTO;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class LeadParser {
	private static final Logger ERROR_LOGGER = LoggerFactory.getLogger("ERROR_LOGGER");
	private static LeadParser instance;

	private LeadParser() {
	}

	public static LeadParser getInstance() {
		if (instance == null) {
			instance = new LeadParser();
		}

		return instance;
	}

	public List<LeadDTO> parse(InputStream stream) {
		List<LeadDTO> leads = new ArrayList<>();

		try {
			CSVParser parser = new CSVParser(new InputStreamReader(stream), CSVFormat.DEFAULT);

			for (CSVRecord record : parser) {
				LeadDTO lead = new LeadDTO();
				lead.setMsisdn(record.get(0));
				leads.add(lead);
			}
		} catch (IOException ex) {
			ERROR_LOGGER.error(ex.getMessage());
		}

		return leads;
	}
}
