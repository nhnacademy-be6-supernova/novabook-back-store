package store.novabook.store.tag.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(unique = true)
	private String name;

	@NotNull
	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public Tag(String Name) {
		this.name = Name;
		this.createdAt = LocalDateTime.now();
	}

	public void update(String name) {
		this.name = name;
		this.updatedAt = LocalDateTime.now();
	}
}
