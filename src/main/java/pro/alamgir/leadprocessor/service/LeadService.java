package pro.alamgir.leadprocessor.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.alamgir.leadprocessor.entity.Lead;
import pro.alamgir.leadprocessor.repository.LeadRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadService {
	private final LeadRepository repository;

	@Transactional
	public void saveBatch(List<Lead> leads) {
		repository.saveAll(leads);
	}
}
