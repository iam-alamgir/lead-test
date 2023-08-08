package pro.alamgir.leadprocessor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pro.alamgir.leadprocessor.entity.Lead;

public interface LeadRepository extends JpaRepository<Lead, Long> {
}
