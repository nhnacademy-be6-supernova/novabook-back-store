package store.novabook.store.category.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "top_category_id")
	private Category topCategory;

	@NotNull
	private String name;

	@NotNull
	private LocalDateTime createdAt;

	@Null
	private LocalDateTime updatedAt;

	public Category(Category topCategory, String name) {
		this.topCategory = topCategory;
		this.name = name;
		this.createdAt = LocalDateTime.now();
	}

	public Category(String name) {
		this.name = name;
		this.createdAt = LocalDateTime.now();
	}

	// 해당 카테고리가 Top 카테고리인지 여부를 반환하는 메소드
	public boolean hasTopCategory() {
		return this.topCategory != null;
	}
}
