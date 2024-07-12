package store.novabook.store.book.entity;

import store.novabook.store.common.exception.BadRequestException;
import store.novabook.store.common.exception.ErrorCode;

public enum BookStatusEnum {
	FOR_SALE(1, "판매중"),
	OUT_OF_STOCK(2, "일시품절"),
	DISCONTINUED(3, "판매종료"),
	DELETED(4, "삭제도서");

	private final int value;
	private final String koreanValue;

	BookStatusEnum(int value, String koreanValue) {
		this.value = value;
		this.koreanValue = koreanValue;
	}

	public long getValue() {
		return value;
	}

	public String getKoreanValue() {
		return koreanValue;
	}

	public static BookStatusEnum fromValue(int value) {
		for (BookStatusEnum status : BookStatusEnum.values()) {
			if (status.value == value) {
				return status;
			}
		}
		throw new BadRequestException(ErrorCode.NOT_EXIST_BOOK_STATUS);
	}

	public static BookStatusEnum fromKoreanValue(String koreanValue) {
		for (BookStatusEnum status : BookStatusEnum.values()) {
			if (status.koreanValue.equals(koreanValue)) {
				return status;
			}
		}
		throw new BadRequestException(ErrorCode.NOT_EXIST_BOOK_STATUS);
	}
}