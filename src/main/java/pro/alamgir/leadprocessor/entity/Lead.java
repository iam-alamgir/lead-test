package pro.alamgir.leadprocessor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leads")
@Data
@NoArgsConstructor
public class Lead {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@TableGenerator(name="lead_generator", table="lead_sqn", schema="test")
	private Long id;
	private String msisdn;
}
